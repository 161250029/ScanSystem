package com.example.demo.Service;

import com.example.demo.Dao.VulnerabilityDao;
import com.example.demo.Entity.Vulnerability;
import com.example.demo.ServiceInterface.VulnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class VulnerServiceImpl implements VulnerService {

    @Autowired
    private VulnerabilityDao vulDao;

    public List<List<String>> datalist = new ArrayList<>();

    @Override
    public void readDataset(String path) {
        File filedir = new File(path);
        getFileList(filedir);
        List<Vulnerability> samples = new ArrayList<Vulnerability>();
        for(List<String> bugs : datalist){
            Long id = Long.valueOf(bugs.get(0));
            int threatlevel = Integer.valueOf(bugs.get(2));
            String type = bugs.get(1);
            String location = bugs.get(3);
            String code = bugs.get(4);
            samples.add(new Vulnerability(id, threatlevel, type, location, code));
        }
        vulDao.saveAll(samples.subList(0,2000));

//        for(Vulnerability vul : samples){
//            vulDao.saveAndFlush(vul);
//        }
    }


    @Override
    public void probUpdate(Long id, double prob) {
        Vulnerability vul = vulDao.findById(id).get();
        vul.setProbability(prob);
        vulDao.saveAndFlush(vul);
    }

    @Override
    public List<Vulnerability> loadDataset() {
        return vulDao.findAll();
    }


    public void getFileList(File Dir) {
        File[] arr=Dir.listFiles();//获取文件或文件夹对象
        if(arr.length>0 && !arr[0].isDirectory()){
            List<String> dataline = new ArrayList<>();
            String datainfo = "";
            String path = "";
            for(File file: arr){
                if(file.getName().endsWith(".txt")){
                    try{
                        InputStreamReader read = new InputStreamReader(new FileInputStream(file));// 考虑到编码格式
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        lineTxt = bufferedReader.readLine();
                        datainfo = lineTxt;
                        bufferedReader.close();
                        read.close();
                    }
                    catch (Exception e)
                    {
                        System.out.println("读取文件内容出错");
                        e.printStackTrace();
                    }
                }
                else if(file.getName().endsWith(".java")){
                    path = file.getAbsolutePath();
                }
                else{
                    continue;
                }
            }

            String[] data = datainfo.split(" ");
            for(String item : data){
                dataline.add(item);
            }
            dataline.add(path);
            datalist.add(dataline);

        }
        else{
            for(File file : arr){
                getFileList(file);
            }
        }
    }

}
