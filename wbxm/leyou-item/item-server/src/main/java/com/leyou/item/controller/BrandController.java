package com.leyou.item.controller;

import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import com.netflix.ribbon.proxy.annotation.Http;
import cpm.leyou.common.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-03-31 16:52
 */
@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;


    /**
     * 根据CID查询商品品牌
     * @param cid
     * @return
     */
     @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable(value = "cid")Long cid){
       List<Brand> brands= this.brandService.queryBrandByCid(cid);
        if ( CollectionUtils.isEmpty(brands)){
            return  ResponseEntity.notFound().build();
        }
       return ResponseEntity.ok(brands);
    }
    /**
     * 分页查询条件分页 并排序
     * */
    @GetMapping("page") //key=&page=1&rows=5&sortBy=id&desc=
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
          @RequestParam(value = "key",required = false)String key,
          @RequestParam(value = "page",defaultValue = "1")Integer page,
          @RequestParam(value = "rows",defaultValue = "5")Integer rows,
          @RequestParam(value = "sortBy",required = false)String sortBy,
          @RequestParam(value = "desc",required = false)Boolean desc
    ){
      PageResult<Brand>  result=  this.brandService.queryBrandsByPage(key,page,rows,sortBy,desc);
      if ( CollectionUtils.isEmpty(result.getItems())){
          return  ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(result);
    }

    /**
     * 更新
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 保存
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除
     * @param bid
     * @return
     */
    @PostMapping("delete")
    public ResponseEntity<Void> deleteBrand(@RequestParam("bid")Long bid){
        this.brandService.deleteBrand(bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 索引接口 根据Id查找品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        Brand brand=this.brandService.queryBrandById(id);
        if (brand==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}
