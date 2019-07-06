package com.dtlonline.api.shop.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author gaoqiang
 * @Description
 * @Date 17:43 2019/3/7
 **/

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuctionApply {

    @NotNull(message = "加价幅度不能为空")
    @Min(value = 0, message = "错误的加价幅度")
    private BigDecimal minAddPrice;

    @NotNull(message = "交货方式不能为空")
    private Integer deliveryType;

    @NotBlank(message = "交货时间不能为空")
    private String deliveryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime beginTime;

    @NotNull(message = "竞拍持续时长不能为空")
    private Integer hours;

    @NotNull(message = "拍卖方式不能为空")
    private Integer auctionType;

    @Min(value = 0, message = "错误的底价")
    private BigDecimal floorPrice;

}