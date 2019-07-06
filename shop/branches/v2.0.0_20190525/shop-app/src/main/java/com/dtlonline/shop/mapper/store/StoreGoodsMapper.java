package com.dtlonline.shop.mapper.store;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.view.store.StoreGoodsDTO;
import com.dtlonline.api.shop.view.store.StoreGoodsMergeQuantityDTO;
import com.dtlonline.shop.model.store.StoreGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* Created by Mybatis Generator 2019/04/17
*/
@Mapper
public interface StoreGoodsMapper extends BaseMapper<StoreGoods> {

    Page<StoreGoodsMergeQuantityDTO> selectPassListByStandard(Page<StoreGoodsMergeQuantityDTO> page, @Param("userId") Long userId);

    @Update("UPDATE store_goods SET validQuantity = validQuantity+#{calculateQuantity} WHERE id = #{storeGoodsId} AND validQuantity = #{oldValidQuantity}")
    Integer calculateValidQuantity(@Param("storeGoodsId") Long storeGoodsId,@Param("calculateQuantity") Integer calculateQuantity,@Param("oldValidQuantity") Integer oldValidQuantity);

    @Update("UPDATE store_goods SET status = 2 WHERE id = #{storeGoodsId} and status = 3")
    Integer disableStoreGoods(@Param("storeGoodsId") Long storeGoodsId);

    @Update("UPDATE store_goods SET status = 1 WHERE id = #{storeGoodsId} and status = 3")
    Integer enableStoreGoods(@Param("storeGoodsId")Long storeGoodsId);
}