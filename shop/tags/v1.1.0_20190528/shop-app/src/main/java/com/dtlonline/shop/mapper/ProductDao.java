package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.ProductDTO;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.api.shop.command.ShopWithProductQueryPageDTO;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.view.ProductBriefnessDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductDao extends BaseMapper<Product> {

    /**
     * 商品上架修改
     */
    Integer updateRackById(@Param("id") Long id, @Param("status") Integer status, @Param("userId") Long userId);

    /**
     * 商品下架修改
     */
    Integer updateUnRackById(@Param("id") Long id, @Param("status") Integer status, @Param("userId") Long userId);

    /**
     * 商品列表接口
     */
    List<Product> queryProductForList(Page<Product> page, @Param("dto") ProductDTO productDTO);

    /**
     * 首页获取最热商品列表
     */
    List<Product> queryProductIndexList(@Param("pageSize") Integer pageSize,@Param("stringList") List<String> stringList);

    /**
     * 商品详情
     */
    Product queryProductDetail(@Param("productId") Long productId);

    /**
     * 通过商铺id查询商铺分页
     */
    IPage<ProductBriefnessDTO> queryProductsByShopId(@Param("shopPage") ShopWithProductQueryPageDTO shopWithProductQueryPageDTO);

    /**
     * 查询未上架的商品
     */
    IPage<ProductBriefnessDTO> queryItsUnRockedProducts(@Param("shopPage") ShopWithProductQueryPageDTO shopWithProductQueryPageDTO, @Param("userId") Long userId);

    /**
     * 查询已下架的商品
     */
    IPage<ProductBriefnessDTO> queryItsRockedProducts(@Param("shopPage") ShopWithProductQueryPageDTO shopWithProductQueryPageDTO);

    /**
     * 商品修改
     */
    Integer updateProductForId(@Param("productId") Long productId, @Param("p") ProductRecordDTO productRecordDTO, @Param("userId") Long userId);

    @Select("select * from product where txnId = #{txnId} limit 1")
    Product selectByTxnId(String txnId);
}
