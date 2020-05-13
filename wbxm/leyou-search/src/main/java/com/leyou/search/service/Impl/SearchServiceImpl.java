package com.leyou.search.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lizichen
 * @create 2020-04-15 20:38
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SpecificationClient specificationClient;

    private static final ObjectMapper MAPPER=new ObjectMapper();

    /**
     * 删除MQ
     * @param id
     */
    @Override
    public void delete(Long id) {
   this.goodsRepository.deleteById(id);
    }

    /**
     * 保存MQ
     * @param id
     */
    @Override
    public void save(Long id) throws IOException {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }

    /**
     * 把Spu转化为Goods对象
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {
       Goods goods=new Goods();
       //根据分类查询分类名称
        List<String> names = this.categoryClient.queryNameSByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

       //根据品牌ID查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());
      //根据sku的ID查询所有的价格
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spu.getId());
       //初始化一个价格集合
        List<Long> prices=new ArrayList<>();
        //收集kus的必要字段信息
        List<Map<String,Object>> skuMapList=new ArrayList<>();

        skus.forEach(sku ->{
            prices.add(sku.getPrice());

            Map<String,Object> map=new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //获取sku中的图片，数据库的图片可能是多张，多张以“，”进行分割 所以也以逗号来切割图片数组，获取第一张图片
            map.put("image",StringUtils.isBlank(sku.getImages()) ? "":StringUtils.split(sku.getImages(), ",")[0]);
            skuMapList.add(map);
    });

        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), null, true);
        //根据spuid 查询spuDetail
        SpuDetail spuDetail=this.goodsClient.querySpuDetailBySpuId(spu.getId());
        Map<String ,Object> genericSpec = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        //把特殊的规格参数值，进行反序列化
        Map<String ,List<Object>> specMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String ,List<Object>>>() {
        });

        Map<String,Object> spec=new HashMap<>();
        params.forEach(param -> {
            //判断规格参数的类型，是否是通的规格参数
            if (param.getGeneric()){
                //如果是通用类型的参数
                String value = genericSpec.get(param.getId().toString()).toString();
               //如果是数字 返回区间
                if (param.getNumeric()){
                     value = chooseSegment(value, param);
                }
                spec.put(param.getName(),value);
            }else {

                //特殊规格参数
                List<Object> value = specMap.get(param.getId().toString());
                spec.put(param.getName(),value);
            }
        });
        goods.setId(spu.getId());
       goods.setCid1(spu.getCid1());
       goods.setCid2(spu.getCid2());
       goods.setCid3(spu.getCid3());
       goods.setBrandId(spu.getBrandId());
       goods.setCreateTime(spu.getCreateTime());
       goods.setSubTitle(spu.getSubTitle());
       //拼接All字段需要分类名称以及品牌名称
       goods.setAll(spu.getTitle()+" "+ StringUtils.join(names,"") +" "+brand.getName());
        //获得所有sku下的价格
        goods.setPrice(prices);
        //获得spu下的所有sku并转化成Json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        //获得所有的规格参数  作为可选项的值
        goods.setSpecs(spec);
     return goods;
    }

    /**
     * 返回特殊字段区间
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    /**
     * 接收搜索服务
     * @param request
     * @return
     */
    @Override
    public SearchResult search(SearchRequest request) {

        if(StringUtils.isBlank(request.getKey())){
            return null;
        }
        //自定义查询构造器
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //添加查询条件
        //QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        BoolQueryBuilder basicQuery= buildBoolQueryBuilder(request);
        queryBuilder.withQuery(basicQuery);
        //添加分页
        queryBuilder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
        //添加结果集过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"}, null));

        //添加分类和品牌的聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //执行查询，获取结果
        //不仅有普通结果集 还有 聚合结果集
        AggregatedPage<Goods> goodsPage =(AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());
        //获取聚合结果 并解析    需要什么解析成什么
        List<Map<String,Object>> categories =getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
        List<Brand> brands =getBrandsAggResult(goodsPage.getAggregation(brandAggName));
        //判断 只有一个分类时才做规格参数的聚合
        List<Map<String ,Object>> specs= null;
        if (!CollectionUtils.isEmpty(categories) && categories.size() ==1){
            //对规格参数进行聚合
            specs= getParamAggResult((Long)categories.get(0).get("id"),basicQuery);
        }
        return new SearchResult(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent(),categories,brands,specs);
    }

    /**
     * 构建布尔查询
     * @param request
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
         boolQueryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()).operator(Operator.AND));
         //添加过滤条件
        //获取用户选择的过滤信息
        Map<String ,Object> filter =request.getFilter();

        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key=entry.getKey();
            if (StringUtils.equals("品牌",key)){
                key="brandId";
            }else if (StringUtils.equals("分类",key)){
                key="cid3";
            }else {
                key="specs."+key+".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return boolQueryBuilder;

    }

    /**
     * 根据查询条件
     * 聚合规格参数聚合
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder basicQuery) {
        //自定义查询对象构建
        NativeSearchQueryBuilder queryBuilder=new NativeSearchQueryBuilder();
        //添加基本查询条件
        queryBuilder.withQuery(basicQuery);
        //查询要聚合的规格参数
        List<SpecParam> specParams = this.specificationClient.queryParams(null, cid, null, true);
        
        //添加规格参数的聚合
        specParams.forEach( specParam -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
        });
        //添加结果集过滤
         queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        //执行聚合查询
        AggregatedPage<Goods> goodsPage =(AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());
       //
        List<Map<String, Object>> spec=new ArrayList<>();
        //解析结果集 返回   key聚合名称（规格参数名） value（聚合对象）
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            //初始化一个map， k 规格参数名 options 规格参数值
            Map<String, Object> map=new HashMap<>();
            map.put("k",entry.getKey());
            //初始化一个options集合，获取桶中的Key
            List<String> options=new ArrayList<>();
            //解析聚合 获取桶
            StringTerms terms = (StringTerms) entry.getValue();
            //获取桶集合
            terms.getBuckets().forEach(bucket -> {
              options.add(bucket.getKeyAsString());
            });
            map.put("options",options);
            spec.add(map);
        }
        return spec;
    }

    /**
     * 解析品牌聚合结果集
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandsAggResult(Aggregation aggregation) {
        //强转为Long
        LongTerms terms = (LongTerms) aggregation;
        //获取聚合中的桶
         return terms.getBuckets().stream().map( bucket -> {
            //获取集合中的桶
            return this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());

    }

    /**
     * 解析分类聚合结果集
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
            LongTerms terms = (LongTerms) aggregation;
            //获取分类集合
            return terms.getBuckets().stream().map(bucket -> {
              //初始化Map
                Map<String, Object> map=new HashMap<>();
                //获取桶中分类ID
                long id = bucket.getKeyAsNumber().longValue();
                //根据分类Id查询分类名称
                List<String> names = this.categoryClient.queryNameSByIds(Arrays.asList(id));
                map.put("id",id);
                map.put("name", names.get(0));
                return map;
        }).collect(Collectors.toList());
        }
}
