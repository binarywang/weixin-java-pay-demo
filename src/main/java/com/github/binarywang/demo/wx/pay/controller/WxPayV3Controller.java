package com.github.binarywang.demo.wx.pay.controller;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Response;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result.DecryptNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result.AppResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result.JsapiResult;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfigHolder;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Binary Wang
 */
@Api("微信支付V3部分接口示例")
@RestController
@RequestMapping("/pay/v3/{mchId}")
@AllArgsConstructor
@Slf4j
public class WxPayV3Controller {
    private WxPayService wxService;

    /**
     * <pre>
     * 查询订单
     * 详见https://pay.weixin.qq.com/doc/v3/merchant/4012791858)
     *
     * </pre>
     *
     * @param transactionId 微信订单号
     * @param outTradeNo    商户系统内部的订单号，当没提供transactionId时需要传这个。
     */
    @ApiOperation(value = "查询订单")
    @GetMapping("/queryOrder")
    public WxPayOrderQueryV3Result queryOrder(@PathVariable String mchId, @RequestParam(required = false) String transactionId, @RequestParam(required = false) String outTradeNo) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.queryOrderV3(transactionId, outTradeNo);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    @ApiOperation(value = "查询订单")
    @PostMapping("/queryOrder")
    public WxPayOrderQueryV3Result queryOrder(@PathVariable String mchId, @RequestBody WxPayOrderQueryV3Request wxPayOrderQueryV3Request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.queryOrderV3(wxPayOrderQueryV3Request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * <pre>
     * 关闭订单
     * 详见https://pay.weixin.qq.com/doc/v3/merchant/4012791860
     *
     * </pre>
     *
     * @param outTradeNo 商户系统内部的订单号
     */
    @ApiOperation(value = "关闭订单")
    @GetMapping("/closeOrder/{outTradeNo}")
    public void closeOrder(@PathVariable String mchId, @PathVariable String outTradeNo) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            this.wxService.closeOrderV3(outTradeNo);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    @ApiOperation(value = "关闭订单")
    @PostMapping("/closeOrder")
    public void closeOrder(@PathVariable String mchId, @RequestBody WxPayOrderCloseV3Request wxPayOrderCloseV3Request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            this.wxService.closeOrderV3(wxPayOrderCloseV3Request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * 调用统一下单接口(JSAPI)
     * 详见：https://pay.weixin.qq.com/doc/v3/merchant/4012791856
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrder")
    public JsapiResult createOrder(@PathVariable String mchId, @RequestBody WxPayUnifiedOrderV3Request request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.createOrderV3(TradeTypeEnum.JSAPI, request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * 调用统一下单接口(APP)
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrderApp")
    public AppResult createOrderApp(@PathVariable String mchId, @RequestBody WxPayUnifiedOrderV3Request request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.createOrderV3(TradeTypeEnum.APP, request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * <pre>
     * 微信支付-申请退款
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791862
     * </pre>
     *
     * @param request 请求对象
     * @return 退款操作结果
     */
    @ApiOperation(value = "退款")
    @PostMapping("/refund")
    public WxPayRefundV3Result refund(@PathVariable String mchId, @RequestBody WxPayRefundV3Request request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.refundV3(request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * <pre>
     * 微信支付-查询退款
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791863
     * </pre>
     *
     * @param outRefundNo 商户退款单号
     * @return 退款信息
     */
    @ApiOperation(value = "退款查询")
    @GetMapping("/refundQuery")
    public WxPayRefundQueryV3Result refundQuery(@PathVariable String mchId, @RequestParam("outRefundNo") String outRefundNo) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.refundQueryV3(outRefundNo);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    @ApiOperation(value = "退款查询")
    @PostMapping("/refundQuery")
    public WxPayRefundQueryV3Result refundQuery(@PathVariable String mchId, @RequestBody WxPayRefundQueryV3Request wxPayRefundQueryV3Request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        try {
            return this.wxService.refundQueryV3(wxPayRefundQueryV3Request);
        } catch (WxPayException e) {
            throw new RuntimeException(e);
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * <pre>
     * 支付成功回调
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791861
     * </pre>
     *
     * @param notifyData
     * @param request
     * @return
     */
    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/notify/order")
    public ResponseEntity<String> parseOrderNotifyResult(@PathVariable String mchId, @RequestBody String notifyData, HttpServletRequest request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        SignatureHeader header = getRequestHeader(request);
        WxPayNotifyV3Result res = null;
        try {
            res = this.wxService.parseOrderNotifyV3Result(notifyData, header);

            DecryptNotifyResult decryptRes = res.getResult();
            // TODO 根据自己业务场景需要构造返回对象
            if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(decryptRes.getTradeState())) {
                //成功返回200/204，body无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                //失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            //失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * <pre>
     * 支付成功回调
     * 详见 https://pay.weixin.qq.com/doc/v3/merchant/4012791865
     * </pre>
     *
     * @param notifyData
     * @param request
     * @return
     */
    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public ResponseEntity<String> parseRefundNotifyResult(@PathVariable String mchId, @RequestBody String notifyData, HttpServletRequest request) {
        if (!wxService.switchover(mchId)) {
            throw new IllegalArgumentException(String.format("未找到对应mchId=[%s]的配置，请核实！", mchId));
        }
        SignatureHeader header = getRequestHeader(request);
        WxPayRefundNotifyV3Result res = null;
        try {
            res = this.wxService.parseRefundNotifyV3Result(notifyData, header);

            WxPayRefundNotifyV3Result.DecryptNotifyResult decryptRes = res.getResult();
            // TODO 根据自己业务场景需要构造返回对象
            if (WxPayConstants.RefundStatus.SUCCESS.equals(decryptRes.getRefundStatus())) {
                //成功返回200/204，body无需有内容
                return ResponseEntity.status(200).body("");
            } else {
                //失败返回4xx或5xx，且需要构造body信息
                return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
            }
        } catch (WxPayException e) {
            //失败返回4xx或5xx，且需要构造body信息
            return ResponseEntity.status(500).body(WxPayNotifyV3Response.fail("错误原因"));
        } finally {
            WxPayConfigHolder.remove();
        }
    }

    /**
     * 组装请求头重的前面信息
     *
     * @param request
     * @return
     */
    private SignatureHeader getRequestHeader(HttpServletRequest request) {
        // 获取通知签名
        String signature = request.getHeader("Wechatpay-Signature");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");

        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(signature);
        signatureHeader.setNonce(nonce);
        signatureHeader.setSerial(serial);
        signatureHeader.setTimeStamp(timestamp);
        return signatureHeader;
    }


}
