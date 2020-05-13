package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import com.leyou.goods.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author lizichen
 * @create 2020-04-22 9:40
 */

@Controller
public class GoodsController {

    @Autowired
    private GoodsHtmlService goodsHtmlService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 页面静态化
     * @param id
     * @param model
     * @return
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable ("id") Long id, Model model){
        Map<String, Object> map = this.goodsService.loadData(id);
        model.addAllAttributes(map);
        // 页面静态化
        this.goodsHtmlService.asyncExcute(id);
        return "item";
    }

}
