package cn.itcast.core.controller;

import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 查询分页对象 条件
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody User user) {
        return userService.search(pageNum, pageSize,user);

    }

    //修改
    @RequestMapping("/update")
    public Result update(@RequestBody User user) {

        try {
            //保存
            System.out.println(user);
            userService.update(user);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }

    }
    //查询一个用户
    @RequestMapping("/findOne")
    public User findOne(Long id){
        return userService.findOne(id);
    }
}
