package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //check address book exists
        Long addressbBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = new AddressBook();
        addressBook.setId(addressbBookId);
        if (addressBookMapper.listByUserId(addressBook) == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //check shopping cart not empty
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(1L);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //save orders
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(1L);
        orderMapper.insert(orders);

        //save order detail
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        }

        orderDetailMapper.insertBatch(orderDetails);

        //clear shopping cart
        shoppingCartMapper.delete(shoppingCart);

        //通过websocket向客户端浏览器推送来单提醒
        Map map = new HashMap();
        map.put("type", 1); // 1代表来单提醒 2表示客户催单
        map.put("orderId", orders.getId());
        map.put("content", "订单号" + orders.getNumber());

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);

        //return orderSubmitVO
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);

        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Map map = new HashMap();
        map.put("type", 2); // 1代表来单提醒 2表示客户催单
        map.put("orderId", orders.getId());
        map.put("content", "订单号" + orders.getNumber());

        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }
}
