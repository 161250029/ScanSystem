package com.example.demo.Tool;



import com.example.demo.Dao.BugDao;

import com.example.demo.Entity.Bug;
import com.example.demo.Entity.Vulnerability;
import com.example.demo.Sampling.CodeSimilarity;
import com.example.demo.Sampling.CosineSimilarity;
import com.example.demo.Sampling.JaccardSimilarity;
import com.example.demo.Sampling.Sampling;

import com.example.demo.Sampling.clustering.Cluster;
import com.example.demo.Sampling.clustering.KMedoids;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimCode {
    @Autowired
    private static BugDao bugDao;


    public static List<List<String>> datalist = new ArrayList<>();

    public static void main(String[] args) {



//        File dir = new File("C:\\Users\\ningl\\Downloads\\CWE15_External_Control_of_System_or_Configuration_Setting");
//        List<List<String>> data = PrintJavaFile(dir);
//        ArrayList<Vulnerability> samples = new ArrayList<>();
////        for(List<String> item: data){
//////            samples.add(new Vulnerability());
////        }


//        List<String> bugs = readTxtFileIntoStringArrList("C:\\Users\\ningl\\IdeaProjects\\SimCode\\src\\main\\java\\com\\ning\\data\\report.txt");
//
////        ArrayList<Vulnerability> samples = new ArrayList<>();
//        for(int i = 0; i < bugs.tasksize(); i++){
//            String bug = bugs.get(i);
//            String[] items = bug.split(",");
//            String taskid = items[0];
//            int threatlevel = Integer.valueOf(items[1]);
//            String type = items[2];
//            String location = items[3];
//            double prob = Double.valueOf(items[4]);
//            samples.add(new Vulnerability(taskid, threatlevel, type, location, prob));
//        }

//        SimCode simCode = new SimCode();
//
//        Object[] res_clustering = simCode.clustering(samples);
//
//        ArrayList<Cluster> clusters =(ArrayList<Cluster>) res_clustering[0];
//
//        Sampling sampling = new Sampling();
//        List<List<Vulnerability>> res = sampling.getSamples(clusters);
//        System.out.println("Size1: "+ clusters.tasksize());
//        System.out.println("Size2: "+ res.tasksize());





        File filedir = new File("/root/BugSlice");
        getFileList(filedir);
        List<Vulnerability> samples = new ArrayList<Vulnerability>();
        for(List<String> bugs : datalist){
            Long id = Long.valueOf(bugs.get(0));
            int threatlevel = Integer.valueOf(bugs.get(2));
            String type = bugs.get(1);
            String location = bugs.get(3);
            String code = bugs.get(4);
            double prob = Double.valueOf("0.0");
            samples.add(new Vulnerability(id, threatlevel, type, location, code, prob));
        }

        SimCode simCode = new SimCode();

        ArrayList<Vulnerability> arrayList_sublist = new ArrayList<Vulnerability>();
        for(int i=0;i<=100;i++){
            arrayList_sublist.add(samples.get(i));
        }

        Object[] res_clustering = simCode.clustering(arrayList_sublist, 8);

        ArrayList<Cluster> clusters =(ArrayList<Cluster>) res_clustering[0];

        Sampling sampling = new Sampling();
        List<List<Vulnerability>> res = sampling.getSamples(clusters);
        for(List<Vulnerability> list: res){
            for(Vulnerability vul : list){
                Bug bug = vul.convert2Bug();
                bugDao.saveAndFlush(bug);
            }
        }


        System.out.println("END");



    }



    public static void getFileList(File Dir) {
        File[] arr=Dir.listFiles();//获取文件或文件夹对象
//        List<List<String>> datalist = new ArrayList<>();


        if(arr.length>0 && !arr[0].isDirectory()){
            List<String> dataline = new ArrayList<>();
            String datainfo = "";
            String content = "";
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
                    String line = null;
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(file)); // 与上等效

                        while (null != (line = br.readLine())) {
                            content += line + "\n";
                        }
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    continue;
                }
            }


            String[] data = datainfo.split(" ");
            for(String item : data){
                dataline.add(item);
            }
            dataline.add(content);
            datalist.add(dataline);
            System.out.println(datalist.size());
            System.out.println("OK item");
        }
        else{
            for(File file : arr){
                getFileList(file);
            }
        }
    }

    public static List<List<String>> PrintJavaFile(File Dir) {
        File [] arr=Dir.listFiles();//获取文件或文件夹对象
        List<List<String>> datalist = new ArrayList<>();




        for (File file : arr) {//遍历File数组
            List<String> list = new ArrayList<>();
            if(file.isFile()&&file.getName().endsWith(".txt")) {//判断对象是否是以.java结尾的类型的文件，是的话就输出
                try{
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file));// 考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    lineTxt = bufferedReader.readLine();
                    String[] strs = lineTxt.split(" ");
                    for(int i = 0; i < strs.length; i++){
                        list.add(strs[i]);
                    }
                    String code = bufferedReader.readLine();
//                    String code = "";
//                    while((lineTxt = bufferedReader.readLine()) != null)
//                    {
//                        code += lineTxt;
//                    }
                    list.add(code);
                    bufferedReader.close();
                    read.close();
                }
                catch (Exception e)
                {
                    System.out.println("读取文件内容出错");
                    e.printStackTrace();
                }
                System.out.println("OK1");
                datalist.add(list);
            }
            else if(file.isFile()&&file.getName().endsWith(".java")){
                int index = datalist.size();
                String line = null;
                String content = ""; // 文件内容
                BufferedReader br = null;
                try {
                    // br = new BufferedReader(new FileReader(path));
                    br = new BufferedReader(new FileReader(file)); // 与上等效

                    while (null != (line = br.readLine())) {
//                        System.out.println(line);
                        content += line + "\n";
                    }
                    if(index >0){
                        datalist.get(index-1).add(content);
                    }
                    br.close();
//                    list.add(content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                datalist.add(list);
                System.out.println("OK2");
            }
            else if(file.isDirectory()) {//判断是否是目录，是的话，就继续调用PrintJavaFile（）方法进行递归
                PrintJavaFile(file);
            }
        }

        return datalist;
    }

    public Object[] clustering(List<Vulnerability> samples, int k){

        ArrayList<String> codelist = new ArrayList<>();
        Map<Long, Integer> map = new HashMap<>();
        int tmp = 0;
        for(Vulnerability v : samples){
            map.put(v.id, tmp++);
            String sourcecode = v.getRelatedCode();
            codelist.add(sourcecode);
        }

        int size = codelist.size();

        System.out.println("SIMILARITY COMUPUTING ....");
        // calculate similarity matrix
        double[][] simMatrix = new double[size][size];

        CodeSimilarity cosineSimilarity = new CodeSimilarity(new CosineSimilarity());
        CodeSimilarity jaccardSimilarity = new CodeSimilarity(new JaccardSimilarity());

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                simMatrix[i][j] = cosineSimilarity.get(codelist.get(i), codelist.get(j));
            }
        }

//        System.out.println("MATRIX");
//        System.out.println(simMatrix);
//        System.out.println("Affinity Clustering");
//        AffinityPropagation affinityPropagation = new AffinityPropagation(simMatrix);
//        List<AffinityPropagation.ClusterIds> clusterIdList = affinityPropagation.run();
//
//        List<List<String>> clusterlist = new ArrayList<>();
//        for (AffinityPropagation.ClusterIds clusterId : clusterIdList) {
//            List<String> idlist = new ArrayList<>();
//            for (Integer item : clusterId.getDataCenterIdList()) {
////                text += samples.get(item).taskid + " ";
//                idlist.add(samples.get(item).taskid);
//            }
//            clusterlist.add(idlist);
//        }
//        List<Double> cpap_list = getCP_AP(clusterlist,simMatrix,map);
//        double cp_ap = 0.0;
//        for(double d : cpap_list){
//            cp_ap += d;
//        }
//        cp_ap = cp_ap / cpap_list.tasksize();
//        System.out.println("CP: "+cp_ap);
//        double sp_ap = getSP_AP(clusterlist, simMatrix, map);
//        System.out.println("SP: "+sp_ap);
//        double db_ap = getDBI_AP(clusterlist, simMatrix, cpap_list, map);
//        System.out.println("DBI: "+db_ap);



        System.out.println("KMedoids Clustering");
        //KMedoids clustering
        KMedoids km = new KMedoids();
        Object[] res_kmedoids = km.AlgoKmedoids(samples, k);
        ArrayList<Cluster> cluster =(ArrayList<Cluster>) res_kmedoids[0];
//        System.out.println("NUMBER ID: "+ cluster.get(0).medoid.taskid);
//        for(int i = 0; i < cluster.tasksize(); i++){
//            System.out.println(cluster.get(i).toString());
//        }
        List<Double> cp_list = getCP_KMedoids(cluster,simMatrix,map);
        double cp_km = 0.0;
        for(double d : cp_list){
            cp_km += d;
        }
        cp_km = cp_km / cp_list.size();
        System.out.println("CP: "+cp_km);
        double sp_km = getSP_KMedoids(cluster, simMatrix, map);
        System.out.println("SP: "+sp_km);
        double db_km = getDBI_KMedoids(cluster, simMatrix, cp_list, map);
        System.out.println("DBI: "+db_km);

        double time = (double) res_kmedoids[1];
        System.out.println("Time: "+time);

        return res_kmedoids;
    }

    public List<Double> getCP_KMedoids(ArrayList<Cluster> cluster, double[][] simi, Map<Long, Integer> map){
        List<Double> cp = new ArrayList<>();
        for(int i = 0; i < cluster.size(); i++){
            Cluster c = cluster.get(i);
            Vulnerability w = c.medoid;
            int indexw = map.get(w.id);
            double sumw = 0.0;
            for(Vulnerability v: c.elements){
                int indexv = map.get(v.id);
                double diswv = (1 - simi[indexw][indexv]) * 10;
                sumw += diswv;
            }
            double cpi = sumw / c.elements.size();
            cp.add(cpi);
        }
        return cp;
    }

    public List<Double> getCP_AP(List<List<String>> cluster, double[][] simi, Map<Long, Integer> map){
        List<Double> cp = new ArrayList<>();
        for(int i = 0; i < cluster.size(); i++){
            List<String> c = cluster.get(i);
            String w = c.get(0);
            int indexw = map.get(w);
            double sumw = 0.0;
            for(String v: c){
                int indexv = map.get(v);
                double diswv = (1 - simi[indexw][indexv]) * 10;
                sumw += diswv;
            }
            double cpi = sumw / c.size();
            cp.add(cpi);
        }
        return cp;
    }

    public double getSP_KMedoids(ArrayList<Cluster> cluster, double[][] simi,  Map<Long, Integer> map){
        double sum = 0.0;
        for(int i = 0; i < cluster.size(); i++){
            Vulnerability wi = cluster.get(i).medoid;
            int indexwi = map.get(wi.id);

            for(int j = 0; j < cluster.size(); j++){
                if(i <= j){
                    Vulnerability wj = cluster.get(j).medoid;
                    int indexwj = map.get(wj.id);
                    double dis = simi[indexwi][indexwj] * 10;
                    sum += dis;
                }
            }
        }
        double res = 0.0;
        int k = cluster.size();
        res = (2 * sum) / (k*k - k);
        return res;
    }

    public double getSP_AP(List<List<String>> cluster, double[][] simi,  Map<Long, Integer> map){
        double sum = 0.0;
        for(int i = 0; i < cluster.size(); i++){
            String wi = cluster.get(i).get(0);
            int indexwi = map.get(wi);
            for(int j = 0; j < cluster.size(); j++){
                if(i <= j){
                    String wj = cluster.get(j).get(0);
                    int indexwj = map.get(wj);
                    double dis = simi[indexwi][indexwj] * 10;
                    sum += dis;
                }
            }
        }
        double res = 0.0;
        int k = cluster.size();
        res = (2 * sum) / (k*k - k);
        return res;
    }


    public double getDBI_KMedoids(ArrayList<Cluster> cluster, double[][] simi, List<Double> cp, Map<Long, Integer> map){
        List<Double> db = new ArrayList<>();
        for(int i = 0; i < cluster.size(); i++){
            double ci = cp.get(i);
            Vulnerability wi = cluster.get(i).medoid;
            int indexwi = map.get(wi.id);
            double max = 0.0;
            for(int j = 0; j < cluster.size(); j++){
                if(i != j){
                    double cj = cp.get(j);
                    Vulnerability wj = cluster.get(j).medoid;
                    int indexwj = map.get(wj.id);
                    double dis = simi[indexwi][indexwj] * 10;
                    dis = (ci + cj) /dis;
                    max = Math.max(dis, max);
                }
            }
            db.add(max);
        }
        double res = 0.0;
        for(double s: db){
            res += s;
        }
        res = res / db.size();
        return res;
    }

    public double getDBI_AP(List<List<String>> cluster, double[][] simi, List<Double> cp, Map<Long, Integer> map){
        List<Double> db = new ArrayList<>();
        for(int i = 0; i < cluster.size(); i++){
            double ci = cp.get(i);
            String wi = cluster.get(i).get(0);
            int indexwi = map.get(wi);
            double max = 0.0;
            for(int j = 0; j < cluster.size(); j++){
                if(i != j){
                    double cj = cp.get(j);
                    String wj = cluster.get(j).get(0);
                    int indexwj = map.get(wj);
                    double dis = simi[indexwi][indexwj] * 10;
                    dis = (ci + cj) /dis;
                    max = Math.max(dis, max);
                }
            }
            db.add(max);
        }
        double res = 0.0;
        for(double s: db){
            res += s;
        }
        res = res / db.size();
        return res;
    }




}
