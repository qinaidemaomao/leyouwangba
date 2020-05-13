package com.leyou.order.service.api;

import com.leyou.item.api.Goodsapi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-gateway", path = "/api/item")
public interface GoodsService extends Goodsapi {
}
