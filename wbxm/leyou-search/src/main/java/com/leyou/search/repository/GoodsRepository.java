package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lizichen
 * @create 2020-04-17 17:44
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
