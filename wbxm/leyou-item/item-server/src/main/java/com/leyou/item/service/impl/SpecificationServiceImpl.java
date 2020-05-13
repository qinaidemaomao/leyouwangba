package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-04-04 13:52
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

       @Autowired
       private SpecGroupMapper specGroupMapper;
       @Autowired
       private SpecParamMapper specParamMapper;

    /**
     * 商品详情页面 根据cid查询组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        groups.forEach(group -> {
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
        return groups;
    }

    /**
     * 更新分组
     * @param specGroup
     */
    @Override
    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    /**
     * 删除分组
     * @param id
     */
    @Override
    @Transactional
    public void deleteGroup(Long id) {
        //删除分组所有规格
          SpecParam specParam=new SpecParam();
          specParam.setGroupId(id);
          this.specParamMapper.delete(specParam);
        //删除分组
        SpecGroup specGroup=new SpecGroup();
        specGroup.setId(id);
        this.specGroupMapper.delete(specGroup);

    }

    /**
     * 增加Group
     * @param specGroup
     */
    @Override
    public void saveGroup(SpecGroup specGroup) {
        this.specGroupMapper.insertSelective(specGroup);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deleteParams(Long id) {
        SpecParam specParam=new SpecParam();
        specParam.setId(id);
        this.specParamMapper.delete(specParam);
    }

    /**
     * 更新
     * @param specParam
     */
    @Override
    public void updateParams(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 添加规格参数
     * @param specParam
     */
    @Override
    public void saveParams(SpecParam specParam) {
        this.specParamMapper.insertSelective(specParam);
    }

    /**
     * 查询规格参数
     * @param gid
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid,Long cid,Boolean generic,Boolean searching) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
       SpecGroup specGroup=new SpecGroup();
       specGroup.setCid(cid);
       return this.specGroupMapper.select(specGroup);
    }
}
