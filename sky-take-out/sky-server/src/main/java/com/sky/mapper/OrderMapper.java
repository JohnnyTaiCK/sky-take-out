package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    // 查询某个状态且下单时间小于某个时间的订单
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    void update(Orders orders);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
}
