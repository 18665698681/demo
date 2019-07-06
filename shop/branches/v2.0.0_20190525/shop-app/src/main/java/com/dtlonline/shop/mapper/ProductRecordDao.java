package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ShopWithProductQueryPageDTO;
import com.dtlonline.shop.model.ProductRecord;
import com.dtlonline.shop.view.ProductBriefnessDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductRecordDao extends BaseMapper<ProductRecord> {

    /**
     * 商品审核列表
     *
     * @param page      the page
     * @param condition the condition
     * @return list list
     */
    List<ProductRecord> productRecordList(Page<ProductRecord> page, @Param("condition") Map<String, Object> condition);

    /**
     * 审核商品状态
     *
     * @param recordName   the record name
     * @param checkInfoDTO the check info dto
     * @return integer integer
     */
    Integer updateProductByStatus(@Param("recordName") String recordName, @Param("checkInfo") CheckInfoDTO checkInfoDTO);

    /**
     * Query products by shop id page.
     *
     * @param shopWithProductQueryPageDTO the shop with product query page dto
     * @return the page
     */
    IPage<ProductBriefnessDTO> queryProductRecordsByShopId(@Param("shopPage") ShopWithProductQueryPageDTO shopWithProductQueryPageDTO);

    /**
     * Query its records except pass products page.
     *
     * @param shopWithProductQueryPageDTO the shop with product query page dto
     * @param userId                      the user id
     * @return the page
     */
    IPage<ProductBriefnessDTO> queryItsRecordsExceptPassProducts(@Param("shopPage") ShopWithProductQueryPageDTO shopWithProductQueryPageDTO, @Param("userId") Long userId);
}
