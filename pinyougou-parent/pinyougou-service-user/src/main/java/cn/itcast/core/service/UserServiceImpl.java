package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private UserDao userDao;

    //生成验证码
    @Override
    public void sendCode(String phone) {

        //1:6位随机整数
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        //2:保存验证码到缓存中
        redisTemplate.boundValueOps(phone).set(randomNumeric);
        //存活时间  实际上1分钟  5分钟
        redisTemplate.boundValueOps(phone).expire(60, TimeUnit.MINUTES);

        //3:发消息
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

     /*           request.putQueryParameter("PhoneNumbers", map.get("PhoneNumbers"));//"17862655057"
                request.putQueryParameter("SignName", map.get("SignName"));//"品优购商城"
                request.putQueryParameter("TemplateCode", map.get("TemplateCode"));//"SMS_126462276"
                request.putQueryParameter("TemplateParam",map.get("TemplateParam"));//"{'number':123456}"*/

                MapMessage map = session.createMapMessage();
//                验证码
                map.setString("TemplateParam","{\"code\":"+randomNumeric+"}");
//                        手机号
                map.setString("PhoneNumbers",phone);
//                签名
                map.setString("SignName","有钱花商城");
//                        模板  注册
                map.setString("TemplateCode","SMS_165676421");

                return map;
            }
        });




    }

    //注册
    @Override
    public void add(String smscode, User user) {
        //1:判断验证码是否正确
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(null != code){
            //判断是否正确
            if(code.equals(smscode)){
                //用户名
                //密码 本次未加密
                //手机号
                //添加时间
                user.setCreated(new Date());
                user.setUpdated(new Date());
                //添加用户状态
                user.setStatus("Y");
                //保存
                userDao.insertSelective(user);
            }else{
                throw new RuntimeException("验证码错误");
            }
        }else{
            //
            throw new RuntimeException("风控验证码失效");
        }
    }

    /**
     * 分页查询用户列表
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, User user) {
        PageHelper.startPage(pageNum, pageSize);

        //创建条件
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();

        //判断用户名称
        if(null != user.getUsername() && !"".equals(user.getUsername())){
            criteria.andUsernameEqualTo("%" + user.getUsername() + "%");
        }
        //判断用户状态
        if(null != user.getStatus() && !"".equals(user.getStatus())){
            criteria.andStatusEqualTo(user.getStatus());
        }
        Page<User> brandPage = (Page<User>) userDao.selectByExample(userQuery);

        PageResult pageResult = new PageResult(brandPage.getTotal(), brandPage.getResult());
        return pageResult;

    }

    //查询一个用户
    @Override
    public User findOne(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    /**
     * 通过id修改用户状态
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    /**
     * 通过用户名查询
     * @param username
     */
    @Override
    public User findOneByname(String username) {
        //创建条件
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andUsernameEqualTo(username);
        User user = (User) userDao.selectByExample(userQuery);
        return user;
    }
}
