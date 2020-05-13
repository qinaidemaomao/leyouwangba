package com.leyou.goods.service;

/**
 * @author lizichen
 * @create 2020-04-22 17:55
 */
public interface GoodsHtmlService {
    /**
     * 页面静态化
     * @param spuId
     */
    public void createHtml(Long spuId);
    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId);

    /**
     * 删除页面
     * @param id
     */
    void deleteHtml(Long id);
}
