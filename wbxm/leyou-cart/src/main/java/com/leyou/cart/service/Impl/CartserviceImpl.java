package com.leyou.cart.service.Impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LonginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.Cartservice;
import com.leyou.common.pojo.UserInfo;
import com.leyou.item.pojo.Sku;
import cpm.leyou.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lizichen
 * @create 2020-04-28 21:13
 */
@Service
public class CartserviceImpl implements Cartservice {

   @Autowired
   private GoodsClient goodsClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX="user:cart:";

    /**
     * 登录状态删除购物车商品
     * @param skuId
     */
    @Override
    public void deleteCart(String skuId) {
        UserInfo userInfo=LonginInterceptor.getUserInfo();
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        hashOperations.delete(skuId);
    }

    /**
     * 更新购物车数据
     * @param cart
     */
    @Override
    public void updateNum(Cart cart) {
        UserInfo userInfo=LonginInterceptor.getUserInfo();
        //判断用户是否有购物车记录
        if (!this.stringRedisTemplate.hasKey(KEY_PREFIX + userInfo.getId())){
            return;
        }
        Integer num = cart.getNum();
        //获取购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
       //反序列化
          cart=JsonUtils.parse(cartJson,Cart.class);
          cart.setNum(num);
          hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));

    }

    /**
     * 登录状态查询购物车
     * @return
     */
    @Override
    public List<Cart> queryCarts() {
        UserInfo userInfo=LonginInterceptor.getUserInfo();
        //判断用户是否有记录
        if (!this.stringRedisTemplate.hasKey(KEY_PREFIX + userInfo.getId())){
            return null;
        }
        //获取用户的购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        //获取购物车中Map中所有Cart集合
        List<Object> cartsJson=hashOperations.values();
         //如果购物车集合为空
        if (CollectionUtils.isEmpty(cartsJson)){
            return null;
        }
        return cartsJson.stream().map(cartJson-> JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 登录状态下 添加购物车
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //获取用户ID
        UserInfo userInfo = LonginInterceptor.getUserInfo();
        //查询购物记录
        BoundHashOperations<String, Object, Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String key=cart.getSkuId().toString();
        Integer num=cart.getNum();
        //是否在购物车中，在的话 更新数量，不在新增购物车
        if (hashOperations.hasKey(key)) {
            //在更新
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(cart.getNum()+num);
            hashOperations.put(key,JsonUtils.serialize(cart));
        }else {
            //不在添加购物车
             Sku sku = this.goodsClient.queryBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages())? "":StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
        }
         //最终都要设置给redise
        hashOperations.put(key,JsonUtils.serialize(cart));
    }
}
