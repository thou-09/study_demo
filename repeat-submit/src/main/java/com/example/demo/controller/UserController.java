package com.example.demo.controller;

import com.example.demo.annotation.RepeatSubmit;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController.
 *
 * @author Thou
 * @date 2022/9/24
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = {"/login"})
    @ResponseBody
    @RepeatSubmit
    public String login(@RequestParam String username, @RequestParam String password) {
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        return "success";
    }
}
