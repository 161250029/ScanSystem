package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.Entity.ReturnXmlPathInfo;
import com.example.demo.ServiceInterface.AutoscancodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AutoScanCodeContoller {
    @Autowired
    private AutoscancodeService autoscancodeService;

    @RequestMapping(value="/autoscan" , method = RequestMethod.GET)
    @ResponseBody
    public String Scan(@RequestParam("jarpath") String jarpath) {
        System.out.println("进来了");
        ReturnXmlPathInfo returnXmlPathInfo = autoscancodeService.ScanUploadJar(jarpath);
        return JSON.toJSONString(returnXmlPathInfo);
    }
}
