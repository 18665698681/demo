/*
Author : lijiangtao
Date   : 2019-02-25
*/
CREATE TABLE `finance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `txnId` varchar(50)  NOT NULL,
  `name` varchar(255)  NOT NULL COMMENT '联系人名称',
  `mobile` varchar(50) NOT NULL COMMENT '手机号',
  `type` tinyint(4) NOT NULL COMMENT '1-金融 2-借换',
  `other` varchar(255)  NOT NULL DEFAULT '' COMMENT '其他需求',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `lastModifyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='金融-借换货信息表';

/**
Author : lijiangtao
Date   ：2019-03-08
 */
CREATE TABLE `product_quote` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `txnId` varchar(50)  NOT NULL COMMENT '业务唯一标识',
  `productId` bigint(20) NOT NULL COMMENT '商品ID',
  `price` decimal(18,2) NOT NULL COMMENT '报价',
  `amount` int(11) NOT NULL COMMENT '报价数量',
  `userId` bigint(20) NOT NULL COMMENT '报价用户ID',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `lastModifyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`)
) COMMENT='采购报价表';