package com.dtlonline.shop.service;

import com.dtlonline.api.shop.command.FinanceDTO;
import com.dtlonline.shop.mapper.FinanceDao;
import com.dtlonline.shop.model.Finance;
import io.alpha.app.core.base.BaseService;
import io.alpha.security.aes.AESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceService extends BaseService {

    @Autowired
    private FinanceDao financeDao;

    public void saveFinance(FinanceDTO financeDTO) {
        Finance finance = new Finance();
        BeanUtils.copyProperties(financeDTO, finance);
        finance.setMobile(AESUtil.encrypt(financeDTO.getMobile()));
        financeDao.insert(finance);
    }
}
