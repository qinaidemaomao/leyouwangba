package com.user.service;

import com.user.pojo.User;

/**
 * @author lizichen
 * @create 2020-04-24 0:09
 */
public interface UserService {
    /**
     * 检查是否可用
     * @param data
     * @param type
     * @return
     */
    Boolean checkUser(String data, Integer type);

    /**
     * 发送验证码
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 注册账户
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * 根据用户名密码查询
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);
}
