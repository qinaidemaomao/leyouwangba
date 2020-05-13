package com.leyou.item.service;

import com.leyou.item.pojo.Brand;
import cpm.leyou.common.pojo.PageResult;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-03-31 16:53
 */
public interface BrandService {

    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

   void saveBrand(Brand brand, List<Long> cids);

   void deleteBrand(Long bid);

   void updateBrand(Brand brand, List<Long> cids);

    List<Brand> queryBrandByCid(Long cid);

    Brand queryBrandById(Long id);
}
