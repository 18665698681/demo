package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.ProductPositionDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("product_position")
public class ProductPosition {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String txnId;

    private String province;

    private String city;

    private String area;

    private String address;

    private String longitude;

    private String latitude;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;

    public final static ProductPositionDTO of(ProductPosition productPosition){
        ProductPositionDTO positionDTO = new ProductPositionDTO();
        Optional.ofNullable(productPosition).ifPresent(position -> BeanUtils.copyProperties(productPosition,positionDTO));
        return positionDTO;
    }
}
