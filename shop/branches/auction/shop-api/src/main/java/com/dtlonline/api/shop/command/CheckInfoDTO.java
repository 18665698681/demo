package com.dtlonline.api.shop.command;

import io.alpha.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CheckInfoDTO extends BaseObject {
    private Long id;

    private Integer status;

    @Size(max = 126, message = "审核内容长度不能超过126")
    private String opinion;
}
