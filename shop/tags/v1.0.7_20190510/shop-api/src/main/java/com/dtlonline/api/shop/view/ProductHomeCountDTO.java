package com.dtlonline.api.shop.view;


import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductHomeCountDTO {
    /**
     * 全部
     */
    private Integer whole;
    /**
     * 在途
     */
    private Integer theWay;
    /**
     * 在库
     */
    private Integer theStock;
}
