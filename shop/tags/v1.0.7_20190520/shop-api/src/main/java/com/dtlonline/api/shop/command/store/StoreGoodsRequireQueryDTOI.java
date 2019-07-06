package com.dtlonline.api.shop.command.store;

import com.dtlonline.api.isp.command.ImageDTO;
import com.dtlonline.api.shop.command.ProductStandardRecordDTO;
import com.dtlonline.api.shop.command.StandardRecordDTO;
import com.dtlonline.api.shop.view.ProductCarInfoDTO;
import io.alpha.app.core.base.BasePageObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* Created by Mybatis Generator 2019/04/17
*/
@ApiModel(value="com.dtlonline.backend.model.store.StoreGoodsRequireDTO")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class StoreGoodsRequireQueryDTOI extends BasePageObject {

    private Long id;

    @ApiModelProperty("类型筛选：5供应类型，2其他类型")
    private Integer type;

    @ApiModelProperty("状态筛选：1通过，2其他状态")
    private Integer auditStatus;

    @ApiModelProperty("用户筛选")
    private Long userId;


    @ApiModelProperty("类别筛选（新）")
    private String categoryName;

    @ApiModelProperty("规格筛选（新）")
    List<StandardRecordDTO> standardList;

    @ApiModelProperty("是否隐藏到期的数据")
    private Boolean hideOffSale;

}