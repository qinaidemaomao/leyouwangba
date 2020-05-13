package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-15 20:30
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
