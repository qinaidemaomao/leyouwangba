package com.leyou.goods.service;


import java.util.Map;

/**
 * @author lizichen
 * @create 2020-04-22 10:47
 */
public interface GoodsService {
    /**
     * 商品详情页面数据加载
     * @param spuId
     * @return
     */
    public Map<String ,Object> loadData(Long spuId);
}
