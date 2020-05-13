package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author lizichen
 * @create 2020-04-15 20:29
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
