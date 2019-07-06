package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.shop.model.Shop;
import com.dtlonline.shop.view.ShopPageInfoDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopDao extends BaseMapper<Shop> {
    /**
     * 根据店铺名字和店铺类型查询店铺分页
     *
     * @param pageQuery the queryShops query
     * @param name      the name
     * @param shopType  the shop type
     * @return the queryShops
     */
    IPage<ShopPageInfoDTO> queryShopsInPageByCondition(Page pageQuery, @Param("name") String name, @Param("shopType") Integer shopType);


    /**
     * 根据UserId查询商铺的状态
     *
     * @param userId the user id
     * @return the integer
     */
    @Select("select status from shop where userId = #{userId} limit 1")
    Integer queryShopStatusByUserId(Long userId);

    /**
     * 根据UserId查询商铺审核单的状态
     *
     * @param userId the user id
     * @return the integer
     */
    @Select("select status from shop_auth_record where userId = #{userId} order by id desc limit 1")
    Integer queryShopAuthRecordsByUserId(Long userId);

    /**
     * 根据商铺id和图片id更新商铺头像id
     *
     * @param shopId   the shop id
     * @param headerId the header id
     */
    @Update("update shop set headerId = #{headerId} where id = #{shopId}")
    void updateShopImageByShopId(@Param("shopId") Long shopId, @Param("headerId") Long headerId);
}
