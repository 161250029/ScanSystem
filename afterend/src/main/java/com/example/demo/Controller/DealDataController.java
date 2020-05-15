package com.example.demo.Controller;

import com.example.demo.ServiceInterface.SaveBugInfosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class DealDataController {

    @Autowired
    private SaveBugInfosService saveBugInfosService;

    @RequestMapping(value="/dealdata" , method = RequestMethod.GET)
    @ResponseBody
    public void dealdata(@RequestParam("spot_path") String spot_path , @RequestParam("find_path") String find_path , @RequestParam("dirpath") String dirpath , @RequestParam("jarpath") String jarpath) throws Exception {
        System.out.println("start deal data");
        saveBugInfosService.dealdata(spot_path , find_path , dirpath , jarpath);
    }
}
