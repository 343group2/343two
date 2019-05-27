package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/orders")
public class OrderController {


    @Reference
    private OrderService orderService;
     @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){

         //当前登陆人
         String name = SecurityContextHolder.getContext().getAuthentication().getName();
         order.setSellerId(name);
          return orderService.search(page,rows,order);
     }

    @RequestMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status){
         try {
             orderService.updateStatus(ids,status);
             return new Result(true,"确认订单成功");
         } catch (Exception e) {
             e.printStackTrace();
             return new Result(false,"确认订单失败");
         }
     }


}
