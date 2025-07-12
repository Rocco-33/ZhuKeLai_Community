package com.jason.community.common;

import java.io.Serializable;

/**
 * 统一项目中所有 Ajax请求的返回结果（通常情况）
 *
 * @author Jason
 * @version 1.0
 */
public class ResultEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;


    // 请求处理结果 成功还是失败
    private boolean result;

    // 请求处理失败时返回的错误信息
    private String message;

    // 返回的数据
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    /**
     * 泛型方法1，请求处理成功且无返回数据，只返回结果的返回对象
     */
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<>(true, null, null);
    }

    /**
     * 泛型方法2，请求处理成功且有返回数据，则返回结果与数据的返回对象
     */
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<>(true, null, data);
    }

    /**
     * 泛型方法3，请求处理失败，则返回结果与错误信息的返回对象
     */
    public static <Type> ResultEntity<Type> failed(String message) {
        return new ResultEntity<>(false, message, null);
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
}
