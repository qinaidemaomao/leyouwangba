package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.service.AuthService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import cpm.leyou.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizichen
 * @create 2020-04-26 14:10
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 退出
     * @param token
     * @param request
     * @param response
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void>  outLogin(@CookieValue("LY_TOKEN")String  token,
                                          HttpServletRequest request,
                                          HttpServletResponse response){

        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),
                token, 0, null, true);
        return ResponseEntity.ok().build();
    }
    /**
     **登录
     * @param token
     * @param request
     * @param response
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("LY_TOKEN")String  token,
                                           HttpServletRequest request,
                                           HttpServletResponse response){
        try {
            //通过JWT使用公钥解析
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            if (user ==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            //解析成功后刷新JWT有效时间
            //重新生成一个
            token = JwtUtils.generateToken(user, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());
            //刷新Cookie的有效时间
            //重新生成
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),
                    token, jwtProperties.getExpire()*60, null, true);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * 登录获取 JWT
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username")String username,
                                         @RequestParam("password")String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
       String token= this.authService.accredit(username,password);
        if (StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(),
                token, jwtProperties.getExpire()*60, null, true);
    return ResponseEntity.ok(null);
    }
}
