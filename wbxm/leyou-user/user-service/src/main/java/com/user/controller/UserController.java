package com.user.controller;

import com.user.pojo.User;
import com.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lizichen
 * @create 2020-04-24 0:10
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;



    /**
     * 检验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Integer type){
      Boolean bool=  this.userService.checkUser(data,type);
      if (bool ==null){
          return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok(bool);
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    /**
     * 注册用户
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        this.userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户密码查询 用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> quetyUser(@RequestParam("username")String username,@RequestParam("password")String password){
         User user=this.userService.queryUser(username,password);
         if (user==null){
             return ResponseEntity.badRequest().build();
         }
         return ResponseEntity.ok(user);
    }
}
