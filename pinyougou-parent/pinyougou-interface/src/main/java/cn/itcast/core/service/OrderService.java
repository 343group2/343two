package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer pageNum, Integer pageSize, Order order);

    List<OrderItem> findByOrderId(Long orderId);


}
