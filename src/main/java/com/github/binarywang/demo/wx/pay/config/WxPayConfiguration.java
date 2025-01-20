package com.github.binarywang.demo.wx.pay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Binary Wang
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WxPayConfiguration {
  private WxPayProperties properties;

  @Bean
  @ConditionalOnMissingBean
  public WxPayService wxService() {
    WxPayConfig payConfig = new WxPayConfig();
    payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppId()));//V3商户模式需要
    payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));//V3商户模式需要
    payConfig.setMchKey(StringUtils.trimToNull(this.properties.getMchKey()));
    payConfig.setSubAppId(StringUtils.trimToNull(this.properties.getSubAppId()));
    payConfig.setSubMchId(StringUtils.trimToNull(this.properties.getSubMchId()));
    payConfig.setKeyPath(StringUtils.trimToNull(this.properties.getKeyPath()));
    payConfig.setApiV3Key(StringUtils.trimToNull(this.properties.getApiV3Key()));//V3商户模式需要
    payConfig.setCertSerialNo(StringUtils.trimToNull(this.properties.getCertSerialNo()));//V3商户模式需要
    payConfig.setPrivateCertPath(StringUtils.trimToNull(this.properties.getPrivateCertPath()));//V3商户模式需要
    payConfig.setPrivateKeyPath(StringUtils.trimToNull(this.properties.getPrivateKeyPath()));//V3商户模式需要
    payConfig.setPublicKeyPath(StringUtils.trimToNull(this.properties.getPublicKeyPath()));//V3商户模式需要
    payConfig.setPublicKeyId(StringUtils.trimToNull(this.properties.getPublicKeyId()));//V3商户模式需要

    // 可以指定是否使用沙箱环境
    payConfig.setUseSandboxEnv(false);

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(payConfig);
    return wxPayService;
  }

}
