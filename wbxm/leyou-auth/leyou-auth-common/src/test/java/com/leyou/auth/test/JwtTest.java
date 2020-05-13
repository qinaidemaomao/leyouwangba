package com.leyou.auth.test;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JwtUtils;
import com.leyou.common.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author lizichen
 * @create 2020-04-26 13:48
 */
public class JwtTest {
      //公钥地址
    private static final String pubKeyPath = "G:\\study\\leyou\\tool\\JwtandRSA\\rsa.pub";
    //私钥地址
    private static final String priKeyPath = "G:\\study\\leyou\\tool\\JwtandRSA\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        //盐越复杂越好
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }
   //读取公钥 和私钥
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4Nzg4MDkzMX0.VDfOtIbuZWGCaixemadpcXp-48dVreOwYU3yj2s5cBl-1jhi-21CVfc2qdbC8VYqS9ky2Iy1Fehlj_2kdTcqKRI9agM8OM_tPqQTLLOicb4kmSFyZQp-tco1Cxj78ILb9rIt2qUl1V42q4F4Hvh6meEcAc6pQGV9KrckWrbJPvw";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
