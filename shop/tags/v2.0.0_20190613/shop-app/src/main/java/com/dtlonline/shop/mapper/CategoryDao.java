package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.api.shop.command.CategoryParamDTO;
import com.dtlonline.shop.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {

    List<Category> queryCategoryList();

    Category queryCategorys(@Param("categoryId") Long categoryId);

    @Update("<script>" +
            " UPDATE category " +
            "<set>" +
            "<if test='p.title!=null'>" +
            " title = #{p.title}," +
            "</if>" +
            "<if test='p.parentId!=null'>" +
            " parentId = #{p.parentId}," +
            "</if>" +
            "<if test='p.status!=null'>" +
            " `status` = #{p.status}," +
            "</if>" +
            "<if test='p.index!=null'>" +
            " `index` = #{p.index}," +
            "</if>" +
            "<if test='p.type!=null'>" +
            " `type` = #{p.type}," +
            "</if>" +
            "<if test='p.active!=null'>" +
            " `active` = #{p.active}," +
            "</if>" +
            "</set>" +
            " WHERE id = #{id}" +
            "</script>")
    Integer updateCategoryForId(@Param("id") Long id, @Param("p") CategoryParamDTO categoryParamDTO);
}
