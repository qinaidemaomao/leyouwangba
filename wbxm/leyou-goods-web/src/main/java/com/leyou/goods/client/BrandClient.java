package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-15 20:29
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
