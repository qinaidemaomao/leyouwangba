package com.leyou.item.controller;

import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
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
 * @create 2020-04-05 0:08
 */
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 商品上下架
     * @param id
     * @return
     */
    @PutMapping("goods/spu/out/{id}")
    public ResponseEntity<Void> goodsSoldOut(@PathVariable("id") Long id){
        if ( id==null||id<=0){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.goodsService.goodsSoldOut(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 逻辑删除商品
     * @param id
     * @return
     */
    @PostMapping("goods/spu/{id}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("id") String id){
        if ( "".equals(id.trim())||Long.parseLong(id)<=0){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        this.goodsService.deleteGoods(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据分页查询数据
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue ="1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows
    ) {
       PageResult<SpuBo> result= this.goodsService.querySpuByPage(key,saleable,page,rows);
        if (result ==null|| CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增 保存商品信息
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     *根据spuID查询spuDetail查找数据
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail=this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail ==null){
           return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 查找数到修改表单
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id")Long spuId){
        List<Sku> skus= this.goodsService.querySkusBySouId(spuId);
        if (CollectionUtils.isEmpty(skus)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 编辑修改后 保存
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
       this.goodsService.updateGoods(spuBo);
       //更新和删除成功 相应204
        return ResponseEntity.noContent().build();
    }

    /**
     * 商品详情页面
     */
    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById (@PathVariable ("id")Long id){
        Spu spu=this.goodsService.querySpuById(id);
        if (spu ==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    /**
     * 登录状态添加购物车方法
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public  ResponseEntity<Sku> queryBySkuId(@PathVariable("skuId")Long skuId){
      Sku sku=  this.goodsService.queryBySkuId(skuId);
      if (sku==null){
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(sku);
    }
}
