package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-28 21:12
 */
public interface Cartservice {
    /**
     * 登录状态下 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 登录状态下查询购物车
     * @return
     */
    List<Cart> queryCarts();

    /**
     *更新购物车数量
     * @param cart
     */
    void updateNum(Cart cart);

    /**
     * 登录状态删除购物车商品
     * @param skuId
     */
    void deleteCart(String skuId);
}
