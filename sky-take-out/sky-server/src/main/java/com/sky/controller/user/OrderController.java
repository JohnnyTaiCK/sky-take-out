package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端-订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation("提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody  OrdersSubmitDTO ordersSubmitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @GetMapping("/reminder/{id}")
    @ApiOperation("客户催单")
    public Result reminder(@PathVariable("id") Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}
