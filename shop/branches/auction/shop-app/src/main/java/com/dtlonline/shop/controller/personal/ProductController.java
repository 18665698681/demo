package com.dtlonline.shop.controller.personal;

import com.dtlonline.api.shop.command.ProductDTO;
import com.dtlonline.api.shop.command.ProductRecordDTO;
import com.dtlonline.api.shop.command.ShopWithProductQueryPageDTO;
import com.dtlonline.api.shop.constant.ProductTypeEnum;
import com.dtlonline.shop.service.ProductRecordService;
import com.dtlonline.shop.service.ProductService;
import io.alpha.core.base.BaseController;
import io.alpha.core.service.JWTService;
import io.alpha.core.util.JsonUtils;
import io.alpha.core.view.RestResult;
import io.alpha.core.view.ViewCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("personalProductController")
@RequestMapping("/personal")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRecordService productRecordService;

    @Autowired
    private JWTService jwtService;

    /**
     * 现货市场列表
     */
    @PostMapping("/products/list")
    public RestResult queryProductForList(@RequestBody ProductDTO productDTO) {
        return RestResult.success(productService.queryProductForList(productDTO));
    }

    /**
     * 商品详情
     */
    @GetMapping("/products/{productId}")
    public RestResult queryProductDetail(@RequestHeader(name = "Authorization",required = false) String jwt, @PathVariable("productId") Long productId){
        Long userId = 0L;
        if (StringUtils.isNotBlank(jwt)){
            userId = jwtService.getUserId(jwt);
        }
        return RestResult.success(productService.queryProductDetail(productId,userId));
    }

    /**
     *店铺商品列表
     */
    @GetMapping("/product/shop")
    public RestResult queryItsProductsByShopId(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO){
        return RestResult.success(productService.queryProductSimplenessInfo(shopWithProductQueryPageDTO));
    }

    /**
     * 热门推荐
     */
    @GetMapping("/products/index/list")
    public RestResult queryProductIndexList(){
        return RestResult.success(productService.queryProductIndexList());
    }

    /**
     * 商品上架
     */
    @PutMapping("/rack/{id}/active")
    public RestResult rack(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long id) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("用户[{}], 商品上架[{}]",userId,id);
        productRecordService.rack(id,userId);
        return RestResult.success();
    }

    /**
     * 商品下架
     */
    @PutMapping("/rack/{id}")
    public RestResult undoRack(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long id) {
        Long userId = jwtService.getUserId(jwt);
        logger.info("用户[{}], 商品下架 [{}]",userId,id);
        productRecordService.undoRack(id,userId);
        return RestResult.success();
    }

    /**
     * 店铺ID查询商品信息
     */
    @GetMapping("/products/status/switch")
    public RestResult queryItsProductByStatus(@RequestHeader(value = "Authorization",required = false) String jwt, @Valid ShopWithProductQueryPageDTO shopWithProductQueryPageDTO) {
        Long userId = null;
        if (StringUtils.isNotBlank(jwt)){
           userId = jwtService.getUserId(jwt);
        }
        switch (ProductTypeEnum.getProductTypeEnum(shopWithProductQueryPageDTO.getStatusSwitch())){
            case RACK:
                return RestResult.success(productService.queryItsRockedProducts(shopWithProductQueryPageDTO));
            case UN_RACK:
                return RestResult.success(productService.queryItsUnRockedProducts(shopWithProductQueryPageDTO, userId));
            case PENDING:
                return RestResult.success(productRecordService.queryItsRecordsExceptPassProducts(shopWithProductQueryPageDTO, userId));
            default:
                return RestResult.failure(ViewCode.FAILURE.getCode(), ViewCode.FAILURE.getMessage());
        }
    }

    /**
     * 编辑商品
     */
    @PutMapping(value = "/products/{productId}",headers = "Authorization")
    public RestResult updateProductForId(@PathVariable("productId") Long productId,@RequestBody ProductRecordDTO productRecordDTO,@RequestHeader(name = "Authorization") String jwt){
        Long userId = jwtService.getUserId(jwt);
        logger.info("商品[{}],编辑[{}]",productId, JsonUtils.toJSON(productRecordDTO));
        productService.updateProductForId(productId,productRecordDTO,userId);
        productRecordService.rack(productId,userId);
        return RestResult.success();
    }
}
