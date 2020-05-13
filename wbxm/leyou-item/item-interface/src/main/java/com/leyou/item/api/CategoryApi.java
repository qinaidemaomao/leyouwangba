package com.leyou.item.api;


import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author lizichen
 * @create 2020-03-31 14:07
 */

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping
    public List<String> queryNameSByIds(@RequestParam("ids")List<Long> ids);



}
