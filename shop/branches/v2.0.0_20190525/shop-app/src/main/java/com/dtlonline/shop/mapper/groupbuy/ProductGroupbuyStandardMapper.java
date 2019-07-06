package com.dtlonline.shop.mapper.groupbuy;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
* Created by Mybatis Generator 2019/05/27
*/
@Mapper
public interface ProductGroupbuyStandardMapper extends BaseMapper<ProductGroupbuyStandard> {

    @Select("<script>"
            + "SELECT productId,MAX(priceScale) priceScale FROM "
            + "product_groupbuy_standard "
            + "GROUP BY productId HAVING productId in "
            + "<foreach collection='list' item='item' separator=',' open='(' close=')'>"
            + " #{item}"
            + "</foreach>"
            + "</script>")
    List<ProductGroupbuyStandard> selectMaxScale(@Param("list") Set<Long> productIds);

    @Select("select * from product_groupbuy_standard  where productId = #{productId} ORDER BY priceScale desc LIMIT 1")
    ProductGroupbuyStandard selectMaxScaleByProductId(@Param("productId") Long productId);
}