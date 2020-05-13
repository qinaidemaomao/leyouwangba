package com.leyou.goods.service.Impl;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import com.leyou.goods.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author lizichen
 * @create 2020-04-22 17:56
 */
@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

     @Autowired
    private TemplateEngine engine;

     @Autowired
     private GoodsService goodsService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);

    /**
     * 删除静态页面
     * @param id
     */
    @Override
    public void deleteHtml(Long id) {
        File file = new File("G:\\study\\leyou\\tool\\nginx-1.14.0\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }

    /**
     * 创建静态页面
     * @param spuId
     */
    public void createHtml(Long spuId){

         PrintWriter writer = null;
         //初始化运行上下文
         try {
             //把静态页面生成到本地服务器
             Context context = new Context();
             //设置数据模型
             context.setVariables(this.goodsService.loadData(spuId));
             // 创建输出流
             File file = new File("G:\\study\\leyou\\tool\\nginx-1.14.0\\html\\item\\" + spuId + ".html");
             writer = new PrintWriter(file);
             //添加模板
             this.engine.process("item",context,writer);
         } catch (Exception e) {
             LOGGER.error("页面静态化出错：{}，"+ e, spuId);
         } finally {
             if (writer != null) {
                 writer.close();
             }
         }
     }

    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }
}
