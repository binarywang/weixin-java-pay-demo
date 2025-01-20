package com.github.binarywang.demo.wx.pay.config;

import com.github.binarywang.demo.wx.pay.config.WxPayProperties.Config;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

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
    List<Config> configs = this.properties.getConfigs();
    if (configs == null) {
      throw new WxRuntimeException("先添加下相关配置，注意别配错了！");
    }

    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setMultiConfig(
      configs.stream().map(a -> {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(a.getAppId()));//V3商户模式需要
        payConfig.setMchId(StringUtils.trimToNull(a.getMchId()));//V3商户模式需要
        payConfig.setMchKey(StringUtils.trimToNull(a.getMchKey()));
        payConfig.setSubAppId(StringUtils.trimToNull(a.getSubAppId()));
        payConfig.setSubMchId(StringUtils.trimToNull(a.getSubMchId()));
        payConfig.setKeyPath(StringUtils.trimToNull(a.getKeyPath()));
        payConfig.setApiV3Key(StringUtils.trimToNull(a.getApiV3Key()));//V3商户模式需要
        payConfig.setCertSerialNo(StringUtils.trimToNull(a.getCertSerialNo()));//V3商户模式需要
        payConfig.setPrivateCertPath(StringUtils.trimToNull(a.getPrivateCertPath()));//V3商户模式需要
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(a.getPrivateKeyPath()));//V3商户模式需要
        payConfig.setPublicKeyPath(StringUtils.trimToNull(a.getPublicKeyPath()));//V3商户模式需要
        payConfig.setPublicKeyId(StringUtils.trimToNull(a.getPublicKeyId()));//V3商户模式需要
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        return payConfig;
      }).collect(Collectors.toMap(WxPayConfig::getMchId, a -> a, (o, n) -> o)));
    return wxPayService;
  }

}
