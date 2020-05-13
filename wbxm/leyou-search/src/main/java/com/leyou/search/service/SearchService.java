package com.leyou.search.service;


import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;


import java.io.IOException;

/**
 * @author lizichen
 * @create 2020-04-15 20:36
 */

public interface SearchService {

    public Goods buildGoods(Spu spu) throws IOException;

    SearchResult search(SearchRequest request);

    /**
     * 保存MQ
     * @param id
     */
    void save(Long id) throws IOException;

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}
