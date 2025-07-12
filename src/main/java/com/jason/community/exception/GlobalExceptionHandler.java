package com.jason.community.exception;

import com.jason.community.common.CommunityConstant;
import com.jason.community.common.ResultEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常处理器
 *
 * @author Jason
 * @version 1.0
 */
@ControllerAdvice // 标识为异常处理器
public class GlobalExceptionHandler {

    /**
     * 该方法是异常处理方法的模板
     *
     * @param exception 实际捕获到的异常类型
     * @param request 当前请求对象
     * @param response 当前响应对象
     * @param viewName 需要跳转的视图名称
     * @return modelAndView
     */
    private ModelAndView commonResolve(Exception exception, HttpServletRequest request, HttpServletResponse response, String viewName) throws IOException {
        // 如果不是Ajax请求，则将exception对象存入模型model
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(CommunityConstant.ATTR_NAME_EXCEPTION, exception);
        // 设置对应的视图名称view
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    // 标识处理异常的方法
    @ExceptionHandler(NullPointerException.class) // 处理空指针异常
    public ModelAndView resolveNullPointerException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "system_exception";
        return commonResolve(exception, request, response, viewName);
    }

    @ExceptionHandler(LoginFailedException.class) // 处理登录失败异常
    public ResultEntity<String> resolveLoginFailedException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResultEntity.failed(exception.getMessage());
    }

    // 标识处理异常的方法
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultEntity<String> error(Exception e){
        e.printStackTrace();
        return ResultEntity.failed(e.getMessage());
    }
}
