package com.dtlonline.shop.utilities;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * The type Oss client.
 * @author xwl && Deveik
 */
public class OssClientUtility {
    private Logger logger = LoggerFactory.getLogger(OssClientUtility.class);

    private static final String ACCESSKEYID = "LTAI5Dkof5mdPH8B";

    private static final String ACCESSKEYSECRET = "LJT60b9s0l59iyHScn4MnmAcMurpXj";

    /**
     * Bucket名称
     */
    private static final String BUCKETNAME = "dtl2019";

    /**
     * 资源上传地址
     */
    private static final String ENDPOINT = "http://img.dtlonline.com/";

    /**
     * 后台需要用到oss的地方有哪些？
     * 一般来说独立开，然后获取的应该是url 后端应该持有资源的引用
     * 而且这里肯定也是做客户的持有和上传限制，不然的话有心的人可以进行资源损耗
     * 肯定有，但是这里应该是由服务端这边返回某个标志用于记录这个用户是谁的信息给前台吧，大概吧
     * 怎么返回一个标志给到后台呢？ram账号的生成？
     */
    public void testOssListObject(){
        try(MyOssClient myOssClient = new MyOssClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET)){
            ObjectListing objectListing = myOssClient.listObjects(BUCKETNAME);
            for(OSSObjectSummary ossObjectSummary : objectListing.getObjectSummaries()){
                logger.info("测试输出\nkey:\t{}\nowner:\t{}\nsize:\t{}\nlast_mod:\t{}",
                        ossObjectSummary.getKey(), ossObjectSummary.getOwner(),
                        ossObjectSummary.getSize(), ossObjectSummary.getLastModified());
            }
        }
    }



    class MyOssClient extends OSSClient implements Closeable {
        public MyOssClient(String endpoint, String accessKeyId, String secretAccessKey) {
            super(endpoint, accessKeyId, secretAccessKey);
        }

        @Override
        public void close() {
            this.shutdown();
        }
    }
}
