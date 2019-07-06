package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.*;
import com.dtlonline.api.shop.constant.ProductLogisticsTypeEnum;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.api.shop.view.ShopDTO;
import com.dtlonline.api.shop.view.*;
import com.dtlonline.api.shop.view.geo.Results;
import com.dtlonline.api.user.command.system.SellerPrivilegeDTO;
import com.dtlonline.api.user.remote.UserAuthRemoteService;
import com.dtlonline.api.user.remote.UserInfosRemoteService;
import com.dtlonline.api.user.remote.UserPrivilegeRemoteService;
import com.dtlonline.api.user.view.UserAuthDetailDTO;
import com.dtlonline.api.user.view.UserDTO;
import com.dtlonline.shop.constant.RedisKey;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.ProductRecommendDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.model.*;
import com.dtlonline.shop.service.auction.AuctionQueryService;
import com.dtlonline.shop.view.*;
import com.dtlonline.shop.view.auction.ProductAuctionDTO;
import io.alpha.app.core.base.BaseService;
import io.alpha.app.core.util.JsonUtils;
import io.alpha.app.core.util.ObjectUtils;
import io.alpha.app.core.view.RestResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService extends BaseService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductStandardRecordDao productStandardRecordDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductRecommendDao productRecommendDao;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductCarInfoService productCarInfoService;

    @Autowired
    private ProductPositionService productPositionService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private UserAuthRemoteService userAuthRemoteService;

    @Autowired
    private UserInfosRemoteService userInfosRemoteService;

    @Autowired
    private UserPrivilegeRemoteService userPrivilegeRemoteService;

    @Autowired
    private ProductStandardRecordService productStandardRecordService;

    @Autowired
    private AuctionQueryService auctionQueryService;

    @Autowired
    private AttentionService attentionService;

    @Autowired
    private ProductLogisticsService productLogisticsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${home.page.size:4}")
    private Integer pageSize;

    @Value("${geo.limit:30}")
    private Integer geoLimit;

    public Page queryProductForList(ProductDTO productDTO) {
        if (productDTO.getCategoryId() != null && CollectionUtils.isNotEmpty(productDTO.getStandardList())) {
            Page txnIdPage = new Page(1, 100);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("categoryId", productDTO.getCategoryId());
            wrapper.in("standardId", productDTO.getStandardList().stream().map(StandardRecordDTO::getStandardId).collect(Collectors.toSet()));
            wrapper.orderByDesc("txnId");
            IPage<ProductStandardRecord> page = productStandardRecordDao.selectPage(txnIdPage, wrapper);
            Map<String, Set<ProductStandardRecord>> standardMap = page.getRecords().stream().collect(Collectors.groupingBy(ProductStandardRecord::getTxnId, Collectors.toSet()));
            Set<String> txnIdSet = new HashSet<>();
            Set<String> sourceSet = productDTO.getStandardList().stream().map(s -> s.getStandardId() + ":" + s.getData()).collect(Collectors.toSet());
            standardMap.keySet().stream().filter(key -> StringUtils.isNotBlank(key)).forEach(txnId -> {
                Set<ProductStandardRecord> set = standardMap.get(txnId);
                Set<String> targetSet = set.stream().map(d -> d.getStandardId() + ":" + d.getData()).collect(Collectors.toSet());
                if (targetSet.containsAll(sourceSet)) {
                    txnIdSet.add(txnId);
                }
            });
            productDTO.setTxnIdList(txnIdSet);
            if (txnIdSet.isEmpty()) {
                return new Page();
            }
        }
        if (StringUtils.isNotBlank(productDTO.getKeywords())) {
            productDTO.setKeywords("%" + productDTO.getKeywords() + "%");
        }
        if (productDTO.getCategoryId() != null && productDTO.getStandardList().isEmpty()) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id", productDTO.getCategoryId());
            Category category = categoryDao.selectOne(wrapper);
            if (0 == category.getParentId() && TypeEnum.PRODUCT_SPOT.getValue().equals(productDTO.getType())) {
                Set<Long> parentSet = new HashSet<>();
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parentId", category.getId());
                List<Category> parentIdList = categoryDao.selectList(queryWrapper);
                Set<Long> pSet = parentIdList.stream().map(p -> p.getId()).collect(Collectors.toSet());
                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.in("parentId", pSet);
                List<Category> parentIdList2 = categoryDao.selectList(queryWrapper2);
                Set<Long> pSet2 = parentIdList2.stream().map(p -> p.getId()).collect(Collectors.toSet());
                QueryWrapper queryWrapper3 = new QueryWrapper();
                queryWrapper3.in("parentId", pSet);
                List<Category> parentIdList3 = categoryDao.selectList(queryWrapper3);
                Set<Long> pSet3 = parentIdList3.stream().map(p -> p.getId()).collect(Collectors.toSet());
                parentSet.addAll(pSet);
                parentSet.addAll(pSet2);
                parentSet.addAll(pSet3);
                productDTO.setParentIdSet(parentSet);
            }
        }
        Page page = productDTO.getMyBatisPage();
        List<Product> productList = productDao.queryProductForList(page, productDTO);
        if (productList.isEmpty()) {
            return page;
        }
        if (TypeEnum.PRODUCT_PURCHASE.getValue().equals(productDTO.getType())) {
            page.setRecords(productPurchaseOtherExtra(productList));
        }else if(TypeEnum.PRODUCT_LOGISTICS.getValue().equals(productDTO.getType())){
            page.setRecords(productLogisticsOtherExtra(productList));
        } else {
            page.setRecords(productOtherExtra(productList));
        }
        return page;
    }

    public List<ProductListDTO> queryProductIndexList() {
        Page<ProductRecommend> pageRecommend = new Page(1, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", SUCCESS);
        queryWrapper.orderByDesc("weights");
        productRecommendDao.selectPage(pageRecommend, queryWrapper);
        List<String> stringList = pageRecommend.getRecords().stream().map(ProductRecommend::getProductTxnId).collect(Collectors.toList());
        List<Product> productList = productDao.queryProductIndexList(pageSize, stringList);
        if (productList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return productOtherExtra(productList);
    }

    public List<ProductListDTO> productPurchaseOtherExtra(List<Product> productList) {
        List<ProductListDTO> productDtos = ObjectUtils.copy(productList, ProductListDTO.class);
        Set<Long> categoryIds = productDtos.stream().map(ProductListDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryMap = categoryService.queryCategoryMap(categoryIds);
        Set<String> txnIdSet = productDtos.stream().map(ProductListDTO::getTxnId).collect(Collectors.toSet());
        Map<String, List<ProductStandardRecordListDTO>> standardListMap = productStandardRecordService.queryProductStandardListMap(txnIdSet);
        for (ProductListDTO p : productDtos) {
            p.setStandardList(standardListMap.get(p.getTxnId()));
            CategoryDTO categoryDTO = categoryMap.get(p.getCategoryId());
            Optional.ofNullable(categoryDTO).ifPresent(category -> p.setCategoryName(category.getTitle()));
        }
        return productDtos;
    }

    public List<ProductListDTO> productLogisticsOtherExtra(List<Product> productList) {
        List<ProductListDTO> productDtos = ObjectUtils.copy(productList, ProductListDTO.class);
        Set<Long> urlIds = productDtos.stream().map(ProductListDTO::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> idToRealUrlMap = imageRemoteService.queryUrls(urlIds);
        Set<String> txnIdSet = productDtos.stream().map(ProductListDTO::getTxnId).collect(Collectors.toSet());
        Map<String, List<ProductStandardRecordListDTO>> standardListMap = productStandardRecordService.queryProductStandardListMap(txnIdSet);
        Set<String> txnIds = productDtos.stream().map(ProductListDTO::getTxnId).collect(Collectors.toSet());
        Map<String,ProductLogisticsListDTO> logisticsMap = productLogisticsService.queryProductLogisticsForTxnId(txnIds);
        for (ProductListDTO p : productDtos) {
            p.setShopBriefness(shopService.queryShopBriefnessInformation(p.getShopId()));
            String[] split = StringUtils.split(p.getImgs(), ",");
            if (!ArrayUtils.isEmpty(split)) {
                Optional<String> firstImgUrl = Arrays.stream(split).map(Long::valueOf).map(idToRealUrlMap::get).findFirst();
                p.setImgs(firstImgUrl.orElse(""));
            }
            ProductLogisticsListDTO logistics = logisticsMap.get(p.getTxnId());
            if (null == logistics){
                logistics = ProductLogisticsListDTO.builder().build();
                List<ProductStandardRecordListDTO> productStandardRecordList = standardListMap.get(p.getTxnId());
                if (CollectionUtils.isNotEmpty(productStandardRecordList)){
                    String source = productStandardRecordList.get(0).getData();
                    String target = productStandardRecordList.get(1).getData();
                    logistics.setSource(source.substring(0,source.lastIndexOf("-")))
                            .setTarget(target.substring(0,target.lastIndexOf("-")))
                            .setLogisticsType(ProductLogisticsTypeEnum.LOGISTICS_TYPE_LAND.getValue()).setRoute(1);
                }
            }
            logistics.setLogisticsTypeName(ProductLogisticsTypeEnum.getProductLogisticsTypeEnum(logistics.getLogisticsType()).getMeaning())
                    .setRouteName(1 == logistics.getRoute() ? "专线物流" : "非专线物流");
            p.setLogistics(logistics);
            SellerPrivilegeDTO sellerPrivilegeDTO = userPrivilegeRemoteService.sellerPrivilegeByUser(p.getShopId()).getData();
            p.getShopBriefness().setLevel(sellerPrivilegeDTO.getLevel());
            p.getShopBriefness().setLevelNickname(sellerPrivilegeDTO.getLevelNickname());
        }
        return productDtos;
    }

    public List<ProductListDTO> productOtherExtra(List<Product> productList) {
        List<ProductListDTO> productDtos = ObjectUtils.copy(productList, ProductListDTO.class);
        Set<Long> urlIds = productDtos.stream().map(ProductListDTO::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> idToRealUrlMap = imageRemoteService.queryUrls(urlIds);
        Set<Long> categoryIds = productDtos.stream().map(ProductListDTO::getCategoryId).collect(Collectors.toSet());
        Map<Long, CategoryDTO> categoryMap = categoryService.queryCategoryMap(categoryIds);
        for (ProductListDTO p : productDtos) {
            p.setShopBriefness(shopService.queryShopBriefnessInformation(p.getShopId()));
            CategoryDTO categoryDTO = categoryMap.get(p.getCategoryId());
            String[] split = StringUtils.split(p.getImgs(), ",");
            if (!ArrayUtils.isEmpty(split)) {
                Optional<String> firstImgUrl = Arrays.stream(split).map(Long::valueOf).map(idToRealUrlMap::get).findFirst();
                p.setImgs(firstImgUrl.orElse(""));
            }
            Optional.ofNullable(categoryDTO).ifPresent(category -> p.setCategoryName(category.getTitle()));
            SellerPrivilegeDTO sellerPrivilegeDTO = userPrivilegeRemoteService.sellerPrivilegeByUser(p.getShopId()).getData();
            p.getShopBriefness().setLevel(sellerPrivilegeDTO.getLevel());
            p.getShopBriefness().setLevelNickname(sellerPrivilegeDTO.getLevelNickname());
        }
        return productDtos;
    }

    public ProductDetailDTO queryProductDetail(Long productId, Long userId) {
        Product product = productDao.queryProductDetail(productId);
        if (product == null) {
            Optional.ofNullable(product).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), ViewCode.PRODUCT_FAILURE.getMessage()));
            return null;
        }
        ProductStandardRecordDTO standardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(product.getTxnId()).setCategoryId(product.getCategoryId());
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(standardRecordDTO);
        List<ProductStandardRecordListDTO> productStandardRecords = standardList.stream().map(ProductStandardRecordListDTO::of).collect(Collectors.toList());
        ProductDetailDTO productDetailDTO = ProductDetailDTO.of(product);
        ShopWithStatusDTO shopBriefness = shopService.queryShopWithStatusDTO(product.getShopId());
        Integer isShop = shopBriefness != null && shopBriefness.getUserId().equals(userId) ? 1 : 0;
        Integer isUser = product.getUserId().equals(userId) ? 1 : 0;
        Integer type = TypeEnum.PRODUCT_PURCHASE.getValue();
        List<String> imgList = queryImgsOtherExtra(product.getImgs());
        CategoryDTO category = categoryService.queryCategorys(product.getCategoryId());
        Boolean personalCertificate = userAuthRemoteService.queryRealnameAuthStatus(userId).getData();
        final Boolean enterpriseCertification = userAuthRemoteService.queryEnterpriseAuthStatus(userId).getData();
        UserAuthDetailDTO userEntity = product.getType().equals(type) ? getUserAuthDetailDTO(product.getUserId()) : null;
        productDetailDTO.setProductStandardList(productStandardRecords)
                .setImgList(imgList)
                .setCategory(category)
                .setShopBriefness(shopBriefness)
                .setPersonalCertificate(personalCertificate)
                .setEnterpriseCertification(enterpriseCertification)
                .setUserIfShop(type.equals(product.getType()) ? isUser : isShop)
                .setUserEntity(userEntity);
        return productDetailDTO;
    }

    private UserAuthDetailDTO getUserAuthDetailDTO(Long userId) {
        UserAuthDetailDTO userEntity = userInfosRemoteService.queryUserAuthDetail(userId).getData();
        userEntity.setBuyerPrivilegeDTO(userPrivilegeRemoteService.buyerPrivilegeByUser(userId).getData());
        //设置头像
        UserDTO userDto = userInfosRemoteService.infos(null, userId).getData();
        Long profileImgId = userDto.getProfileImg();
        if (profileImgId != null) {
            userEntity.setProfileImg(profileImgId);
            userEntity.setHeaderUrl(imageRemoteService.queryUrl(profileImgId));
        }
        return userEntity;
    }

    public ProductInfoDTO queryProductInfo(Long productId) {
        Product product = productDao.queryProductDetail(productId);
        return Product.of(product, queryImgsOtherExtra(product.getImgs()));
    }

    public List<String> queryImgsOtherExtra(String imgs) {
        Set<Long> imgIds = Arrays.stream(imgs.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(imgIds);
        List<String> imageList = new ArrayList(urls.size());
        imgIds.forEach(id -> imageList.add(urls.get(id)));
        return imageList;
    }

    public IPage<ProductBriefnessDTO> queryProductSimplenessInfo(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO) {
        IPage<ProductBriefnessDTO> shopWithProductPage = productDao.queryProductsByShopId(shopWithProductQueryPageDTO);
        convertImgsToMainImage(shopWithProductPage);
        return shopWithProductPage;
    }

    public IPage<ProductBriefnessDTO> queryItsRockedProducts(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO) {
        IPage<ProductBriefnessDTO> shopWithProductPage = productDao.queryItsRockedProducts(shopWithProductQueryPageDTO);
        convertImgsToMainImage(shopWithProductPage);
        return shopWithProductPage;
    }

    public IPage<ProductBriefnessDTO> queryItsUnRockedProducts(ShopWithProductQueryPageDTO shopWithProductQueryPageDTO, Long userId) {
        Optional.ofNullable(userId).orElseThrow(() -> new ShopException(ViewCode.SHOP_UN_RACK_LIST_FAILURE.getCode(), "请先登录再查看已下架的商品"));
        IPage<ProductBriefnessDTO> shopWithProductPage = productDao.queryItsUnRockedProducts(shopWithProductQueryPageDTO, userId);
        convertImgsToMainImage(shopWithProductPage);
        return shopWithProductPage;
    }

    private void convertImgsToMainImage(IPage<ProductBriefnessDTO> productBriefnessDTOIPage) {
        if (productBriefnessDTOIPage.getRecords().isEmpty()) {
            return;
        }
        Set<Long> urlIds = productBriefnessDTOIPage.getRecords().stream().map(ProductBriefnessDTO::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(urlIds);
        productBriefnessDTOIPage.getRecords().forEach(x -> {
            Long mainImageId = Long.valueOf(x.getImgs().split(",")[0]);
            x.setImgs(urls.get(mainImageId));
        });
    }

    public Map<Long, ProductInfoDTO> queryProductInfoByIds(Set<Long> ids) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("id", ids);
        List<Product> list = productDao.selectList(wrapper);
        Set<Long> urlIds = list.stream().map(Product::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> urls = imageRemoteService.queryUrls(urlIds);
        Map<Long, ProductInfoDTO> products = list.stream().collect(Collectors.toMap(Product::getId, Product::of));
        Map<Long, ProductAuctionDTO> auctions = auctionQueryService.queryProductAuctionByProductIds(ids);
        products.values().forEach(p -> {
            Optional.ofNullable(p.getType()).filter(type -> type.equals(4)).ifPresent(type -> {
                ProductAuctionDTO auctionDTO = auctions.get(p.getId());
                Optional.ofNullable(auctionDTO).ifPresent(auction -> p.setAuctionType(auction.getAuctionType()));
            });
            Set<Long> productUrlIds = Arrays.stream(p.getImgs().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<String> imageList = new ArrayList<>(productUrlIds.size());
            productUrlIds.forEach(id -> imageList.add(urls.get(id)));
            p.setImgs(imageList.isEmpty() ? "" : imageList.get(0));
        });
        return products;
    }

    public Integer updateProductForId(Long productId, ProductRecordDTO productRecordDTO, Long userId) {
        if (!productRecordDTO.getImageList().isEmpty()) {
            RestResult<List<Long>> imgsResult = imageRemoteService.batchSaveImage(productRecordDTO.getImageList());
            List<Long> imgs =  imgsResult.getData();
            productRecordDTO.setImgs(StringUtils.strip(imgs.toString(), "[]").replace(" ", ""));
        }
        Integer result = productDao.updateProductForId(productId, productRecordDTO, userId);
        Optional.ofNullable(result).filter(r -> !r.equals(0)).orElseThrow(() -> new ProductException(ViewCode.PRODUCT_FAILURE.getCode(), "修改商品信息失败,请联系管理员"));
        return result;
    }

    public Page queryProductSupplyForList(Integer current, Integer size, String keywords) {
        Page<Product> page = new Page(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", SUCCESS);
        wrapper.in("type", TypeEnum.PRODUCT_DEMAND.getValue(), TypeEnum.PRODUCT_SUPPLY.getValue());
        wrapper.gt("expireTime", LocalDate.now());
        wrapper.like("title", keywords);
        wrapper.orderByDesc("createTime");
        productDao.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        page.setRecords(queryProductSupplyOther(page.getRecords()));
        return page;
    }

    public Page queryProductSupplyForListByShopId(Integer current, Integer size, Long shopId) {
        ShopBriefnessDTO shopBriefnessDTO = shopService.queryShopBriefnessInformation(shopId);
        Page<Product> page = new Page(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("type", TypeEnum.PRODUCT_DEMAND.getValue(), TypeEnum.PRODUCT_SUPPLY.getValue());
        wrapper.eq("userId", shopBriefnessDTO.getUserId());
        wrapper.eq("status", SUCCESS);
        wrapper.gt("expireTime", LocalDate.now());
        wrapper.orderByDesc("createTime");
        productDao.selectPage(page, wrapper);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return page;
        }
        page.setRecords(queryProductSupplyOther(page.getRecords()));
        return page;
    }

    public List queryProductSupplyOther(List<Product> productList) {
        Set<String> txnIds = productList.stream().map(Product::getTxnId).collect(Collectors.toSet());
        Set<Long> shopIds = productList.stream().map(Product::getShopId).collect(Collectors.toSet());
        Map<String, ProductCarInfoDTO> carInfoMap = productCarInfoService.queryProductCarInfoList(txnIds);
        Map<String, ProductPositionDTO> positionMap = productPositionService.queryProductPosition(txnIds);
        Map<Long, ShopDTO> shopMap = shopService.queryByIds(shopIds);
        List supplyList = new ArrayList<>();
        productList.stream().forEach(product -> {
            ProductSupplyDTO supplyDTO = Product.ofToSupply(product);
            ProductPositionDTO positionDTO = positionMap.get(product.getTxnId());
            if (TypeEnum.PRODUCT_SUPPLY.getValue().equals(product.getType())) {
                ProductCarInfoDTO productCarInfoDTO = carInfoMap.get(product.getTxnId());
                if (productCarInfoDTO != null) {
                    supplyDTO.setSupply(productCarInfoDTO);
                    supplyDTO.getSupply().setShipDate(productCarInfoDTO.getSendDate().format(DateTimeFormatter.ofPattern("MM/dd")))
                            .setReachDate(productCarInfoDTO.getArrivalDate().format(DateTimeFormatter.ofPattern("MM/dd")));
                }
                supplyDTO.getSupply().setAddress(positionDTO.getAddress()).setTheWay(product.getTheWay());
            }
            if (TypeEnum.PRODUCT_DEMAND.getValue().equals(product.getType())) {
                supplyDTO.setDemand(positionDTO);
            }
            Category category = categoryService.queryParentCategory(product.getCategoryId());
            supplyDTO.setLongitude(positionDTO.getLongitude()).setLatitude(positionDTO.getLatitude())
                    .setShopBriefness(shopMap.get(product.getShopId())).setCategoryId(category.getId()).setCategoryName(category.getTitle());
            supplyList.add(supplyDTO);
        });
        return supplyList;
    }

    public ProductSupplyDetailDTO queryProductDetailSupply(Long productId) {
        Product product = productDao.selectById(productId);
        if (product == null) {
            return null;
        }
        ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(product.getTxnId()).setCategoryId(product.getCategoryId());
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardListDTO> productStandardRecords = new ArrayList<>(standardList.size());
        standardList.forEach(record -> productStandardRecords.add(ProductStandardRecord.of(record)));
        ProductSupplyDetailDTO productSupplyDetailDTO = Product.ofToDetail(product);
        productSupplyDetailDTO.setProductStandardList(productStandardRecords)
                .setCategoryName(categoryService.queryCategorys(product.getCategoryId()).getTitle())
                .setDepotAddress(productPositionService.queryProductPositionById(product.getTxnId()).getAddress())
                .setShopBriefness(shopService.queryById(product.getShopId()))
                .setImg(imageRemoteService.queryUrl(Long.valueOf(product.getImgs())));
        return productSupplyDetailDTO;
    }

    public ProductSupplyDetailDTO queryProductDetailSupplyByTxnId(String txnId) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("txnId", txnId);
        Product product = productDao.selectOne(wrapper);
        if (product == null) {
            return null;
        }
        ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                .setProductTxnId(product.getTxnId()).setCategoryId(product.getCategoryId());
        List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
        List<ProductStandardListDTO> productStandardRecords = new ArrayList<>(standardList.size());
        standardList.forEach(record -> productStandardRecords.add(ProductStandardRecord.of(record)));
        ProductSupplyDetailDTO productSupplyDetailDTO = Product.ofToDetail(product);
        productSupplyDetailDTO.setProductStandardList(productStandardRecords)
                .setCategoryName(categoryService.queryCategorys(product.getCategoryId()).getTitle())
                .setDepotAddress(productPositionService.queryProductPositionById(product.getTxnId()).getAddress())
                .setShopBriefness(shopService.queryById(product.getShopId()))
                .setImg(imageRemoteService.queryUrl(Long.valueOf(product.getImgs())));
        return productSupplyDetailDTO;
    }

    public List<ProductSupplyDTO> querySupplyMapForList(Double longitude, Double latitude, Integer type, Long userId) {
        //redis 获取最近坐标
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        args.includeCoordinates().sortAscending().limit(geoLimit);
        GeoResults radius = redisTemplate.opsForGeo().radius(RedisKey.PRODUCT_GEO_LOCATION_KEY, new Circle(new Point(longitude, latitude), new Distance(30, Metrics.KILOMETERS)), args);
        if (CollectionUtils.isEmpty(radius.getContent())) {
            return Collections.EMPTY_LIST;
        }
        String json = JsonUtils.toJSON(radius);
        Results results = JsonUtils.parseJSON(json, Results.class);
        Set<String> txnIds = new HashSet<>();
        results.getResults().stream().forEach(r -> txnIds.add(r.getContent().getName()));
        return querySupplyMapOtherList(txnIds, type, userId);
    }

    public List<ProductSupplyDTO> querySupplyMapOtherList(Set<String> txnIds, Integer type, Long userId) {
        Set<Long> categoryIds = new HashSet();
        if (null != userId && 0 != userId) {
            Set<Long> longSet = attentionService.queryAttentionForUserId(userId);
            if (CollectionUtils.isNotEmpty(longSet)) {
                categoryIds = categoryService.queryChildCategory(longSet);
            }
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("status", SUCCESS);
        wrapper.in("txnId", txnIds);
        wrapper.in("type", TypeEnum.PRODUCT_SUPPLY.getValue(), TypeEnum.PRODUCT_DEMAND.getValue());
        wrapper.gt("expireTime", LocalDate.now());
        Optional.ofNullable(type).filter(t -> t != 0).ifPresent(t -> wrapper.eq("theWay", type));
        Optional.ofNullable(categoryIds).ifPresent(category -> wrapper.in("categoryId", category));
        wrapper.orderByDesc("createTime");
        List<Product> list = productDao.selectList(wrapper);
        return queryProductSupplyOther(list);
    }

    public ProductHomeCountDTO queryProductHomeCount() {
        Integer supplyValue = TypeEnum.PRODUCT_SUPPLY.getValue();
        Integer demandValue = TypeEnum.PRODUCT_DEMAND.getValue();
        Integer whole = productDao.selectCount(Wrappers.<Product>lambdaQuery().eq(Product::getStatus, SUCCESS).in(Product::getType, supplyValue, demandValue).gt(Product::getExpireTime, LocalDate.now()));
        Integer theStock = productDao.selectCount(Wrappers.<Product>lambdaQuery().eq(Product::getStatus, SUCCESS).eq(Product::getTheWay, SUCCESS).eq(Product::getType, supplyValue).gt(Product::getExpireTime, LocalDate.now()));
        Integer theWay = productDao.selectCount(Wrappers.<Product>lambdaQuery().eq(Product::getStatus, SUCCESS).eq(Product::getTheWay, FAILURE).eq(Product::getType, supplyValue).gt(Product::getExpireTime, LocalDate.now()));
        return ProductHomeCountDTO.builder().build().setWhole(whole).setTheStock(theStock).setTheWay(theWay);
    }
}
