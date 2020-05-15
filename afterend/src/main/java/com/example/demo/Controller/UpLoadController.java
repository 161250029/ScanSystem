package com.example.demo.Controller;

import com.example.demo.ServiceInterface.UploadService;
import com.example.demo.Tool.ZipTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


@Controller
public class UpLoadController {
    @Autowired
    private UploadService uploadService;
    @RequestMapping(value="/upload" , method = RequestMethod.POST)
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile file , HttpServletRequest request) throws Exception {
        System.out.println("已调通");
        return uploadService.uploadSource(file);
    }

}
