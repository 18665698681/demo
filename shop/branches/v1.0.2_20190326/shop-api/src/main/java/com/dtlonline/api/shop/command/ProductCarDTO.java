package com.dtlonline.api.shop.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductCarDTO {

    @NotBlank(message = "业务唯一标识不能为空")
    private String txnId;
    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空")
    private String carPlate;
    /**
     * 起点
     */
    @NotBlank(message = "起点不能为空")
    private String startingPoint;
    /**
     * 终点
     */
    @NotBlank(message = "终点不能为空")
    private String terminalPoint;
    /**
     * 发货日期
     */
    @NotNull(message = "发货日期不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate sendDate;
    /**
     * 到货日期
     */
    @NotNull(message = "到货日期不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate arrivalDate;
}
