package com.example.demo.exception;

import com.example.demo.constant.StatusCodeMsgEnum;
import org.springframework.lang.NonNull;

/**
 * AppException.
 *
 * @author Thou
 * @date 2022/10/6
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 4989359437827815324L;

    private String code;
    private String message;

    public AppException(@NonNull StatusCodeMsgEnum statusCodeMsg) {
        this.code = statusCodeMsg.getCode();
        this.message = statusCodeMsg.getMessage();
    }

    public AppException(@NonNull String code, @NonNull String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
