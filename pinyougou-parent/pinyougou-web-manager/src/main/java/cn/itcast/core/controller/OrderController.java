package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @Reference
    private OrderService orderService;
    //查询分页对象 条件
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order) {
        return orderService.search(page, rows,order);
    }

    //根据订单Id 查询订单详情
    @RequestMapping("/findByOrderId")
    public List<OrderItem> findByOrderId(Long orderId){
        return orderService.findByOrderId(orderId);
    }
}
