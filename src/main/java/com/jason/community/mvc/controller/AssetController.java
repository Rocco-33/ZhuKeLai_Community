package com.jason.community.mvc.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.github.pagehelper.Page;
import com.jason.community.common.CommunityConstant;
import com.jason.community.common.PageResultEntity;
import com.jason.community.common.ResultEntity;
import com.jason.community.config.PayProperties;
import com.jason.community.entity.Asset;
import com.jason.community.mvc.service.api.AssetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Jason
 * @version 1.0
 */
@PreAuthorize("hasAuthority('asset:write')")
@Controller
public class AssetController {

    @Resource //装配 支付宝接口参数类
    private PayProperties payProperties;
    
    @Resource
    private AssetService assetService;

    /**
     * 生成订单
     */
    @PreAuthorize("hasAuthority('asset:read')")
    @ResponseBody //需要将页面直接作为响应体显示在页面
    @RequestMapping("/asset/generate/order")
    public String generateOrder(Integer id, HttpSession session) throws AlipayApiException {

        ResultEntity<Asset> resultEntity = getAssetById(id);
        Asset asset = resultEntity.getData();

        // 1.生成订单号
        String currentTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userUID = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String orderNum = currentTimeStamp + userUID;

        // 2.设置订单号
        asset.setOrderNum(orderNum);

        // 3.将资产对象保存到 session 域
        session.setAttribute(CommunityConstant.ATTR_NAME_ASSET, asset);

        String form = "";
        form = sendRequestToAliPay(orderNum, asset.getOrderAmount(), asset.getName(), asset.getDescription());

        // 4.向支付宝接口发送请求
        return form;
    }

    /**
     * 封装的支付宝接口方法
     *
     * @param outTradeNo  商户订单号
     * @param totalAmount 付款金额
     * @param subject     订单名称（资产名称）
     * @param body        商品描述（资产名称）
     * @return            返回显示支付宝的登录页面
     */
    private String sendRequestToAliPay(String outTradeNo, Double totalAmount, String subject, String body) throws AlipayApiException {

        // 1.获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                payProperties.getGatewayUrl(),
                payProperties.getAppId(),
                payProperties.getMerchantPrivateKey(),
                "json",
                payProperties.getCharset(),
                payProperties.getAlipayPublicKey(),
                payProperties.getSignType()
        );
        // 2.设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(payProperties.getNotifyUrl());

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                + "\"total_amount\":\""+ totalAmount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();
            // 调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    /**
     * 支付宝服务端要调用的返回结果方法
     */
    @ResponseBody
    @RequestMapping("/asset/ali/pay/return")
    public String returnUrlMethod(HttpServletRequest request,HttpServletResponse response, HttpSession session) throws AlipayApiException, IOException {

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                payProperties.getSignType()
        );
        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            // 返回的结果信息

            // 商户订单号
            String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            // 支付宝交易号
            String payOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            // 付款金额
            String orderAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            // 支付完成后 将订单保存到数据库

            // 1.从session域获取资产对象
            Asset asset = (Asset) session.getAttribute("asset");

            asset.setPayNum(payOrderNum); // 交易号
            asset.setOrderStatus("1"); // 订单状态
            asset.setPurchaseTime(new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date()));

            // 3.更改资产对象
            assetService.updateAsset(asset);

            response.sendRedirect("http://zhukelai.natapp1.cc/asset_list.html");

            return "success";
        }else {
            // 页面显示信息：验签失败
            return "验签失败";
        }
        //——请在这里编写您的程序（以上代码仅作参考）——
    }

    /**
     * 浏览器发送请求给支付宝服务器，支付宝服务器返回页面
     */
//    @RequestMapping("/asset/ali/pay/notify")
//    public void notifyUrlMethod(HttpServletRequest request) throws IOException, AlipayApiException {
//
//        //获取支付宝POST过来反馈信息
//        Map<String,String> params = new HashMap<String,String>();
//        Map<String,String[]> requestParams = request.getParameterMap();
//        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
//        //调用SDK验证签名
//        boolean signVerified = AlipaySignature.rsaCheckV1(
//                params,
//                payProperties.getAlipayPublicKey(),
//                payProperties.getCharset(),
//                payProperties.getSignType()
//        );
//
//        //——请在这里编写您的程序（以下代码仅作参考）——
//
//        /* 实际验证过程建议商户务必添加以下校验：
//        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
//        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
//        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
//        4、验证app_id是否为该商户本身。
//        */
//        if(signVerified) {//验证成功
//
//            //商户订单号
//            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
//            //支付宝交易号
//            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
//            //交易状态
//            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
//
//
//        }else {//验证失败
//            //调试用，写文本函数记录程序运行情况是否正常
//            //String sWord = AlipaySignature.getSignCheckContentV1(params);
//            //AlipayConfig.logResult(sWord);
//            System.out.println("验证失败");
//        }
//        //——请在这里编写您的程序（以上代码仅作参考）——
//    }

    /**
     * 获取所有查询到的资产信息
     */
    @ResponseBody
    @RequestMapping("/asset/get/all/by/search")
    public PageResultEntity<Page<Asset>> getAllAssetBySearch(@RequestBody Map<String, Object> searchMap) {
        try {
            // 执行查询业务
            Page<Asset> AssetPage = assetService.getAllAssetBySearch(searchMap);
            return PageResultEntity.successWithData(AssetPage, AssetPage.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            return PageResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 添加资产信息
     */
    @ResponseBody
    @RequestMapping("/asset/add")
    public ResultEntity<String> addAsset(@RequestBody Asset asset) {
        try {
            // 执行添加业务
            assetService.addAsset(asset);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 修改资产信息
     */
    @ResponseBody
    @RequestMapping("/asset/update")
    public ResultEntity<String> updateAsset(@RequestBody Asset asset) {
        try {
            // 执行修改业务
            assetService.updateAsset(asset);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 根据 id 获取资产信息
     */
    @ResponseBody
    @RequestMapping("/asset/get/by/id")
    public ResultEntity<Asset> getAssetById(Integer id) {
        try {
            // 执行查询业务
            Asset asset = assetService.getAssetById(id);
            return ResultEntity.successWithData(asset);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 删除资产信息
     */
    @ResponseBody
    @RequestMapping("/asset/delete")
    public ResultEntity<String> deleteByIds(@RequestBody List<Integer> ids) {
        try {
            // 执行删除业务
            assetService.deleteByIds(ids);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
