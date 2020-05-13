package com.leyou.item.service;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import cpm.leyou.common.pojo.PageResult;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-05 0:07
 */
public interface GoodsService {
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkusBySouId(Long spuId);

    void updateGoods(SpuBo spuBo);

    void deleteGoods(Long id);

    void goodsSoldOut(Long id);

    Spu querySpuById(Long id);

    Sku queryBySkuId(Long skuId);
}
