package com.example.demo.Service;

import com.example.demo.Entity.ReturnXmlPathInfo;
import com.example.demo.ServiceInterface.AutoscancodeService;
import com.example.demo.Tool.CMDTool;
import com.example.demo.Tool.DirectoryTool;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class AutoScanCodeServiceImpl implements AutoscancodeService {

    @Override
    public ReturnXmlPathInfo ScanUploadJar(String JarPath) {
        try {
//            System.out.println(jarpath);
            String spot_path = "/root/upload/spot.xml";
            String find_path = "/root/upload/find.xml";
            CMDTool.Shell_execute(
                    "/root/findsecbugs-cli-1.10.1/findsecbugs.sh -progress -high -xml -output " + find_path + " " + JarPath);
            CMDTool.Shell_execute("/root/spotbugs-4.0.0-RC2/bin/spotbugs -textui -progress -output " + spot_path +" " + JarPath);
            ReturnXmlPathInfo pathInfo = new ReturnXmlPathInfo(spot_path , find_path);
            return pathInfo;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void ScanCode(String save_path) {
        ArrayList<String> JarPaths = DirectoryTool.getJarFiles("D:\\ChromeCoreDownloads\\Juliet_Test_Suite_v1.3_for_Java\\Java\\src\\testcases");
        for (String jarpath : JarPaths) {
            try {
                CMDTool.cmdC("D: && cd D:\\ChromeCoreDownloads\\findsecbugs-cli-1.10.1 && findsecbugs.bat -progress -high -xml -output " + save_path + "\\" +jarpath.split("\\\\")[jarpath.split("\\\\").length - 1].split("\\.")[0] + ".xml " + jarpath);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
