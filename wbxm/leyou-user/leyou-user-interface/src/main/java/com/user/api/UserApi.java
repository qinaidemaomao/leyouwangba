package com.user.api;

import com.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lizichen
 * @create 2020-04-26 14:27
 */
public interface UserApi {

    /**
     * 根据用户密码查询 用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public User quetyUser(@RequestParam("username")String username, @RequestParam("password")String password);

    }
