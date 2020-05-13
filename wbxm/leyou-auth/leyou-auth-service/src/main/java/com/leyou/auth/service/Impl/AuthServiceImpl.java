package com.leyou.auth.service.Impl;

import com.leyou.auth.clent.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lizichen
 * @create 2020-04-26 14:13
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserClient userClient;
    /**
     * 登录创建JWT
     * @param username
     * @param password
     * @return
     */
    @Override
    public String accredit(String username, String password) {
        //根据用户名密码查询
        User user = this.userClient.quetyUser(username, password);
        //判断是否为空
        if (user==null){
            return null;
        }
        try {
            //不为空 通过JWT 生成 token
            UserInfo userInfo=new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            return  JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
