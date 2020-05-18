package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.Dao.BugCollectDao;
import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Entity.*;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoadDataController {

    @Autowired
    BugInfoDao bugInfoDao;
    @Autowired
    BugCollectDao bugCollectDao;

//    @RequestMapping(value="/loaddata" , method = RequestMethod.GET)
//    @ResponseBody
//    public String getBug(@RequestParam("jarname")String jarname) {
////        System.out.println(jarname);
////        System.out.println(jarname.split("\\.")[0]);
//        jarname = jarname.split("\\.")[0];
////        System.out.println("进入" + jarname + " " + bugInfoDao.findBugsByJarLocation(jarname).size());
//        List<BugInfo> bugInfos = bugInfoDao.findBugsByJarLocation(jarname);
//        List<BugShow> bugShows = new ArrayList<>();
//        for (BugInfo bugInfo : bugInfos) {
//            bugShows.add(new BugShow(bugInfo));
//        }
//        ReturnValue value = new ReturnValue();
//        value.setCode(0);
//        value.setMsg("");
//        value.setCount(bugShows.size());
////        value.setData(bugShows);
//        String jsonString = JSON.toJSONString(value);
//        System.out.println(jsonString);
//        return jsonString;
//    }

    @RequestMapping(value="/loaddata" , method = RequestMethod.GET)
    @ResponseBody
    public String getBugs(@RequestParam("jarpath")String jarpath) {
//        jarpath = jarpath.replaceAll("/" , "\\\\");
        System.out.println("load data");
        List<BugCollect> bugCollects = bugCollectDao.findbugsbyjar(jarpath);
        ReturnValue value = new ReturnValue();
        value.setCode(0);
        value.setMsg("");
        value.setCount(bugCollects.size());
        value.setData(bugCollects);
        String jsonString = JSON.toJSONString(value);
        System.out.println(jsonString);
        return jsonString;
    }

    @RequestMapping(value="/getdata" , method = RequestMethod.GET)
    @ResponseBody
    public String getdata(@RequestParam("jarpath")String jarpath) {
        System.out.println("draw graph");
        System.out.println(jarpath);
        List<BugCollect> bugCollects = bugCollectDao.findbugsbyjar(jarpath);
        System.out.println(bugCollects.size());
        ReturnGraph returnGraph = new ReturnGraph();
        Map<String , Integer> map = new HashMap<>();
        for (BugCollect collect : bugCollects) {
            map.put(collect.getBugType() , map.getOrDefault(collect.getBugType() , 0) + 1);
        }
        returnGraph.setType(new ArrayList<>(map.keySet()));
        returnGraph.setCount(new ArrayList<>(map.values()));
        String jsonString = JSON.toJSONString(returnGraph);
        System.out.println(jsonString);
        return jsonString;
    }

    @RequestMapping(value="/getleveldata" , method = RequestMethod.GET)
    @ResponseBody
    public List<Integer> getleveldata(@RequestParam("jarpath")String jarpath) {
        System.out.println("draw level graph");
        System.out.println(jarpath);
        List<Integer> result = new ArrayList<>();
        result.add(bugCollectDao.findBugsByJarAndLevel(jarpath , 3).size());
        result.add(bugCollectDao.findBugsByJarAndLevel(jarpath , 2).size());
        result.add(bugCollectDao.findBugsByJarAndLevel(jarpath , 1).size());
        return result;
    }

}
