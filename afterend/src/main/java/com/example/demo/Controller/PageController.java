package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {
    @RequestMapping("/Hello")
    @ResponseBody
    public String hello() {
        System.out.println("已经调通");
        return "Hello";
    }

    @RequestMapping("/Start")
    public String fuck() {
        System.out.println("已经调通");
        return "index";
    }

    @RequestMapping("/Worker")
    public String test() {
        System.out.println("已经调通");
        return "worker";
    }

    @RequestMapping("/codeFeature")
    public String codeFeature() {
        return "qmy";
    }
}
