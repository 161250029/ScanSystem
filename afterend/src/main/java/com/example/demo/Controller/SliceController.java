package com.example.demo.Controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.ServiceInterface.DoSlicerService;
import com.example.demo.Tool.FileTool;
import com.example.demo.Tool.WalaSlicerTool;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class SliceController {
    @Autowired
    private DoSlicerService doSlicerService;
    @RequestMapping(value="/doslice" , method = RequestMethod.GET)
    @ResponseBody
    public String doslice(@RequestParam("jarpath") String jarpath , @RequestParam("methodname")String methodName , @RequestParam("classname")String className , @RequestParam("linenumber")int lineNumber ) throws WalaException, CancelException, InvalidClassFileException, IOException {
        return doSlicerService.doslice(jarpath , methodName , className , lineNumber);
    }

    @RequestMapping(value="/showslice" , method = RequestMethod.GET)
    @ResponseBody
    public String showslice(@RequestParam("jarpath") String jarpath , @RequestParam("methodname")String methodName , @RequestParam("classname")String className , @RequestParam("linenumber")int lineNumber , @RequestParam("filepath") String filepath) throws WalaException, CancelException, InvalidClassFileException, IOException {
        Set<Integer> silces = WalaSlicerTool.doSlicing(jarpath , methodName , className , lineNumber);
        FileTool fileTool = new FileTool(filepath);
        System.out.println(filepath);
        List<String> result = new ArrayList<>();
        for (Integer i : silces)
            if (i < fileTool.Lines.size()) {
                result.add(fileTool.Lines.get(i -1));
                System.out.println(fileTool.Lines.get(i -1));
                System.out.println(fileTool.Lines.size());
            }
        return JSON.toJSONString(result);
    }
}
