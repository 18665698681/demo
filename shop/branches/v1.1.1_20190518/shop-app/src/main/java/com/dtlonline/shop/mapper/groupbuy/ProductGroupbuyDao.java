package com.dtlonline.shop.mapper.groupbuy;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyQueryRequestDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyResponseDTO;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductGroupbuyDao extends BaseMapper<ProductGroupbuy> {

    IPage<ProductGroupbuyResponseDTO> queryListInPage(ProductGroupbuyQueryRequestDTO productGroupbuyQueryRequestDTO);

}