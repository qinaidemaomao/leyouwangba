package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import cpm.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-03-31 16:53
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据Cid查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return  this.brandMapper.queryBrandByCid(cid);
    }

    /**
     * 分页查询
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {

        //初始化example对象
        Example example=new Example(Brand.class);
        Example.Criteria criteria=example.createCriteria();

        //根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);
        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc?"desc":"asc"));
        }


        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageInfo
        //把结果包装成分页结果集
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

          //包装成自己的分页结果集然后 返回
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     *
     * @param brand
     * @param cids
     */
    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //先新增Brand
        this.brandMapper.insertSelective(brand);
        cids.forEach(cid ->{
            this.brandMapper.insertCategoryAndBrand(cid, brand.getId());
        });
    }

    /**
     * 编辑更新数据
     * @param brand
     * @param cids
     */
    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        //先更新Brand
        this.brandMapper.updateByPrimaryKeySelective(brand);
        cids.forEach(cid ->{
            this.brandMapper.updateCategoryAndBrand(cid, brand.getId());
        });
    }

    /**
     * 删除Brand
     * @param bid
     */
    @Override
    @Transactional
    public void deleteBrand(Long bid) {
        //先删除Bran
        this.brandMapper.deleteByPrimaryKey(bid);
        //根据bid删除Category
        this.brandMapper.deleteCategoryAndBrand(bid);
    }

    /**
     * 索引根据ID查找品牌
     * @param id
     * @return
     */
    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
