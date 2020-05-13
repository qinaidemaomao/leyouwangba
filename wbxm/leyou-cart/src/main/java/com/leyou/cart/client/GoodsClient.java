package com.leyou.cart.client;

import com.leyou.item.api.Goodsapi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-29 18:28
 */
@FeignClient("item-service")
public interface GoodsClient extends Goodsapi {
}
