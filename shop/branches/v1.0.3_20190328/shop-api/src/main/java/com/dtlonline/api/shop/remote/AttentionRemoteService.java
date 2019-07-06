package com.dtlonline.api.shop.remote;

import com.dtlonline.api.shop.command.AttentionPackDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "${apps.shop.name:shop-app}",contextId = "AttentionRemoteService")
public interface AttentionRemoteService {

    /**
     * 关注
     */
    @PostMapping("/internal/attentions")
    Integer saveAttention(@RequestBody List<AttentionPackDTO> attentionList,@RequestParam("userId") Long userId);

}
