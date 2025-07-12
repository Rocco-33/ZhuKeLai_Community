package com.jason.community.common;

/**
 * 提示信息常量类
 *
 * @author Jason
 * @version 1.0
 */
public class CommunityConstant {

    public static final String REDIS_CODE_PREFIX = "REDIS_CODE_PREFIX"; // redis数据 手机验证码前缀

    //--------------------------- 操作消息提示信息 ---------------------------------------
    public static final String DELETE_IDS_IS_NULL = "请至少选择一项数据进行删除！";

    //--------------------------- 系统提示信息 -------------------------------------------
    public static final String SYSTEM_BUSY = "系统繁忙，请求稍后重试！";
    public static final String SEARCH_MAP_IS_NULL = "系统异常，传递参数为空！";

    public static final String MESSAGE_LOGIN_FAILED = "账号密码错误，请重新输入！";
    public static final String MESSAGE_LOGIN_ACCT_ALREADY_INUSE = "该账号已被使用，请重新输入！";

    public static final String MESSAGE_ACCESS_DENIED = "权限不足，无法访问！";
    public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录后再访问！";

    public static final String MESSAGE_STRING_INVALIDATE = "字符串不合法，不要传入空字符串！";

    public static final String MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE = "系统异常，登录账号不唯一！";

    public static final String MESSAGE_CODE_NOT_EXIST = "验证码过期！请检查手机号并重新发送！";
    public static final String MESSAGE_CODE_INVALID = "验证码不正确！";

    //---------------------------文件上传提示信息-----------------------------------------
    public static final String FILE_NO_SELECTED = "未选择上传的文件,请求选择后上传!";
    public static final String FILE_NO_WRITE_PERMISSION = "上传目录没有写权限!";
    public static final String FILE_INCORRECT_NAME = "目录名不正确!";
    public static final String FILE_SIZE_EXCEEDS_LIMIT = "上传文件大小超过限制!";
    public static final String FILE_TYPE_ERROR = "文件类型错误，只允许上传JPG/PNG/JPEG/GIF等图片类型的文件!";

    //--------------------------- session属性名 -------------------------------------------
    public static final String ATTR_NAME_EXCEPTION = "exception";
    public static final String ATTR_NAME_LOGIN_ADMIN = "admin";
    public static final String ATTR_NAME_ASSET = "asset";
}
