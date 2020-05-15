package com.example.demo.Service;

import com.example.demo.Dao.BugDao;

import com.example.demo.Entity.Bug;
import com.example.demo.Sampling.Sampling;

import com.example.demo.Sampling.clustering.Cluster;
import com.example.demo.ServiceInterface.DoClusterService;
import com.example.demo.Tool.SimCode;
import com.example.demo.Tool.Vulnerability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


@Service
public class DoClusterImpl implements DoClusterService {
    @Autowired
    private BugDao bugDao;

    public List<List<String>> datalist = new ArrayList<>();

    @Override
    public void doClusterService() {

        File filedir = new File("/root/BugSlice");
        getFileList(filedir);
        List<Vulnerability> samples = new ArrayList<Vulnerability>();
        for(List<String> bugs : datalist){
            String id = bugs.get(0);
            int threatlevel = Integer.valueOf(bugs.get(2));
            String type = bugs.get(1);
            String location = bugs.get(3);
            String code = bugs.get(4);
            BigDecimal   b   =   new BigDecimal(Math.random());
            double prob   =   b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
            samples.add(new Vulnerability(id, threatlevel, type, location, code, prob));
        }

        SimCode simCode = new SimCode();

        ArrayList<Vulnerability> arrayList_sublist = new ArrayList<Vulnerability>();


        Set set=new HashSet();
        for(Vulnerability v: samples){
            set.add(v.type);
        }
        System.out.println("SET START");
//        for(Iterator it = set.iterator(); it.hasNext(); )
//        {
//            System.out.println(it.next().toString());
//        }
        System.out.println("SET END");


        for(int i=0;i<=500;i++){
            arrayList_sublist.add(samples.get(i));
        }

        Object[] res_clustering = simCode.clustering(arrayList_sublist, 5);

        ArrayList<Cluster> clusters =(ArrayList<Cluster>) res_clustering[0];

        Sampling sampling = new Sampling();
        List<List<Vulnerability>> res = sampling.getSamples(clusters);
        int clusterid = 0;
        for(List<Vulnerability> list: res){
            for(Vulnerability vul : list){
                Bug bug = vul.convert2Bug();
                bug.setCluster(""+clusterid);
                bugDao.saveAndFlush(bug);
            }
            clusterid++;
        }

        System.out.println("END");
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
