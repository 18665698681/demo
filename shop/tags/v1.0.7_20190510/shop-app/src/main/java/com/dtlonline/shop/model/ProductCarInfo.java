package com.dtlonline.shop.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dtlonline.api.shop.view.ProductCarInfoDTO;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@Accessors
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@TableName("product_car_info")
public class ProductCarInfo {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String txnId;
    /**
     * 车牌号
     */
    private String carPlate;
    /**
     * 起点
     */
    private String startingPoint;
    /**
     * 终点
     */
    private String terminalPoint;
    /**
     * 发货日期
     */
    private LocalDate sendDate;
    /**
     * 到货日期
     */
    private LocalDate arrivalDate;

    /**
     * 司机姓名、身份证、手机号
     */
    private String driverName;
    private String driverIdCard;
    private String driverPhone;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;

    public final static ProductCarInfoDTO of(ProductCarInfo productCarInfo){
        ProductCarInfoDTO productCarInfoDTO = new ProductCarInfoDTO();
        Optional.ofNullable(productCarInfo).ifPresent(car -> BeanUtils.copyProperties(productCarInfo,productCarInfoDTO));
        productCarInfoDTO.setStartingPoint(productCarInfo.getStartingPoint().split(" ")[1]);
        productCarInfoDTO.setTerminalPoint(productCarInfo.getTerminalPoint().split(" ")[1]);
        return productCarInfoDTO;
    }
}
