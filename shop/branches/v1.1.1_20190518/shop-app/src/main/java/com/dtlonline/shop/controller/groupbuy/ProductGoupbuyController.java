package com.dtlonline.shop.controller.groupbuy;

import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyQueryRequestDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRequestDTO;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyStatus;
import com.dtlonline.api.shop.constant.groupbuy.ProductGroupbuyType;
import com.dtlonline.shop.constant.groupbuy.OperationType;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import com.dtlonline.shop.service.groupbuy.ProductGroupbuyService;
import com.dtlonline.shop.view.ViewCode;
import io.alpha.app.core.base.BaseController;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.view.RestResult;
import io.alpha.app.jwt.service.JWTService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 团购活动controller
 * @author ken
 * @date 2019-05-20 10:55:50
 */
@RestController("productGoupbuyController")
@RequestMapping("/groupbuy")
public class ProductGoupbuyController extends BaseController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ProductGroupbuyService productGroupbuyService;

    @Value("${intention.interval.months:1}")
    private Integer intentionIntervalMonths;

    @Value("${agreement.interval.days:1}")
    private Integer agreementIntervalDays;

    /**
     * 团购活动状态统计
     */
    @GetMapping(value = "/activity/statistics")
    public RestResult statistics() {

        return RestResult.success(productGroupbuyService.queryGroupbuyStatistics());
    }

    /**
     * 查询团购活动列表
     */
    @GetMapping(value = "/activity")
    public RestResult list(ProductGroupbuyQueryRequestDTO productGroupbuyQueryRequestDTO) {

        return RestResult.success(productGroupbuyService.queryListInPage(productGroupbuyQueryRequestDTO));
    }

    /**
     * 团购活动详情
     */
    @GetMapping(value = "/activity/{id}")
    public RestResult view(@PathVariable Long id) {

        return RestResult.success(productGroupbuyService.queryDetailById(id));
    }

    /**
     * 新增团购活动
     */
    @PostMapping(value = "/activity")
    public RestResult save(@RequestBody @Valid ProductGroupbuyRequestDTO productGroupbuyRequestDTO, @RequestHeader("Authorization") String jwt) {

        logger.info("新增团购活动，请求参数：{}", JsonUtils.toJSON(productGroupbuyRequestDTO));
        Long userId = jwtService.getUserId(jwt);

        ViewCode viewCode = validateRequestParam(productGroupbuyRequestDTO, OperationType.OPERATION_TYPE_ADD);
        if (viewCode != null) {
            return RestResult.failure(viewCode.getCode(), viewCode.getMessage());
        }

        productGroupbuyService.save(productGroupbuyRequestDTO, userId);
        return RestResult.success();
    }

    private ViewCode validateRequestParam(ProductGroupbuyRequestDTO productGroupbuyRequestDTO, int operationType) {

        int year = Integer.parseInt(productGroupbuyRequestDTO.getYear());
        int month = Integer.parseInt(productGroupbuyRequestDTO.getMonth());
        Integer type = productGroupbuyRequestDTO.getType();
        BigDecimal marketPrice = productGroupbuyRequestDTO.getMarketPrice();
        LocalDate buyEndDate = productGroupbuyRequestDTO.getBuyEndDate();
        LocalDate activityEndDate = productGroupbuyRequestDTO.getActivityEndDate();

        LocalDate now = LocalDate.now();

        if (year < now.getYear()) {
            return ViewCode.GROUP_BUY_ADD_YEAR_ERROR;
        }
        if (year == now.getYear() && month < now.getMonthValue()) {
            return ViewCode.GROUP_BUY_ADD_MONTH_ERROR;
        }
        // 意向团购
        if (type == ProductGroupbuyType.INTENT.getCode()) {

            if (marketPrice == null) {
                return ViewCode.GROUP_BUY_ADD_MARKET_PRICE_IS_NULL;
            }
            // 目标团购时间
            LocalDate targetGroupbuyDate = LocalDate.of(year, month, 1);
            // 实际报名截止时间
            String actualBuyEndDate = targetGroupbuyDate.minusMonths(intentionIntervalMonths).format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-15";
            if (!StringUtils.equals(buyEndDate.toString(), actualBuyEndDate)) {
                return ViewCode.GROUP_BUY_ADD_INTENTION_BUY_END_TIME_IS_ERROR;
            }        
        } else if (type == ProductGroupbuyType.AGREEMENT.getCode()) { // 协议团购

            LocalDate groupbuyBuyEndDate = buyEndDate.plusMonths(agreementIntervalDays);
            // 活动报名截止时间必须在团购目标时间的上一个月
            if (groupbuyBuyEndDate.getYear() != year || groupbuyBuyEndDate.getMonthValue() != month) {
                return ViewCode.GROUP_BUY_ADD_AGREEMENT_BUY_END_TIME_IS_ERROR;
            }
        }

        if (!buyEndDate.isBefore(activityEndDate)) {
            return ViewCode.GROUP_BUY_ADD_END_TIME_IS_ERROR;
        }

        // 修改操作验证
        if (OperationType.OPERATION_TYPE_MODIFY == operationType) {

            Long id = productGroupbuyRequestDTO.getId();
            if (id == null) {
                return ViewCode.GROUP_BUY_EDIT_ID_IS_NULL;
            }
            ProductGroupbuy existProductGroupbuy = productGroupbuyService.get(id);
            if (existProductGroupbuy == null) {
                return ViewCode.GROUP_BUY_NOT_EXISTS;
            }

            int status = existProductGroupbuy.getStatus();
            if (ProductGroupbuyStatus.OFF.getCode() == status) {
                return ViewCode.GROUP_BUY_IS_SELL_OFF;
            }
            // 团购活动审核通过后，某些属性将不能被修改
            if (ProductGroupbuyStatus.PAS.getCode() == status) {

                if (productGroupbuyRequestDTO.getCategoryId() != existProductGroupbuy.getCategoryId()) {
                    return ViewCode.GROUP_BUY_EDIT_CATEGORY;
                }
                if (productGroupbuyRequestDTO.getBalanceType() != existProductGroupbuy.getBalanceType()) {
                    return ViewCode.GROUP_BUY_EDIT_BALANCE_TYPE;
                }
                if (productGroupbuyRequestDTO.getMinTrade().doubleValue() != existProductGroupbuy.getMinTrade().doubleValue()) {
                    return ViewCode.GROUP_BUY_EDIT__MINTRADE;
                }
                if (productGroupbuyRequestDTO.getMinDiscountWeight().doubleValue() != existProductGroupbuy.getMinDiscountWeight().doubleValue()) {
                    return ViewCode.GROUP_BUY_EDIT_MIN_DISCOUNT_WEIGHT;
                }
                if (!StringUtils.equals(productGroupbuyRequestDTO.getYear(), existProductGroupbuy.getYear())) {
                    return ViewCode.GROUP_BUY_EDIT_YEAR;
                }
                if (!StringUtils.equals(productGroupbuyRequestDTO.getMonth(), existProductGroupbuy.getMonth())) {
                    return ViewCode.GROUP_BUY_EDIT_MONTH;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        LocalDate now = LocalDate.now();

        System.out.println(now.getYear());
        System.out.println(now.getMonth());
        System.out.println(now.getMonth().getValue());
        System.out.println(now.getMonthValue());
        System.out.println(now.getDayOfYear());

        System.out.println(Integer.parseInt("08"));

        // 目标团购时间
        LocalDate targetGroupbuyDate = LocalDate.of(2019, 5, 1);
        System.out.println(targetGroupbuyDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-15");

    }
}
