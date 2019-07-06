package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.ProductRecommend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductRecommendDao extends BaseMapper<ProductRecommend> {

    @Select("select * from product_recommend where status=1 and type = #{type} order by weights desc limit #{size} ")
    List<ProductRecommend> queryProductIndexList(@Param("type") Integer type,@Param("size") int size);
}
