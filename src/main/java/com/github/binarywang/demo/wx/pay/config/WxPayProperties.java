package com.github.binarywang.demo.wx.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * wxpay pay properties.
 *
 * @author Binary Wang
 */
@Data
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {
  /**
   * 设置微信公众号或者小程序等的appid
   * (V3商户模式需要)
   */
  private String appId;

  /**
   * 微信支付商户号
   * (V3商户模式需要)
   */
  private String mchId;

  /**
   * 微信支付商户密钥
   */
  private String mchKey;

  /**
   * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
   */
  private String subAppId;

  /**
   * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
   */
  private String subMchId;

  /**
   * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
   */
  private String keyPath;

  /**
   * apiV3 秘钥值
   * (V3商户模式需要)
   */
  private String apiV3Key;

  /**
   * apiV3 证书序列号值
   * (V3商户模式需要)
   */
  private String certSerialNo;

  /**
   * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径.
   * (V3商户模式需要，这里用的是文件路径，可以用其它base64编码或字节数组参数替代)
   */
  private String privateCertPath;

  /**
   * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径.
   * (V3商户模式需要，这里用的是文件路径，可以用其它base64编码或字节数组参数替代)
   */
  private String privateKeyPath;

  /**
   * 微信支付公钥，pub_key.pem证书文件的绝对路径或者以classpath:开头的类路径.
   * (V3商户模式需要，这里用的是文件路径，可以用其它base64编码或字节数组参数替代，2024.08后的新商户验签需要用此公钥)
   */
  private String publicKeyPath;

  /**
   * 微信支付公钥ID
   * (V3商户模式需要，2024.08后的新商户验签需要用此公钥ID)
   */
  private String publicKeyId;

}
