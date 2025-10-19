package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    void reminder(Long id);

    PageResult pageQuery(int page, int pageSize, Integer status);

    OrderVO details(Long id);

    void cancel(Long orderId);

    void repetition(Long orderId);
}
