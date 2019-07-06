package com.dtlonline.api.shop.command;

import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StandardRecordDTO extends BaseObject {

    /**
     * 规格ID
     */
    private Long standardId;
    /**
     * 规格数据
     */
    private String data;
}
