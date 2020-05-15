package com.example.demo.Service;

import com.example.demo.ServiceInterface.UploadService;
import com.example.demo.Tool.ZipTool;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public String uploadSource(MultipartFile file) throws Exception {
        String pathString = null;
        String jar_path = null;
        String dir_name = null;
        String dir_path = null;
        if(file!=null) {
            System.out.println(file.getOriginalFilename());
            pathString = "/root/upload/" + file.getOriginalFilename();
        }
        try {
            File files=new File(pathString);
            if(!files.getParentFile().exists()){
                files.getParentFile().mkdirs();
            }
            file.transferTo(files);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        jar_path = "/root/upload/" + file.getOriginalFilename().split("\\.")[0] + "/" + file.getOriginalFilename().split("\\.")[0] + ".jar";
        dir_name = file.getOriginalFilename().split("\\.")[0];
        dir_path = "/root/upload/" + file.getOriginalFilename().split("\\.")[0];
        ZipTool.zipUncompress(pathString , "/root/upload/");
        return "{\"code\":0,\"msg\":\""+jar_path+ "#"+ dir_name +"#" + dir_path +"\"}";
    }
}
