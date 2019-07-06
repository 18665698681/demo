package com.dtlonline.shop.service;

import com.dtlonline.api.shop.command.ProductQuoteDTO;
import com.dtlonline.shop.constant.RedisKey;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.mapper.ProductQuoteDao;
import com.dtlonline.shop.model.ProductQuote;
import com.dtlonline.shop.view.ViewCode;
import io.alpha.app.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ProductQuoteService extends BaseService {

    @Autowired
    private ProductQuoteDao productQuoteDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void saveProductQuote(ProductQuoteDTO productQuoteDTO, Long userId) {
        String key = RedisKey.PRODUCT_QUOTE_KEY.getKey(String.valueOf(userId));
        Long cnt = stringRedisTemplate.opsForValue().increment(key, 1);
        if (cnt == 1) {
            stringRedisTemplate.expire(key,1,TimeUnit.DAYS);
        }
        Optional.ofNullable(cnt).filter(c -> c < 4).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_QUOTE.getCode(), ViewCode.PRODUCT_QUOTE.getMessage()));
        ProductQuote productQuote = new ProductQuote();
        BeanUtils.copyProperties(productQuoteDTO, productQuote);
        productQuote.setUserId(userId);
        productQuoteDao.insert(productQuote);
    }
}
