package com.github.binarywang.demo.wxpay.controller;

import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Binary Wang
 */
@RestController
@RequestMapping("/pay")
public class WxPayController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxPayService wxService;

    /**
     * 关闭订单
     * @param orderId 订单号
     */
    @GetMapping("/closeOrder/{orderId}")
    public String closeOrder(@PathVariable String orderId) {
        try {
            WxPayOrderCloseResult orderCloseResult = this.wxService.closeOrder(orderId);
            return orderCloseResult.toString();
        } catch (WxErrorException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

}
