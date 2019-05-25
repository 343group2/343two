package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import entity.PageResult;

public interface UserService {
    void sendCode(String phone);

    void add(String smscode, User user);
    //分页查询用户列表
    PageResult search(Integer pageNum, Integer pageSize, User user);

    //根据id修改用户状态
    User findOne(Long id);

    //通过id修改用户状态
    void update(User user);
}
