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
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Binary Wang
 */
@Api("微信支付V3部分接口示例")
@RestController
@RequestMapping("/pay/v3")
@AllArgsConstructor
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
    public WxPayOrderQueryV3Result queryOrder(@RequestParam(required = false) String transactionId,
                                              @RequestParam(required = false) String outTradeNo)
            throws WxPayException {
        return this.wxService.queryOrderV3(transactionId, outTradeNo);
    }

    @ApiOperation(value = "查询订单")
    @PostMapping("/queryOrder")
    public WxPayOrderQueryV3Result queryOrder(@RequestBody WxPayOrderQueryV3Request wxPayOrderQueryV3Request) throws WxPayException {
        return this.wxService.queryOrderV3(wxPayOrderQueryV3Request);
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
    public void closeOrder(@PathVariable String outTradeNo) throws WxPayException {
        this.wxService.closeOrderV3(outTradeNo);
    }

    @ApiOperation(value = "关闭订单")
    @PostMapping("/closeOrder")
    public void closeOrder(@RequestBody WxPayOrderCloseV3Request wxPayOrderCloseV3Request) throws WxPayException {
        this.wxService.closeOrderV3(wxPayOrderCloseV3Request);
    }

    /**
     * 调用统一下单接口(JSAPI)
     * 详见：https://pay.weixin.qq.com/doc/v3/merchant/4012791856
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrder")
    public JsapiResult createOrder(@RequestBody WxPayUnifiedOrderV3Request request) throws WxPayException {
        return this.wxService.createOrderV3(TradeTypeEnum.JSAPI, request);
    }

    /**
     * 调用统一下单接口(APP)
     */
    @ApiOperation(value = "统一下单，并组装所需支付参数")
    @PostMapping("/createOrderApp")
    public AppResult createOrderApp(@RequestBody WxPayUnifiedOrderV3Request request) throws WxPayException {
        return this.wxService.createOrderV3(TradeTypeEnum.APP, request);
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
    public WxPayRefundV3Result refund(@RequestBody WxPayRefundV3Request request) throws WxPayException {
        return this.wxService.refundV3(request);
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
    public WxPayRefundQueryV3Result refundQuery(@RequestParam("outRefundNo") String outRefundNo)
            throws WxPayException {
        return this.wxService.refundQueryV3(outRefundNo);
    }

    @ApiOperation(value = "退款查询")
    @PostMapping("/refundQuery")
    public WxPayRefundQueryV3Result refundQuery(@RequestBody WxPayRefundQueryV3Request wxPayRefundQueryV3Request) throws WxPayException {
        return this.wxService.refundQueryV3(wxPayRefundQueryV3Request);
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
     * @throws WxPayException
     */
    @ApiOperation(value = "支付回调通知处理")
    @PostMapping("/notify/order")
    public ResponseEntity<String> parseOrderNotifyResult(@RequestBody String notifyData, HttpServletRequest request) {
        SignatureHeader header = getRequestHeader(request);
        try {
            WxPayNotifyV3Result res = this.wxService.parseOrderNotifyV3Result(notifyData, header);
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
     * @throws WxPayException
     */
    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/notify/refund")
    public ResponseEntity<String> parseRefundNotifyResult(@RequestBody String notifyData, HttpServletRequest request) {
        SignatureHeader header = getRequestHeader(request);
        try {
            WxPayRefundNotifyV3Result res = this.wxService.parseRefundNotifyV3Result(notifyData, header);
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
