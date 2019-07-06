package com.dtlonline.shop.service;

import com.dtlonline.api.shop.command.ProductQuoteDTO;
import com.dtlonline.shop.constant.RedisKey;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.mapper.ProductQuoteDao;
import com.dtlonline.shop.model.ProductQuote;
import com.dtlonline.shop.view.ViewCode;
import io.alpha.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class ProductQuoteService extends BaseService {

    @Autowired
    private ProductQuoteDao productQuoteDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void saveProductQuote(ProductQuoteDTO productQuoteDTO,Long userId){
        Long cnt = stringRedisTemplate.opsForValue().increment(RedisKey.PRODUCT_QUOTE_KEY.getKey(String.valueOf(userId)), 1);
        if(cnt ==1){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 24);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.MILLISECOND, 0);
            stringRedisTemplate.expireAt(RedisKey.PRODUCT_QUOTE_KEY.getKey(String.valueOf(userId)),cal.getTime());
        }
        Optional.ofNullable(cnt).filter(c ->c < 4).orElseThrow(() ->new ProductException(ViewCode.PRODUCT_QUOTE.getCode(),ViewCode.PRODUCT_QUOTE.getMessage()));
        // TODO: 2019/3/15 推荐用复制对象ObjectUtils.transfer(xxxList,XxxDTO.class);
        ProductQuote productQuote = new ProductQuote();
        BeanUtils.copyProperties(productQuoteDTO,productQuote);
        productQuote.setUserId(userId);
        productQuoteDao.insert(productQuote);
    }
}
