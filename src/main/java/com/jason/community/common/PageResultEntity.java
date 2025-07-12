package com.jason.community.common;

import java.io.Serializable;

/**
 * 统一项目中所有 Ajax请求的返回结果（分页情况）
 *
 * @author Jason
 * @version 1.0
 */
public class PageResultEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 请求处理结果 成功还是失败
    private boolean result;

    // 请求处理失败时 返回的错误信息
    private String message;

    // 返回的数据
    private T data;

    // 分页的总页数
    private Long total;

    public PageResultEntity() {
    }

    public PageResultEntity(boolean result, String message, T data, Long total) {
        this.result = result;
        this.message = message;
        this.data = data;
        this.total = total;
    }


    /**
     * 泛型方法 1：请求处理成功且有返回数据，则返回结果与数据的返回对象
     */
    public static <Type> PageResultEntity<Type> successWithData(Type data, Long total) {
        return new PageResultEntity<Type>(true, null, data, total);
    }

    /**
     * 泛型方法 2：请求处理失败，则返回结果与错误信息的返回对象
     */
    public static <Type> PageResultEntity<Type> failed(String message) {
        return new PageResultEntity<Type>(false, message, null, null);
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
