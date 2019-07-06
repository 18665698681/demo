package com.dtlonline.shop.service.auction;

import com.dtlonline.api.shop.command.AuctionApply;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.shop.constant.auction.AuctionAuditStatus;
import com.dtlonline.shop.mapper.auction.ProductAuctionDao;
import com.dtlonline.shop.mapper.auction.ProductAuctionUserDao;
import com.dtlonline.shop.model.auction.ProductAuction;
import com.dtlonline.shop.model.ProductRecord;
import com.dtlonline.shop.service.ProductRecordService;
import io.alpha.core.base.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionTradeService extends BaseService{

    @Autowired
    ProductRecordService productRecordService;

    @Autowired
    ProductAuctionUserDao productAuctionUserDao;
    @Autowired
    ProductAuctionDao productAuctionDao;

}
