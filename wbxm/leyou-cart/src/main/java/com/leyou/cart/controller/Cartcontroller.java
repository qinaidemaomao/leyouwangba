package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.Cartservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-29 17:51
 */
@Controller
public class Cartcontroller {

    @Autowired
    private Cartservice cartservice;


    @PutMapping("add")
    public ResponseEntity<Void> addAllCart(@RequestBody Cart cart){
        this.cartservice.addCart(cart);
        return ResponseEntity.ok().build();
    }
    /**
     * 登录状态 删除商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")String skuId){
        this.cartservice.deleteCart(skuId);
        return ResponseEntity.ok().build();

    }
    /**
     * 更新购物车数据
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
             this.cartservice.updateNum(cart);
             return ResponseEntity.noContent().build();
    }

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartservice.addCart(cart);
        return ResponseEntity.ok().build();
    }

    /**
     * 登录后查询购物车
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts=this.cartservice.queryCarts();
        if (CollectionUtils.isEmpty(carts)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }
}
