package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author lizichen
 * @create 2020-03-31 16:51
 */
public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid,@Param("bid") Long bid);

    @Delete("Delete FROM tb_category_brand where brand_id = #{bid}")
    void deleteCategoryAndBrand(Long bid);

    @Update("UPDATE tb_category_brand SET category_id =#{cid} where brand_id = #{bid}")
    void updateCategoryAndBrand(@Param("cid") Long cid,@Param("bid") Long bid);

    @Select("SELECT * FROM tb_brand a JOIN  tb_category_brand b on a.id=b.brand_id WHERE b.category_id =#{cid}")
    List<Brand> queryBrandByCid(Long cid);
}
