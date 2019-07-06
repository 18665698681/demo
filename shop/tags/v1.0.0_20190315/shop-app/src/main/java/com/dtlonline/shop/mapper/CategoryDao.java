package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends BaseMapper<Category> {

    /**
     * 品类列表
     * @return
     */
    List<Category> queryCategoryList();

    /**
     * 根据ID查询品类
     */
    Category queryCategorys(@Param("categoryId") Long categoryId);
}
