package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.ProductLogisticsListDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("product_logistics")
@ApiModel("物流商品扩展表")
public class ProductLogistics {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("业务唯一标识(关联物流商品txnId)")
    private String txnId;

    @ApiModelProperty("运输类型 1-陆运 2-海运 3-空运")
    private Integer logisticsType;

    @ApiModelProperty("运输路线 1-专线物流 2-非装线物流")
    private Integer route;

    @ApiModelProperty("终点")
    private String target;

    @ApiModelProperty("起点")
    private String source;

    @ApiModelProperty("创建日期")
    private LocalDateTime createTime;

    @ApiModelProperty("修改日期")
    private LocalDateTime lastModifyTime;

    public static final ProductLogisticsListDTO of(ProductLogistics logistics){
        ProductLogisticsListDTO logisticsDTO = new ProductLogisticsListDTO();
        Optional.ofNullable(logistics).ifPresent(l ->{
            BeanUtils.copyProperties(logistics,logisticsDTO);
            String source = logistics.getSource();
            String target = logistics.getTarget();
            logisticsDTO.setSource(source.substring(0,source.lastIndexOf("-")))
                    .setTarget(target.substring(0,target.lastIndexOf("-")));
        });
        return logisticsDTO;
    }
}
