package com.dtlonline.shop.view;

import com.dtlonline.shop.model.Attention;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

@Builder
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AttentionDTO {

    private Long id;

    private Long targetId;

    private Integer type;

    public final static AttentionDTO of(Attention attention){
        AttentionDTO attentionDTO = new AttentionDTO();
        Optional.ofNullable(attention).ifPresent(att -> BeanUtils.copyProperties(attention,attentionDTO));
        return attentionDTO;
    }
}
