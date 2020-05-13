package com.user.service.Impl;


import com.user.mapper.UserMapper;
import com.user.pojo.User;
import com.user.service.UserService;
import com.user.utils.CodecUtils;
import cpm.leyou.common.utils.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lizichen
 * @create 2020-04-24 0:09
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    //前缀
    private static final String KEY_PREFIX="user:verify:";

    /**
     * 根据用户名密码查询
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        User record=new User();
        record.setUsername(username);
        User user=this.userMapper.selectOne(record);
        //判断是否为空
        if (user==null){
            return null;
        }
        //获取盐，对用户输入的密码进行加盐加密
        password=CodecUtils.md5Hex(password,user.getSalt());
        //和数据中的密码比较
        if (StringUtils.equals(password,user.getPassword())){
            return user;
        }
        return null;
    }

    /**
     * 注册用户
     * @param user
     * @param code
     */
    @Override
    public void register(User user, String code) {
        //查询redis验证码
        String redisCode = (String) this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //校验验证码
        if (!StringUtils.equals(code,redisCode)){
            return;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //加盐 加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));
        //新增用户
        user.setId(null);
        user.setCreated(new Date());
         this.userMapper.insertSelective(user);
         //删除 验证码

    }

    /**
     * 发送验证码
     * @param phone
     */
    @Override
    public void sendVerifyCode(String phone) {
        //验证手机号
        if (StringUtils.isBlank(phone)){
            return;
        }
        //生成验证码6位
        String code = NumberUtils.generateCode(6);

        //发送消息MQ
        Map<String ,String>msg =new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        this.amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);
        //把验证信息保存到redis
        this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,code,5, TimeUnit.MINUTES);
    }

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User record=new User();
        if (type==1){
            record.setUsername(data);
        }else if (type==2){
            record.setPhone(data);
        }else {
            return null;
        }
        return this.userMapper.selectCount(record) ==0;
    }
}
