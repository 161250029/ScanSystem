package com.example.demo.Sampling.clustering;


import com.example.demo.Sampling.CodeSimilarity;
import com.example.demo.Sampling.CosineSimilarity;
import com.example.demo.Tool.Vulnerability;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMedoids {
    String url = "templates/data/HEART_Stat.arff";
    public double[][] sim;
    public List<Vulnerability> samples;
    public ArrayList<Cluster> clusterList;
    public int k = 10;

    public Object[] AlgoKmedoids(List<Vulnerability> samples, int k ) {
        clusterList = new ArrayList<Cluster>();
        this.k = k;
        this.samples = samples;
        int size = samples.size();
        if (k > size - 1) {
            return null;
        }
        //计算执行时间
        long startTime = System.currentTimeMillis();

        //步骤1
        //随机选择k medoid(从我们的数据集中)
        //为此，我们用dataset索引填充一个列表
        ArrayList<Integer> listIndex = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            listIndex.add(i);
        }
        //然后每次从列表中删除一个索引(以避免生成随机索引)
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            int r = rand.nextInt(listIndex.size());
            int index = listIndex.remove(r);
            Vulnerability medoid = samples.get(index);
            Cluster c = new Cluster(medoid, index);
            clusterList.add(c);
        }
        //步骤2
        boolean change = true;
        while (change) {
            //初始化不同的arraylist
            for (int i = 0; i < k; i++) {
                clusterList.get(i).dissimilarity = new ArrayList<Double>();
            }
            //计算距离
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < k; j++) {
                    double dis = distance(samples.get(i), clusterList.get(j).medoid);
                    clusterList.get(j).getDissimilarity().add(dis);
                }
            }
            //重新元素的集群
            for (int j = 0; j < k; j++) {
                clusterList.get(j).elements = new ArrayList<Vulnerability>(); //en les mettre vides
            }
            //将每个实例分配到集群
            for (int i = 0; i < size; i++) {
                int minClusterIndex = 0;
                double minDistance = clusterList.get(0).getDissimilarity().get(i);
                for (int j = 1; j < k; j++) {
                    if (clusterList.get(j).getDissimilarity().get(i) < minDistance) {
                        minClusterIndex = j;
                        minDistance = clusterList.get(j).getDissimilarity().get(i);
                    }
                }
                clusterList.get(minClusterIndex).getElements().add(samples.get(i));
            }
            //随机选择一个中心
            int Oj = rand.nextInt(k);
            //替换这个中心
            listIndex = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                listIndex.add(i);
            }
            for (int i = 0; i < k; i++) {
                int index = listIndex.indexOf(clusterList.get(i).index);
                listIndex.remove(index);
            }
            int Orand = rand.nextInt(listIndex.size());

            ArrayList<Cluster> clusterListTemp = new ArrayList<Cluster>();
            clusterListTemp = clusterList;
            clusterListTemp.get(Oj).medoid = samples.get(Orand);
            //计算总开销
            double absE_Before = absoluteErrorSom(clusterList);
            double absE_After = absoluteErrorSom(clusterListTemp);
            double S = absE_After - absE_Before;
            if (S < 0) {
                clusterList = clusterListTemp;
            } else {
                change = false;
            }
        }
        long endTime = System.currentTimeMillis();
        double duration = endTime - startTime;
        Object obj[] = new Object[2];
        obj[0] = clusterList;
        obj[1] = duration;
        return obj;
    }


    public double distance(Vulnerability vulnerability1, Vulnerability vulnerability2) {
        double dist = 0;
        CodeSimilarity cosineSimilarity = new CodeSimilarity(new CosineSimilarity());
        double sim = cosineSimilarity.get(vulnerability1.getRelatedCode(), vulnerability2.getRelatedCode());
//        for (int i = 0; i < vulnerability1.getTab().length; i++) {
//            double dif = Math.pow(Float.parseFloat(vulnerability1.getTab()[i]) - Float.parseFloat(vulnerability2.getTab()[i]), 2);
//            dist += dif;
//        }
        dist = 10 * (1-sim);
        return dist;
    }

    //the sum of absolute error
    public double absoluteErrorSom(ArrayList<Cluster> clusterList) {
        double absE = 0;
        for (int i = 0; i < k; i++) {
            Cluster c = clusterList.get(i);
            double somC = 0;
            for (int j = 0; j < c.getElements().size(); j++) {
                somC += Math.abs(distance(c.getElements().get(j), c.medoid));
            }
            absE += somC;
        }
        return absE;
    }

}


