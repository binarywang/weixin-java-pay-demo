#### 本Demo基于Spring Boot构建，实现微信支付开发功能。

[![Build Status](https://travis-ci.org/binarywang/weixin-java-pay-demo.svg?branch=master)](https://travis-ci.org/binarywang/weixin-java-pay-demo)
-----------------------

## 使用步骤：
1. 配置：复制`/src/main/resources/application.yml.template` 生成application.yml文件，并根据自己需要填写相关配置，其中支付相关参数含义请参考WxPayProperties类中的注释（需要注意的是：yml文件内的属性冒号后面的文字之前需要加空格，可参考已有配置，否则属性会设置不成功）；	
1. 其中各参数的含义请参考 /src/main/java/com/github/binarywang/demo/wxpay/config/WxPayProperties.java 文件里的注释；
1. 需要特别注意的，是sub开头的两个参数属于服务商模式使用的，如果是普通模式，请不要配置这两个参数，最好从配置文件中移除相关项；
1. 查看`com.github.binarywang.demo.wxpay.controller.WxPayController`类代码，根据自己需要修改调整相关实现；
1. 运行Java程序：`com.github.binarywang.demo.wxpay.WxPayDemoApplication`；
1. 打开网页浏览器，输入想要访问的地址，比如`http://localhost:8080/pay/closeOrder/123`查看效果。
	
