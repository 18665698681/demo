package com.dtlonline.shop.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.shop.model.ProductCarInfo;
import com.dtlonline.shop.model.store.StoreGoodsRequirePath;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* Created by Mybatis Generator 2019/05/15
*/
@Mapper
public interface StoreGoodsRequirePathMapper extends BaseMapper<StoreGoodsRequirePath> {

    @Select("select * from store_goods_require_path where txnId = #{txnId} limit 1")
    List<StoreGoodsRequirePath> selectByTxnId(String txnId);
}