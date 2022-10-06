package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController.
 *
 * @author Thou
 * @date 2022/10/5
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/func1")
    public String func1() {
        return "func1 需要 admin 权限";
    }

    @GetMapping("/func2")
    public String func2() {
        return "func2 需要 user 权限";
    }

    @GetMapping("/func3")
    public String func3() {
        return "func3 允许匿名访问";
    }

    @GetMapping("/func4")
    public String func4() {
        return "func4 需要 admin 或者 user 权限";
    }
}
