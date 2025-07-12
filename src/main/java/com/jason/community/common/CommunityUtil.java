package com.jason.community.common;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.http.HttpResponse;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 住客来社区系统 通用工具方法
 *
 * @author Jason
 * @version 1.0
 */
public class CommunityUtil {

    /**
     * 调用第三方云服务存储服务接口，上传文件到 OSS服务器
     *
     * @param accessKeyId AccessKey ID
     * @param accessKeySecret AccessKey Secret
     * @param bucketName 服务器名
     * @param bucketDomain 服务器域名
     * @param endpoint 地域节点
     * @param originalName 要上传的文件的原始文件名
     * @param inputStream 要上传的文件的输入流
     * @return 包含上传结果以及上传的文件在 OSS上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String accessKeyId,
            String accessKeySecret,
            String bucketName,
            String bucketDomain,
            String endpoint,
            String originalName,
            InputStream inputStream
    ) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传的"文件目录"（根据日期划分）
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 使用 UUID 生成"文件主体名称"
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中 获取"文件扩展名"
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用 "文件目录"+"/"+"文件主体名称"+"文件扩展名" 拼接得到 "对象名称"
        String objectName = folderName + "/" + fileMainName + extensionName;
        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名：apple.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 检查 响应是否成功
            if(responseMessage == null) {
                // 使用 "域名"+"对象名称" 拼接得到 文件的访问路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 响应成功，返回 文件的访问路径
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 响应失败，返回错误消息
                return ResultEntity.failed("响应状态码=" + statusCode
                        + "错误消息=" + responseMessage.getErrorResponseAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 请求失败，返回错误信息
            return ResultEntity.failed(e.getMessage());

        } finally {
            if(ossClient != null) {
                // 关闭 OSSClient 连接
                ossClient.shutdown();
            }
        }
    }

    /**
     * 调用第三方云服务存储服务接口，从 OSS服务器 删除文件
     *
     * @param endpoint 地域节点
     * @param accessKeyId AccessKey ID
     * @param accessKeySecret AccessKey Secret
     * @param bucketName 服务器名
     * @param objectName 要删除的文件名
     */
    public static ResultEntity<String> deleteFileFromOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            String bucketName,
            String objectName
    ) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 调用 OSS 客户端对象的方法删除文件并获取响应结果数据
            ossClient.deleteObject(bucketName, objectName);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            // 请求失败，返回错误信息
            return ResultEntity.failed(e.getMessage());

        } finally {
            if(ossClient != null) {
                // 关闭 OSSClient 连接
                ossClient.shutdown();
            }
        }
    }

    /**
     * 调用第三方短信服务接口，发送短信验证码
     *
     * @param host          短信接口调用的 URL地址
     * @param path          地址的请求路径
     * @param method        请求方式
     * @param appcode       购买短信接口的商品编号
     * @param phoneNum      接收短信的手机号
     * @param template_id   短信模板编号
     *
     * @return 请求成功或失败结果
     */
    public static ResultEntity<String> sendShortMessage(
            String host,
            String path,
            String method,
            String appcode,
            String phoneNum,
            String template_id
    ) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        // 用于保存相关的参数
        Map<String, String> bodys = new HashMap<String, String>();

        // 生成验证码（固定：随机四位数）
        StringBuilder StringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            StringBuilder.append(random);
        }
        String code = StringBuilder.toString();

        // 要发送的验证码
        bodys.put("content", "code:" + code);
        // 接收短信的手机号
        bodys.put("phone_number", phoneNum);
        // 短信模板编号
        bodys.put("template_id", "TPL_" + template_id);
        try {
            // 执行调用
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            // 获取响应状态码（400:请求参数错误，403:套餐余额用完，500:服务器内部错误）
            int statusCode = response.getStatusLine().getStatusCode();

            // 检查 响应是否成功
            if (statusCode == 200) {
                // 响应成功，返回验证码
                return ResultEntity.successWithData(code);
            }
            // 响应失败，返回错误原因
            return ResultEntity.failed(response.getStatusLine().getReasonPhrase());

        } catch (Exception e) {
            // 请求失败，返回错误信息
            return ResultEntity.failed(e.getMessage());
        }
    }

}
