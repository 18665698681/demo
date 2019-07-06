package com.dtlonline.shop.service.groupbuy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyQueryRequestDTO;
import com.dtlonline.api.shop.command.groupbuy.ProductGroupbuyRequestDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyResponseDTO;
import com.dtlonline.api.shop.view.groupbuy.ProductGroupbuyStandardResponseDTO;
import com.dtlonline.shop.constant.groupbuy.ProductGroupbuyStatus;
import com.dtlonline.shop.exception.ShopException;
import com.dtlonline.shop.mapper.groupbuy.ProductGroupbuyDao;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuy;
import com.dtlonline.shop.model.groupbuy.ProductGroupbuyStandard;
import com.dtlonline.shop.view.ViewCode;
import io.alpha.app.core.base.BaseService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 团购活动service
 */
@Service
public class ProductGroupbuyService extends BaseService {

    @Autowired
    private ProductGroupbuyDao productGroupbuyDao;

    @Autowired
    private ImageRemoteService imageRemoteService;

    @Autowired
    private ProductGroupbuyStandardService productGroupbuyStandardService;

    /**
     * 团购活动状态统计
     *
     * @param
     * @return Map<String, Integer>
     */
    public Map<String, Integer> queryGroupbuyStatistics() {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.ne("status", ProductGroupbuyStatus.DEL.getCode());
        List<ProductGroupbuy> productGroupbuys = productGroupbuyDao.selectList(wrapper);

        Map<String, Integer> map = new HashMap<>();

        productGroupbuys.forEach(p -> {

            String statusMessage = ProductGroupbuyStatus.getMessageByCode(p.getStatus());
            Integer statusNum = MapUtils.getInteger(map, statusMessage, 0);
            map.put(statusMessage, ++statusNum);
        });

        return map;
    }

    /**
     * 根据ID主键获取团购活动信息
     *
     * @param id 主键
     * @return ProductGroupbuy 团购活动实体
     */
    public ProductGroupbuy get(Long id) {
        return productGroupbuyDao.selectById(id);
    }

    /**
     * 新增团购活动
     *
     * @param productGroupbuyRequestDTO 新增团活动对象参数
     * @param userId                    用户ID
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(ProductGroupbuyRequestDTO productGroupbuyRequestDTO, Long userId) {

        // insert团购主表
        ProductGroupbuy productGroupbuy = new ProductGroupbuy();
        BeanUtils.copyProperties(productGroupbuyRequestDTO, productGroupbuy);
        productGroupbuy.setUserId(userId);
        productGroupbuy.setBuyBeginDate(LocalDate.now());

        productGroupbuyDao.insert(productGroupbuy);

        // insert团购规则表
        productGroupbuyRequestDTO.getStandardRequestDTOs().stream().forEach(s -> {

            ProductGroupbuyStandard productGroupbuyStandard = new ProductGroupbuyStandard();
            BeanUtils.copyProperties(s, productGroupbuyStandard);
            productGroupbuyStandard.setUserId(userId).setProductGroupbuyId(productGroupbuy.getId());
            productGroupbuyStandardService.save(productGroupbuyStandard);
        });
    }

    /**
     * 查询团购活动列表
     *
     * @param productGroupbuyQueryRequestDTO 查询请求DTO
     * @return IPage<ProductGroupbuyResponseDTO> 团购活动列表
     */
    public IPage<ProductGroupbuyResponseDTO> queryListInPage(ProductGroupbuyQueryRequestDTO productGroupbuyQueryRequestDTO) {

        IPage<ProductGroupbuyResponseDTO> productGroupbuyResponseDTOIPage = productGroupbuyDao.queryListInPage(productGroupbuyQueryRequestDTO);

        productGroupbuyResponseDTOIPage.getRecords().forEach(g -> {

            List<String> discounts = new ArrayList<>();
            List<ProductGroupbuyStandard> productGroupbuyStandards = productGroupbuyStandardService.queryListByProductGroupbuyId(g.getId());
            productGroupbuyStandards.forEach(s -> {

                Double targetWeight = ObjectUtils.defaultIfNull(s.getTargetWeight(), 0d);
                BigDecimal priceScale = ObjectUtils.defaultIfNull(s.getPriceScale(), BigDecimal.ZERO);
                discounts.add(String.format("达标%s吨；优惠%s%%", targetWeight.toString(), priceScale.toString()));
            });

            g.setDiscounts(discounts);
        });

        return productGroupbuyResponseDTOIPage;
    }

    /**
     * 团购活动详情
     *
     * @param id 团购活动ID
     * @return Map<String, Integer>
     */
    public ProductGroupbuyResponseDTO queryDetailById(Long id) {

        ProductGroupbuy productGroupbuy = productGroupbuyDao.selectById(id);
        List<ProductGroupbuyStandard> productGroupbuyStandards = productGroupbuyStandardService.queryListByProductGroupbuyId(id);

        Optional.ofNullable(productGroupbuy).orElseThrow(() -> new ShopException(ViewCode.SHOP_FAILURE.getCode(), "团购活动信息不存在"));
        Optional.ofNullable(productGroupbuyStandards).orElseThrow(() -> new ShopException(ViewCode.SHOP_FAILURE.getCode(), "团购活动规则不存在"));

        // 获取图片列表
        String showImageIds = productGroupbuy.getShowImageIds();
        List<String> imageUrls = new ArrayList();
        if (StringUtils.isNoneBlank(showImageIds)) {

            Set<Long> imageIds = Arrays.stream(showImageIds.split(",")).map(Long::valueOf).collect(Collectors.toSet());
            Map<Long, String> urls = imageRemoteService.queryUrls(imageIds);
            imageIds.forEach(imageId -> imageUrls.add(urls.get(imageId)));
        }

        // 处理简介中图片的URL
        String description = productGroupbuy.getDescription();
        List<String> srcs = this.getImgSrcs(description);
        for (String src : srcs) {

            String regex = String.format("src=\"%s\"", src);
            String imageUrl = imageRemoteService.queryUrl(Long.parseLong(src));
            String replacement = String.format("src=\"%s\"", imageUrl);
            description = description.replaceAll(regex, replacement);
        }

        List<ProductGroupbuyStandardResponseDTO> productGroupbuyStandardResponseDTOs = io.alpha.app.core.util.ObjectUtils.copy(productGroupbuyStandards, ProductGroupbuyStandardResponseDTO.class);

        ProductGroupbuyResponseDTO productGroupbuyResponseDTO = new ProductGroupbuyResponseDTO();
        BeanUtils.copyProperties(productGroupbuy, productGroupbuyResponseDTO);

        productGroupbuyResponseDTO.setImageUrls(imageUrls);
        productGroupbuyResponseDTO.setDescription(description);
        productGroupbuyResponseDTO.setStandardDTOs(productGroupbuyStandardResponseDTOs);

        return productGroupbuyResponseDTO;
    }

    /**
     * 获取字符串中图片的src地址
     *
     * @param str 字符串
     * @return List<String> src列表
     */
    public static List<String> getImgSrcs(String str) {

        if (org.apache.commons.lang.StringUtils.isBlank(str)) {
            return null;
        }

        List<String> images = new ArrayList<>();
        String img = StringUtils.EMPTY;
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(str);
        while (m_image.find()) {

            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                images.add(m.group(1));
            }
        }

        return images;
    }

}
