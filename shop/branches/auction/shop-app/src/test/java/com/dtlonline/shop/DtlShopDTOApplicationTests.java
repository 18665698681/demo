package com.dtlonline.shop;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dtlonline.api.isp.remote.ImageRemoteService;
import com.dtlonline.api.shop.command.CheckInfoDTO;
import com.dtlonline.api.shop.command.ShopAuditDTO;
import com.dtlonline.api.shop.command.ShopAuthQueryPageDTO;
import com.dtlonline.api.shop.command.ShopPageQueryDTO;
import com.dtlonline.api.shop.constant.ShopTypeEnum;
import com.dtlonline.api.shop.constant.Status;
import com.dtlonline.shop.mapper.ShopAuthRecordDao;
import com.dtlonline.shop.mapper.ShopDao;
import com.dtlonline.shop.model.Shop;
import com.dtlonline.shop.model.ShopAuthRecord;
import com.dtlonline.shop.service.ShopService;
import com.dtlonline.shop.utilities.GetJsonFromClass;
import com.dtlonline.shop.utilities.OssClientUtility;
import com.dtlonline.shop.utilities.PropertyEditorConverter;
import com.dtlonline.shop.view.ShopBriefnessDTO;
import com.github.kevinsawicki.http.HttpRequest;
import io.alpha.core.view.RestResult;
import io.alpha.security.aes.AESUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DtlShopDTOApplicationTests {

    private Logger logger = LoggerFactory.getLogger("测试用例");

    private MockMvc mockMvc;

    @Autowired WebApplicationContext wac;

    @Autowired private ShopDao shopDao;
    @Autowired private ShopAuthRecordDao shopAuthRecordDao;
    @Autowired private ShopService shopService;
    @Autowired private ImageRemoteService imageRemoteService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void contextLoads() {
    }

    @Test
    @Ignore
    public void testCommodityDetailByIdEveryTestUnit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/queryCommodityDetailById/12")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Code").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isMap());

        mockMvc.perform(MockMvcRequestBuilders.get("/queryCommodityDetailById/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Code").value("FAIL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isString());

        mockMvc.perform(MockMvcRequestBuilders.get("/queryCommodityDetailById/200007")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Code").value("FAIL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isString());
    }


    @Test
    @Ignore
    public void testTransactionInShopAuditOperation(){
        // todo: 这里可能不能通过配置文件那块去配置，而是在测试环境中做一层处理
        // todo: 除了部分接口
        // h2数据库上已经测试好过了 数据结构更高后，这里作废弃处理
        String normalOpinion = "信息缺失，不允许通过";
        String transactionOpinion = "可以了";
        ShopAuthRecord shopAuthRecordNormal =  new ShopAuthRecord();
        shopAuthRecordNormal.setStaffName("123");
        shopAuthRecordNormal.setOpinion(normalOpinion);
        shopAuthRecordNormal.setStatus(Status.NOT_PASS.getValue());
        try {
            // shopAuditOperationService.saveOperationAndCopyInfo("a", shopAuthRecordNormal);
        } catch (Exception e) {
            logger.info("正常的保存信息");
        }
        ShopAuthRecord shopAuthRecordTransaction = new ShopAuthRecord();
        shopAuthRecordTransaction.setStaffName("321");
        shopAuthRecordTransaction.setOpinion(transactionOpinion);
        shopAuthRecordTransaction.setStatus(Status.PASS.getValue());
        try {
            // shopAuditOperationService.saveOperationAndCopyInfo("s", shopAuthRecordTransaction);
        } catch (Exception e) {
            logger.info("保存信息的时候出现错误");
        }

        // 校验信息完整性
        Assert.assertNotNull(shopService.queryAuditOperationByTxnId("a"));
        Assert.assertNull(shopService.queryAuditOperationByTxnId("s"));
    }

    @Test
    public void testObjectStorageServiceBucketListInfo(){
        // 这里如果是一个服务，单例作为bean衍生出来然后在注入到其他需要用到的模块中去？or其他方式？
        OssClientUtility ossClientUtility = new OssClientUtility();
        ossClientUtility.testOssListObject();
    }

    @Test
    @Ignore
    public void testSpringJunitTestRollbackData() {
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.builder()
                .txnId("xxx")
                .name("商铺1号")
                .type(ShopTypeEnum.THIRD_PARTY_STORE.getValue())
                .area("广州")
                .address("广东省 广州 xx区 xx街 3号")
                .contactName("联系人名字")
                .mobile("13487964555")
                .staffName("操作员的id")
                .status(Status.PASS.getValue())
                .opinion("通过").build();
    }

    @Test
    public void testBeanUtilsCopyProperties(){
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.builder()
                .txnId("xxx")
                .name("商铺1号")
                .type(ShopTypeEnum.THIRD_PARTY_STORE.getValue())
                .area("广州")
                .address("广东省 广州 xx区 xx街 3号")
                .contactName("联系人名字")
                .mobile("13487964555")
                .staffName("操作员的id")
                .status(Status.PASS.getValue())
                .opinion("通过").build();
        Shop shop =  new Shop();
        BeanUtils.copyProperties(shopAuthRecord, shop);
        Assert.assertEquals(shop.getId(), shopAuthRecord.getId());
        Assert.assertEquals(shop.getName(), shopAuthRecord.getName());
    }

    @Ignore
    @Test
    public void testEnumConverter() {
        PropertyEditorConverter propertyEditorConverter = new PropertyEditorConverter(ShopTypeEnum.class);
        propertyEditorConverter.setAsText("1");
        Assert.assertEquals(propertyEditorConverter.getValue(), ShopTypeEnum.THIRD_PARTY_STORE);

        propertyEditorConverter.setAsText("3");
        logger.info("如果转换失败还是那个值吗？ {}", propertyEditorConverter.getValue());
        PropertyEditorConverter propertyEditorConverter2 = new PropertyEditorConverter(Status.class);
        propertyEditorConverter2.setAsText("1");
    }

    @Test
    public void testGsonToJson(){
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.builder()
                .txnId("xxx")
                .name("商铺1号")
                .type(ShopTypeEnum.THIRD_PARTY_STORE.getValue())
                .address("广东省 广州 xx区 xx街 3号")
                .contactName("联系人名字")
                .mobile("13487964555")
                .staffName("操作员的id")
                .status(Status.PASS.getValue())
                .opinion("通过").build();
        logger.info("转换后的信息:{}", GetJsonFromClass.toJson(shopAuthRecord));
        ShopAuditDTO shopAuditDTO = ShopAuditDTO.builder()
                .txnId("112544567789665455463287654")
                .address("广东-东莞-南城")
                .name("测试用商铺2")
                .type(ShopTypeEnum.SELF_SUPPORT_STORE.getValue())
                .contactName("xtw")
                .mobile("44558796644")
                .build();
        logger.info("{} 转换后的信息: {}", shopAuditDTO.getClass().getName()
                , GetJsonFromClass.toJson(shopAuditDTO));
    }

    @Ignore
    @Test
    public void testUpdateOperation(){
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.builder()
                .txnId("ceshiceshi")
                .name("测试更新操作店铺")
                .type(ShopTypeEnum.SELF_SUPPORT_STORE.getValue())
                .area("东莞")
                .address("广州东莞xx大道xx号")
                .contactName("xgd")
                .mobile("14328897555")
                .build();
        UpdateWrapper<ShopAuthRecord> updateWrapper =  new UpdateWrapper<>();
        shopAuthRecordDao.insert(shopAuthRecord);
        updateWrapper.set(true, "area", "测试后的地区");
        updateWrapper.set(true, "address", "测试后的地址");
        updateWrapper.eq("id", shopAuthRecord.getId());
        logger.info("难道连类都更新了值？id:{}", shopAuthRecord.getId());
        shopAuthRecordDao.update(new ShopAuthRecord(), updateWrapper);

        ShopAuthRecord shopAuthRecord1 = shopAuthRecordDao
                .selectOne(Wrappers.<ShopAuthRecord>query().eq("txnId", "ceshiceshi"));
        logger.info("更新后的数据:\narea:{}\naddress:{}"
                , shopAuthRecord1.getArea(), shopAuthRecord1.getAddress());
    }

    @Test
    public void testUpdateInService(){
        ShopAuthRecord shopAuthRecord = ShopAuthRecord.builder()
                .userId(123456L)
                .txnId("ceshiceshi")
                .name("测试更新操作店铺")
                .type(ShopTypeEnum.SELF_SUPPORT_STORE.getValue())
                .area("东莞")
                .address("广州东莞xx大道xx号")
                .contactName("xgd")
                .mobile("14328897555")
                .build();
        shopAuthRecordDao.insert(shopAuthRecord);
        shopAuthRecord.setArea("啊哈");
        shopAuthRecord.setAddress("啊哈");
        shopAuthRecord.setStatus(Status.NOT_PASS.getValue());
        CheckInfoDTO checkInfoDTO = CheckInfoDTO.builder()
                .id(shopAuthRecord.getId())
                .status(Status.NOT_PASS.getValue())
                .opinion("信息不完全")
                .build();
//        shopAuthRecordDao.updateShopAuthRecordsById("s", checkInfoDTO);
        ShopAuthRecord shopAuthRecord1 = shopAuthRecordDao.selectOne(Wrappers.<ShopAuthRecord>query()
                .eq("id", shopAuthRecord.getId()));
        //Assert.assertEquals(Status.NOT_PASS.getValue(), shopAuthRecord1.getStatus());
        //Assert.assertEquals("信息不完全", shopAuthRecord1.getOpinion());
    }

    @Test
    @Ignore
    public void testApplyShopFlow() throws Exception {
        // TODO: 登录操作的补全
        // TODO: Test 返回信息校验
        String userJwt = "user1";
        String inSideJwt = "worker1";
        // 获取流水号 这里的请求会更改成动态类型的
        RestResult restResultFail = GetJsonFromClass
                .restResultFromJson(HttpRequest.get("http://localhost:8888/common/token").body());
        RestResult restResultSuccess = GetJsonFromClass
                .restResultFromJson(HttpRequest.get("http://localhost:8888/common/token").body());
        String txnIdFail = String.valueOf(restResultFail.getData());
        String txnIdSuccess = String.valueOf(restResultSuccess.getData());
        // 用户申请店铺 这里获取的值默认就是流水号，但由于kv，所以再切分业务的话，是有可能出错
        ShopAuditDTO shopAuditDTO1 = ShopAuditDTO.builder()
                .txnId(txnIdFail)
                .name("测试店铺1")
                .type(ShopTypeEnum.SELF_SUPPORT_STORE.getValue())
                .area("东莞")
                .address("广州东莞xx大道xx号")
                .contactName("xgd")
                .mobile("14328897555")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/personal/apply")
                .header("Authorization", userJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(shopAuditDTO1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        ShopAuthRecord shopAuthRecord1 = shopAuthRecordDao
                .selectOne(Wrappers.<ShopAuthRecord>query().eq("txnId", txnIdFail));
        Assert.assertNotNull(shopAuthRecord1);
        // 查询审核单详情
        mockMvc.perform(MockMvcRequestBuilders.get("/personal/audit/{id}", shopAuthRecord1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // 客服批回审核
        CheckInfoDTO checkInfoDTO1 = CheckInfoDTO.builder()
                .id(shopAuthRecord1.getId())
                .status(Status.NOT_PASS.getValue())
                .opinion("联系手机显示空号")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/system/audit")
                .header("Authorization", inSideJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(checkInfoDTO1))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        shopAuthRecord1 = shopAuthRecordDao
                .selectOne(Wrappers.<ShopAuthRecord>query().eq("txnId", txnIdFail));
        Assert.assertEquals(Status.NOT_PASS.getValue(), shopAuthRecord1.getStatus());
        // 用户申请店铺2
        ShopAuditDTO shopAuditDTO2 = ShopAuditDTO.builder()
                .txnId(txnIdSuccess)
                .name("测试店铺1_之前是失败的")
                .type(ShopTypeEnum.SELF_SUPPORT_STORE.getValue())
                .area("东莞")
                .address("广州东莞xx大道xx号")
                .contactName("xgd")
                .mobile("1234567893")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/personal/apply")
                .header("Authorization", userJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(shopAuditDTO2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        ShopAuthRecord shopAuthRecord2 = shopAuthRecordDao
                .selectOne(Wrappers.<ShopAuthRecord>query().eq("txnId", txnIdSuccess));
        Assert.assertNotNull(shopAuthRecord2);
        // 客服查询审核单分页
        ShopAuthQueryPageDTO shopAuthQueryPageDTO = new ShopAuthQueryPageDTO();
        shopAuthQueryPageDTO.setCurrent(0);
        shopAuthQueryPageDTO.setSize(100);
        mockMvc.perform(MockMvcRequestBuilders.get("/system/audits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(shopAuthQueryPageDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // 客服或用户查询审核单详情
        mockMvc.perform(MockMvcRequestBuilders.get("/personal/audit/{id}", shopAuthRecord1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // 客服通过审核
        CheckInfoDTO checkInfoDTO2 = CheckInfoDTO.builder()
                .id(shopAuthRecord2.getId())
                .status(Status.PASS.getValue())
                .opinion("信息完整")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.put("/system/audit")
                .header("Authorization", inSideJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(checkInfoDTO2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        shopAuthRecord2 = shopAuthRecordDao
                .selectOne(Wrappers.<ShopAuthRecord>query().eq("txnId", txnIdSuccess));
        Assert.assertEquals(Status.PASS.getValue(), shopAuthRecord2.getStatus());
        List<Shop> shopList = shopDao.selectList(Wrappers.<Shop>emptyWrapper());
        logger.info("商铺的列表数量为: {}", shopList.size());
        // 用户查询店铺
        ShopPageQueryDTO shopQueryInfoDTO = new ShopPageQueryDTO();
        shopQueryInfoDTO.setCurrent(0);
        shopQueryInfoDTO.setSize(100);
        mockMvc.perform(MockMvcRequestBuilders.get("/system/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(GetJsonFromClass.toJson(shopQueryInfoDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        // 用户查询店铺详情
        Shop shop =  shopDao.selectOne(Wrappers.<Shop>query().eq("name", shopAuditDTO2.getName()));
        mockMvc.perform(MockMvcRequestBuilders.get("/personal/shop/{id}", shop.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAESUtilFunction() {
        String originNumber = "17888854568";
        String encryptNumber = AESUtil.encrypt(originNumber);
        Assert.assertEquals(originNumber, AESUtil.decrypt(encryptNumber));
    }

    @Ignore
    @Test
    public void testTestImageArcheive() {
        String url = imageRemoteService.queryUrl(1L);
        logger.info("image : {}", url);
        logger.info("image2: {}", imageRemoteService.queryUrl(10L));
        Assert.assertNotNull(url);
    }
}

