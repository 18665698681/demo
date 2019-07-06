package com.dtlonline.shop.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.store.StoreGoodsRequire;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Mybatis Generator 2019/04/17
*/
@Mapper
public interface StoreGoodsRequireMapper extends BaseMapper<StoreGoodsRequire> {

    @Update("UPDATE store_goods_require SET validQuantity = validQuantity+#{calculateQuantity} WHERE id = #{storeGoodsRequireId} AND validQuantity = #{oldValidQuantity}")
    Integer calculateValidQuantity(@Param("storeGoodsRequireId") Long storeGoodsRequireId, @Param("calculateQuantity") Integer calculateQuantity, @Param("oldValidQuantity") Integer oldValidQuantity);

}