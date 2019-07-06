package com.dtlonline.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.shop.model.ProductStandardRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductStandardRecordDao extends BaseMapper<ProductStandardRecord> {

    @Select("SELECT standardId,standardName,data FROM product_standard_record WHERE txnId = #{stand.productTxnId} AND categoryId = #{stand.categoryId} AND status = 1")
    List<ProductStandardRecord> queryProductStandardList(@Param("stand") ProductStandardRecordDTO productStandardRecordDTO);

    @Select("SELECT id FROM `product_standard_record` where txnId = #{txnId}  and `status` = 1")
    List<Long> selectProductStandardRecordIds(String txnId);
}
