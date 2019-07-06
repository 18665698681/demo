package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ShopAuthQueryPageDTO;
import com.dtlonline.shop.model.ShopAuthRecord;
import com.dtlonline.shop.view.ShopAuthRecordPageInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShopAuthRecordDao extends BaseMapper<ShopAuthRecord> {

    IPage<ShopAuthRecordPageInfoDTO> queryShopAuthRecordsInPageByCondition(
            ShopAuthQueryPageDTO<ShopAuthRecord> shopAuthQueryPageDTO, @Param("name") String name, @Param("shopType") Integer shopType, @Param("status") Integer status);

    Integer updateShopAuthRecordsById(@Param("staffName") String staffName, @Param("checkInfoDTO") CheckInfoDTO checkInfoDTO);

    @Select("select status from shop_auth_record  where userId=#{userId} ORDER BY id desc LIMIT 1")
    Integer queryLastAuthStatusByUserId(Long userId);
}
