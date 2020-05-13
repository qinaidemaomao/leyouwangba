package com.leyou.item.api;

import com.leyou.item.pojo.Brand;

import org.springframework.web.bind.annotation.*;


/**
 * @author lizichen
 * @create 2020-03-31 16:52
 */
@RequestMapping("brand")
public interface BrandApi {


    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id);

}
