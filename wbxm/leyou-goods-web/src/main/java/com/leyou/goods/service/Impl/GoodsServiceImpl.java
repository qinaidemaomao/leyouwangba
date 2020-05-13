package com.leyou.goods.service.Impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.goods.service.GoodsService;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lizichen
 * @create 2020-04-22 10:52
 */
@Service
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    /**
     * 商品详情页面数据加载
     * @param spuId
     * @return
     */
    public Map<String ,Object> loadData(Long spuId){
        //创建Map装所有数据模型
        Map<String ,Object> model=new HashMap<>();

        Spu spu = this.goodsClient.querySpuById(spuId);
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        List<Long> cids =Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        //处理成为Map
        List<String> names = this.categoryClient.queryNameSByIds(cids);
        //初始化为一个分类Map
        List<Map<String ,Object>> categories =new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String ,Object> map=new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);

        }
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        //规格参数组
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询特殊规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        //处理成为一个map
        Map<Long,String> paramsMap=new HashMap<>();
        params.forEach(param->{
            paramsMap.put(param.getId(),param.getName());
        });
        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramsMap",paramsMap);
           return model;
    }

}
