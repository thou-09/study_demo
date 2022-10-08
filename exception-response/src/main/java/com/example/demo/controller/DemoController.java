package com.example.demo.controller;

import com.example.demo.constant.StatusCodeMsgEnum;
import com.example.demo.exception.AppException;
import com.example.demo.util.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * DemoController.
 *
 * @author Thou
 * @date 2022/10/6
 */
@RestController
@SuppressWarnings("all")
public class DemoController {

    @GetMapping("/func1")
    public ResponseResult func1() {
        return ResponseResult.success();
    }

    @GetMapping("/func2")
    public ResponseResult func2() {
        throw new AppException(StatusCodeMsgEnum.USER_NOT_EXISTS);
    }

    @GetMapping("/func3")
    public ResponseResult func3() {
        throw new AppException(StatusCodeMsgEnum.USERNAME_AND_PASSWORD_ERROR);
    }

    @GetMapping("/func4")
    public ResponseResult func4() {
        int i = 1 / 0;
        return ResponseResult.success();
    }

    @GetMapping("/func5")
    public ResponseResult<Map> fucn5() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", "tom");
        map.put("2", "jerry");
        return ResponseResult.success(map);
    }
}
