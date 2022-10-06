package com.example.demo.constant;

/**
 * 响应状态码枚举类
 *
 * @author Thou
 * @date 2022/10/6
 */
public enum StatusCodeMsgEnum {
    /**
     * Response 响应状态码和提示信息
     */
    SUCCESS("00000", "成功"),

    USER_NOT_EXISTS("10001", "用户不存在"),
    USERNAME_AND_PASSWORD_ERROR("10002", "用户名或密码错误"),
    ;

    /**
     * 状态码
     */
    private final String statusCode;

    /**
     * 提示信息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param statusCode 状态码
     * @param message 提示信息
     * @author Thou
     * @date 2022/10/6
     */
    StatusCodeMsgEnum(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * 获取状态码
     *
     * @return java.lang.String
     * @author Thou
     * @date 2022/10/6
     */
    public String getCode() {
        return statusCode;
    }

    /**
     * 获取提示信息
     *
     * @return java.lang.String
     * @author Thou
     * @date 2022/10/6
     */
    public String getMessage() {
        return message;
    }
}
