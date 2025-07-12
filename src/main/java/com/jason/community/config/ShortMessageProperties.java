package com.jason.community.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信接口的自动配置参数
 *
 * @author Jason
 * @version 1.0
 */
@Component
@ConfigurationProperties(prefix = "short.message")
public class ShortMessageProperties {

    private String host;
    private String path;
    private String method;
    private String appcode;
    private String phoneNum;
    private String template_id;

    public ShortMessageProperties() {
    }

    public ShortMessageProperties(String host, String path, String appcode, String phoneNum, String method, String template_id) {
        this.host = host;
        this.path = path;
        this.appcode = appcode;
        this.phoneNum = phoneNum;
        this.method = method;
        this.template_id = template_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
}
