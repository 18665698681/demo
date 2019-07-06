package com.dtlonline.shop.controller.system;

import com.dtlonline.shop.service.StandardService;
import io.alpha.app.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("systemStandardController")
@RequestMapping("/system")
public class StandardController extends BaseController {

    @Autowired
    private StandardService standardService;

}
