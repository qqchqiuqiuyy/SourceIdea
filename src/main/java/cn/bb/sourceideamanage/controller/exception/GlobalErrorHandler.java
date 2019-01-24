package cn.bb.sourceideamanage.controller.exception;

import cn.bb.sourceideamanage.common.exception.ErrorInfo;
import cn.bb.sourceideamanage.common.exception.ErrorInfoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalErrorHandler {
    /**
     * 错误信息页
     */
    private final static String DEFAULT_ERROR_VIEW = "/pages/common/error";

    /**
     * 错误信息构建器
     */
    @Autowired
    private ErrorInfoBuilder errorInfoBuilder;

    /**
     * 根据业务规则,统一处理异常。
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(HttpServletRequest request, Throwable error, Model model) {
        ErrorInfo errorInfo =  errorInfoBuilder.getErrorInfo(request, error);
        model.addAttribute("errTime",errorInfo.getTime());
        model.addAttribute("errUrl",errorInfo.getUrl());
        model.addAttribute("statusCode",errorInfo.getStatusCode());
        model.addAttribute("errorMsg",errorInfo.getError());
        return DEFAULT_ERROR_VIEW;
    }



}