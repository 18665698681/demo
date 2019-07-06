package com.dtlonline.api.shop.command;

import io.alpha.app.core.base.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO extends BaseObject {

	/**
	 * 账号
	 */
	private String account;
	/**
	 * 昵称
	 */
	private String nikeName;
	/**
	 * 实名认证
	 */
	private Boolean personalCertificate;
	/**
	 * 企业认证
	 */
	private Boolean enterpriseCertification;
	/**
	 * 实地认证
	 */
	private Boolean fieldCertification;

}