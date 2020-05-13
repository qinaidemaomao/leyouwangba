package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-03-31 14:04
 */

public interface CategoryService {

    List<Category> queryCategoriesByPid(Long pid);

    List<Category> queryByBrandId(Long bid);

    List<String> queryNamesByIds(List<Long> ids);

    void saveCategory(Category category);

    void deleteCategoryById(Long cid);

    void updateCategory(Category category);
}
