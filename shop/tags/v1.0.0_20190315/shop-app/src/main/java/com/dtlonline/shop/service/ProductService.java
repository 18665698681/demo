package com.dtlonline.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.*;
import com.dtlonline.api.shop.constant.TypeEnum;
import com.dtlonline.api.shop.view.ProductInfoDTO;
import com.dtlonline.api.user.command.system.SellerPrivilegeDTO;
import com.dtlonline.api.user.remote.PrivilegeRemoteService;
import com.dtlonline.api.user.remote.UserRemoteService;
import com.dtlonline.api.user.view.UserAuthDetailDTO;
import com.dtlonline.shop.exception.ProductException;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.CategoryDao;
import com.dtlonline.shop.mapper.ProductDao;
import com.dtlonline.shop.mapper.ProductStandardRecordDao;
import com.dtlonline.shop.model.Category;
import com.dtlonline.shop.model.Product;
import com.dtlonline.shop.model.ProductStandardRecord;
import com.dtlonline.shop.util.ObjectUtils;
import com.dtlonline.shop.view.*;
import io.alpha.core.base.BaseService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private PrivilegeRemoteService privilegeRemoteService;

    public Page queryProductForList(ProductDTO productDTO) {
        if (productDTO.getCategoryId() != null && CollectionUtils.isNotEmpty(productDTO.getStandardList())) {
            Page txnIdPage = new Page(1,100);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("categoryId",productDTO.getCategoryId());
            wrapper.in("standardId",productDTO.getStandardList().stream().map(StandardRecordDTO::getStandardId).collect(Collectors.toSet()));
            wrapper.orderByDesc("txnId");
            IPage<ProductStandardRecord> page = productStandardRecordDao.selectPage(txnIdPage, wrapper);
            Map<String, Set<ProductStandardRecord>> standardMap = page.getRecords().stream().collect(Collectors.groupingBy(ProductStandardRecord::getTxnId, Collectors.toSet()));
            Set<String> txnIdSet = new HashSet<>();
            Set<String> sourceSet = productDTO.getStandardList().stream().map(s -> s.getStandardId() + ":" + s.getData()).collect(Collectors.toSet());
            standardMap.keySet().stream().filter(key -> StringUtils.isNotBlank(key)).forEach(txnId -> {
                Set<ProductStandardRecord> set = standardMap.get(txnId);
                Set<String> targetSet = set.stream().map(d -> d.getStandardId() + ":" + d.getData()).collect(Collectors.toSet());
                if(targetSet.containsAll(sourceSet)){
                    txnIdSet.add(txnId);
                }
            });
            productDTO.setTxnIdList(txnIdSet);
            if (txnIdSet.isEmpty()){
                return new Page();
            }
        }
        if (StringUtils.isNotBlank(productDTO.getKeywords())){
            productDTO.setKeywords("%"+productDTO.getKeywords()+"%");
        }
        if (productDTO.getCategoryId() != null && productDTO.getStandardList().isEmpty()){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id",productDTO.getCategoryId());
            Category category = categoryDao.selectOne(wrapper);
            if (0 == category.getParentId() && !TypeEnum.PRODUCT_STORE.getValue().equals(category.getType())){
                Set<Long> parentSet = new HashSet<>();
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parentId",category.getId());
                List<Category> parentIdList = categoryDao.selectList(queryWrapper);
                Set<Long> pSet = parentIdList.stream().map(p -> p.getId()).collect(Collectors.toSet());
                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.in("parentId",pSet);
                List<Category> parentIdList2 = categoryDao.selectList(queryWrapper2);
                Set<Long> pSet2 = parentIdList2.stream().map(p -> p.getId()).collect(Collectors.toSet());
                // TODO: 2019/3/15 使用递归不论出现多少层都可以运行
                QueryWrapper queryWrapper3 = new QueryWrapper();
                queryWrapper3.in("parentId",pSet);
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
        if (productList.isEmpty()){
            return page;
        }
        page.setRecords(productOtherExtra(productList));
        return page;
    }

    public List<ProductListDTO> queryProductIndexList() {
        Page page = new Page(1, 3);
        List<Product> productList = productDao.queryProductIndexList(page);
        if (productList.isEmpty()){
            return page.getRecords();
        }
        return productOtherExtra(productList);
    }

    public List<ProductListDTO> productOtherExtra(List<Product> productList){
        //转换成相关对象
        List<ProductListDTO> productDtos = ObjectUtils.transfer(productList, ProductListDTO.class);

        //所有图片提前转换存入map
        Set<Long> urlIds = productDtos.stream().map(ProductListDTO::getImgs).flatMap(s -> Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long, String> idToRealUrlMap = imageRemoteService.queryUrls(urlIds);

        //设置{ 商铺简介、图片转换、等级权限 }
        for (ProductListDTO p : productDtos) {
            p.setShopBriefness(shopService.queryShopBriefnessInformation(p.getShopId()));
            String[] split = StringUtils.split(p.getImgs(), ",");
            if (!ArrayUtils.isEmpty(split)) {
                Optional<String> firstImgUrl = Arrays.stream(split).map(Long::valueOf).map(idToRealUrlMap::get).findFirst();
                p.setImgs(firstImgUrl.orElse(""));
            }
            SellerPrivilegeDTO sellerPrivilegeDTO = privilegeRemoteService.sellerPrivilegeByUser(p.getShopId());
            p.getShopBriefness().setLevel(sellerPrivilegeDTO.getLevel());
            p.getShopBriefness().setLevelNickname(sellerPrivilegeDTO.getLevelNickname());
        }

        return productDtos;
    }

    public ProductDetailDTO queryProductDetail(Long productId,Long userId) {
        Product product = productDao.queryProductDetail(productId);
        if (product != null) {
            ProductStandardRecordDTO productStandardRecordDTO = new ProductStandardRecordDTO()
                    .setProductTxnId(product.getTxnId()).setCategoryId(product.getCategoryId());
            List<ProductStandardRecord> standardList = productStandardRecordDao.queryProductStandardList(productStandardRecordDTO);
            List<ProductStandardRecordListDTO> productStandardRecords = new ArrayList<>(standardList.size());
            standardList.forEach(addr -> productStandardRecords.add(ProductStandardRecordListDTO.of(addr)));
            ProductDetailDTO productDetailDTO = ProductDetailDTO.of(product);
            productDetailDTO.setProductStandardList(productStandardRecords)
                    .setImgList(queryImgsOtherExtra(product.getImgs()))
                    .setCategory(categoryService.queryCategorys(product.getCategoryId()))
                    .setShopBriefness(shopService.queryShopWithStatusDTO(product.getShopId()))
                    .setPersonalCertificate(userRemoteService.queryRealnameAuthStatus(userId));
            Optional.ofNullable(productDetailDTO.getShopBriefness()).ifPresent(shop ->{
                productDetailDTO.setUserIfShop(productDetailDTO.getShopBriefness().getUserId().equals(userId) ? 1 : 0);
            });
            Optional.ofNullable(product.getType()).filter(type -> type.equals(99)).ifPresent(t ->{
                UserAuthDetailDTO userEntity = userRemoteService.queryUserAuthDetail(product.getUserId());
                userEntity.setEnterpriseCertification(userRemoteService.queryEnterpriseAuthStatus(product.getUserId()));
                userEntity.setPersonalCertificate(userRemoteService.queryRealnameAuthStatus(product.getUserId()));
                userEntity.setFieldCertification(userRemoteService.querySiteAuthStatus(product.getUserId()));
                productDetailDTO.setUserEntity(userEntity);
            });
            return productDetailDTO;
        }
        Optional.ofNullable(product).orElseThrow(()->new ProductException(ViewCode.PRODUCT_FAILURE.getCode(),ViewCode.PRODUCT_FAILURE.getMessage()));
        return null;
    }

    public ProductInfoDTO queryProductInfo(Long productId) {
        Product product = productDao.queryProductDetail(productId);
        return Product.of(product,queryImgsOtherExtra(product.getImgs()));
    }

    public List<String> queryImgsOtherExtra(String imgs){
        Set<Long> imgIds = Arrays.stream(imgs.split(",")).map(Long::valueOf).collect(Collectors.toSet());
        Map<Long,String> urls = imageRemoteService.queryUrls(imgIds);
        List<String> imageList = new ArrayList(urls.size());
        imgIds.forEach(id->imageList.add(urls.get(id)));
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
        Optional.ofNullable(userId).orElseThrow(()-> new ShopException(ViewCode.SHOP_UN_RACK_LIST_FAILURE.getCode(),"请先登录再查看已下架的商品"));
        IPage<ProductBriefnessDTO> shopWithProductPage = productDao.queryItsUnRockedProducts(shopWithProductQueryPageDTO, userId);
        convertImgsToMainImage(shopWithProductPage);
        return shopWithProductPage;
    }

    private void convertImgsToMainImage(IPage<ProductBriefnessDTO> productBriefnessDTOIPage) {
        if (productBriefnessDTOIPage.getRecords().isEmpty()){
            return ;
        }
        Set<Long> urlIds = productBriefnessDTOIPage.getRecords().stream().map(ProductBriefnessDTO::getImgs).flatMap(s->Arrays.stream(s.split(","))).map(Long::valueOf).collect(Collectors.toSet());
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
        products.values().forEach(p -> {
            Set<Long> productUrlIds = Arrays.stream(p.getImgs().split(",")).map(Long::valueOf).collect(Collectors.toSet());
            List<String> imageList = new ArrayList<>(productUrlIds.size());
            productUrlIds.forEach(id->imageList.add(urls.get(id)));
            p.setImgs(imageList.isEmpty() ? "" : imageList.get(0));
        });
        return products;
    }

    public Integer updateProductForId(Long productId , ProductRecordDTO productRecordDTO,Long userId){
        if (!productRecordDTO.getImageList().isEmpty()){
            List<Long> imgs = imageRemoteService.batchSaveImage(productRecordDTO.getImageList());
            productRecordDTO.setImgs(StringUtils.strip(imgs.toString(),"[]").replace(" ",""));
        }
        Integer result = productDao.updateProductForId(productId,productRecordDTO,userId);
        Optional.ofNullable(result).filter(r ->!r.equals(0)).orElseThrow(()->new ProductException(ViewCode.PRODUCT_FAILURE.getCode(),"修改商品信息失败,请联系管理员"));
        return result;
    }
}
