package com.dtlonline.shop.view;

import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.shop.model.ProductRecord;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecordListDTO extends BaseObject {

    private Long id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 图片
     */
    @JsonProperty("imgPath")
    private String imgs;
    /**
     * 单价 -元
     */
    private BigDecimal unitPrice;
    /**
     * 库存 -吨
     */
    private Integer stock;
    /**
     * 审核状态
     */
    private Integer status;
    /**
     * 审核状态名称
     */
    private String label;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 更新时间
     */
    private Date createTime;
    /**
     * 店铺信息
     */
    private ShopDTO shop;

    public static final ProductRecordListDTO of(ProductRecord productRecord, ShopDTO shop, List<String> imageList){
        ProductRecordListDTO productRecordListDTO = new ProductRecordListDTO();
        Optional.ofNullable(productRecord).ifPresent(record->BeanUtils.copyProperties(record,productRecordListDTO));
        productRecordListDTO.setShop(shop);
        productRecordListDTO.setImgs(imageList.get(0));
        productRecordListDTO.setLabel(Status.getStatusType(productRecord.getStatus()).getMeaning());
        return productRecordListDTO;
    }
}
