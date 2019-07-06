package com.dtlonline.api.shop.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductRecordParamDTO {
    /**
     * 业务唯一标识
     */
    @NotBlank(message = "商品唯一标识不能为空")
    private String txnId;
    /**
     * 商品标题
     */
    @NotBlank(message = "商品标题为必填")
    @Size(max = 200,message = "商品标题不能超过200字")
    private String title;
    /**
     * 1-现货 2-物流 3-仓储 4-竞拍 99-采购
     */
    @NotNull(message = "发布类型不能为空")
    private Integer type;
    /**
     * 买卖类型 1-要买 2-要换 3-要借
     */
    @NotNull(message = "买卖类型不能为空")
    private Integer tradingType;
    /**
     * 品类ID
     */
    @NotNull(message = "品类ID不能为空")
    private Long categoryId;
    /**
     * 品类名称
     */
    @NotBlank(message = "品类名称不能为空")
    private String categoryName;
    /**
     * 店铺ID
     */
    @NotNull(message = "店铺ID不能为空")
    private Long shopId;
    /**
     * 单价
     */
    @Min(value = 0,message = "单价不能为0")
    private BigDecimal unitPrice;
    /**
     * 库存
     */
    @NotNull(message = "库存不能为空")
    @Min(value = 0,message = "库存不能为0")
    private Integer stock;
    /**
     * 最小交易量
     */
    @NotNull(message = "最小交易量不能为空")
    @Min(value = 0,message = "最小交易量不能为0")
    private Integer minDeal;
    /**
     * 描述
     */
    @JsonProperty("descriptions")
    private String description;
    /**
     * 是否开发票 1-开票 2-不开票
     */
    @NotNull(message = "是否开发票不能为空")
    private Integer invoice;
    /**
     * 有效时间
     */
    @NotNull(message = "截止有效时间不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate expireTime;
    /**
     * 规格集合参数
     */
    @NotEmpty(message = "规格集合参数不能为空")
    List<ProductStandardRecordDTO> standardList = new ArrayList<>();
    /**
     * 1-在库 2-在途 -1-无效
     */
    private Integer theWay;
    /**
     * 车辆信息
     */
    private ProductCarDTO productCar = new ProductCarDTO();
    /**
     * 地址
     */
    private ProductPositionParamDTO position = new ProductPositionParamDTO();
}
