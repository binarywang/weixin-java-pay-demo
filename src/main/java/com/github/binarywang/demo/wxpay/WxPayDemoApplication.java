package com.github.binarywang.demo.wxpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author Binary Wang
 */
@SpringBootApplication
public class WxPayDemoApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WxPayDemoApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WxPayDemoApplication.class, args);
    }
}
