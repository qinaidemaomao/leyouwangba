package com.leyou.goods.client;

import com.leyou.item.api.Goodsapi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-15 16:37
 */
@FeignClient("item-service")
public interface GoodsClient extends Goodsapi {

}
