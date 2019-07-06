package com.dtlonline.shop.view;

import com.dtlonline.api.isp.command.ImageDTO;
import lombok.Data;

/**
 * The type Shop with header dto.
 *
 * @author Deveik
 * @date 2019 /01/25
 */
@Data
public class ShopWithHeaderDTO {
    private ImageDTO headerImage;
    private Long shopId;
}
