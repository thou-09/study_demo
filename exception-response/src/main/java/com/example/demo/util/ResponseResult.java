package com.example.demo.util;

import com.example.demo.constant.StatusCodeMsgEnum;
import com.example.demo.exception.AppException;

import java.io.Serializable;

/**
 * ResponseResult.<br>
 * 自定义响应对象
 *
 * @author Thou
 * @date 2022/10/6
 */
@SuppressWarnings("unused")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1997384422946860831L;
    /**
     * 响应状态码
     */
    private String statusCode;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    private ResponseResult() {}

    private ResponseResult(String status, String message, T data) {
        this.statusCode = status;
        this.message = message;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    /**
     * 成功响应
     *
     * @return com.itany.netclass.util.ResponseResult
     * @author Thou
     * @date 2022/10/6
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(StatusCodeMsgEnum.SUCCESS.getCode(), StatusCodeMsgEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功响应，包含响应数据
     *
     * @param data 响应数据
     * @return com.itany.netclass.util.ResponseResult
     * @author Thou
     * @date 2022/10/6
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(StatusCodeMsgEnum.SUCCESS.getCode(), StatusCodeMsgEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 失败响应，包含错误状态码和响应信息
     *
     * @param e AppException
     * @return com.itany.netclass.util.ResponseResult
     * @author Thou
     * @date 2022/10/6
     */
    public static <T> ResponseResult<T> fail(AppException e) {
        return new ResponseResult<>(e.getCode(), e.getMessage(), null);
    }

    /**
     * 失败响应，自定义错误状态码、响应信息
     *
     * @param status 错误状态码
     * @param message 响应信息
     * @return com.itany.netclass.util.ResponseResult
     * @author Thou
     * @date 2022/10/6
     */
    public static <T> ResponseResult<T> fail(String status, String message) {
        return new ResponseResult<>(status, message, null);
    }
}
