package cn.bb.sourceideamanage.common.exception;

import lombok.Data;
import lombok.ToString;

/**
 * 异常信息类
 * @author
 */
@Data
@ToString
public class ErrorInfo {
    /**
     * 发生时间
     */
    private String time;
    /**
     * 访问Url
     */
    private String url;
    /**
     * 错误类型
     */
    private String error;
    /**
     * 错误的堆栈轨迹
     */
    String stackTrace;
    /**
     * 状态码
     */
    private int statusCode;
    /**
     * 状态码-描述
     */
    private String reasonPhrase;


}
