package com.example.demo.exception.handler;

import com.example.demo.exception.AppException;
import com.example.demo.util.ResponseResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * GlobalExceptionHandler.
 *
 * @author Thou
 * @date 2022/10/6
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseResult handlerException(Exception e) {
        return ResponseResult.fail("50000", "服务器繁忙");
    }

    @ExceptionHandler({AppException.class})
    private ResponseResult handlerAppException(AppException e) {
        return ResponseResult.fail(e);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ResponseResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (Objects.nonNull(fieldError)) {
                message = fieldError.getDefaultMessage();
            }
        }
        return ResponseResult.fail("50001", message);
    }
}
