package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TokenController.
 *
 * @author Thou
 * @date 2022/9/26
 */
@Controller
@RequestMapping("/token")
public class TokenController {

    @RequestMapping("/one")
    @ResponseBody
    public Object createToken(HttpSession session) {
        Map<String, Object> token = new HashMap<>(2);
        token.put("token", UUID.randomUUID());
        session.setAttribute("token", token.get("token"));
        return token;
    }
}
