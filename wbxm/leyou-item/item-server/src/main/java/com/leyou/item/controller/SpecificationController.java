package com.leyou.item.controller;


import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-04 13:53
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 更新分组
     * @param cid
     * @param name
     * @param id
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(
            @RequestParam("cid") Long cid,
            @RequestParam("name")String name,
            @RequestParam("id")Long id
    ){
           SpecGroup specGroup=new SpecGroup();
           specGroup.setId(id);
           specGroup.setCid(cid);
           specGroup.setName(name);
        this.specificationService.updateGroup(specGroup);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id")Long id){
        this.specificationService.deleteGroup(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("group")
    public ResponseEntity<Void> saveGroup(SpecGroup specGroup){
        this.specificationService.saveGroup(specGroup);
        return ResponseEntity.ok().build();
    }

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
       List<SpecGroup> groups=  this.specificationService.queryGroupsByCid(cid);
       if (CollectionUtils.isEmpty(groups)){
           return  ResponseEntity.notFound().build();
       }
       return ResponseEntity.ok(groups);
    }

    /**
     * 查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean searching){
         List<SpecParam> specParams= this.specificationService.queryParams(gid,cid,generic,searching);
         if (CollectionUtils.isEmpty(specParams)){
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(specParams);
    }

    /**
     * 详情页面 根据Cid获取Groups集合
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid")Long cid){
        List<SpecGroup> groups= this.specificationService.queryGroupsWithParam(cid);
        if (CollectionUtils.isEmpty(groups)){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);

    }

    /**
     * 保存
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> saveParams(SpecParam specParam){
        this.specificationService.saveParams(specParam);
        return ResponseEntity.ok().build();
    }
    @PutMapping("param")
    public ResponseEntity<Void> updateParams(SpecParam specParam){
        this.specificationService.updateParams(specParam);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteParams(@PathVariable("id")Long id){
        this.specificationService.deleteParams(id);
        return ResponseEntity.ok().build();
    }
}
