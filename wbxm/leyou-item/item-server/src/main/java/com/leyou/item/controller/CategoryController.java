package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author lizichen
 * @create 2020-03-31 14:07
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 更新
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCategory(Category category){
        this.categoryService.updateCategory(category);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("cid/{cid}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("cid") Long id){
        this.categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
    /**
     * 添加分类
     * @param category
     * @return
     */
    //添加分类
    @PostMapping
    public ResponseEntity<Void> saveCategory(Category category){
        this.categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 根据父节点的ID查询子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid",defaultValue = "0")Long pid){

        if (pid==null||pid<0){
//                400:参数不合法
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//                  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().build();
        }

        List<Category> categories=this.categoryService.queryCategoriesByPid(pid);
//        判断是否为空
        if (CollectionUtils.isEmpty(categories)){
//                404：资源未找到
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
//            200：查询成功
        return ResponseEntity.ok(categories);


    }

    /**
     * 查询
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid")Long bid){
        List<Category> list=this.categoryService.queryByBrandId(bid);
        if (list==null||list.size()<1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);

    }
    @GetMapping
    public ResponseEntity<List<String>> queryNameSByIds(@RequestParam("ids")List<Long> ids){

        List<String> names = this.categoryService.queryNamesByIds(ids);
        //        判断是否为空
        if (CollectionUtils.isEmpty(names)){
//                404：资源未找到
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
//            200：查询成功
        return ResponseEntity.ok(names);
    }



}
