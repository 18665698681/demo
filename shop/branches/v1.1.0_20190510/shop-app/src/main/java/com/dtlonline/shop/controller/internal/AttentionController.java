package com.dtlonline.shop.controller.internal;

import com.dtlonline.api.shop.command.AttentionPackDTO;
import com.dtlonline.api.shop.remote.AttentionRemoteService;
import com.dtlonline.shop.service.AttentionService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("internalAttention")
@RequestMapping("/internal")
public class AttentionController extends BaseController implements AttentionRemoteService {

    @Autowired
    private AttentionService attentionService;

    @Override
    @PostMapping("/attentions")
    public Integer saveAttention(@RequestBody List<AttentionPackDTO> attentionList,@RequestParam("userId") Long userId){
        return attentionService.saveAttention(attentionList, userId);
    }
}
