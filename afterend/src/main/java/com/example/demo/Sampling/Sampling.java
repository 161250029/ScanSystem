package com.example.demo.Sampling;



import com.example.demo.Entity.Vulnerability;
import com.example.demo.Sampling.clustering.Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sampling {

    public List<List<Vulnerability>> getSamples(List<Cluster> clusters){
        List<Vulnerability> samples= new ArrayList<>();
        for(int i = 0; i < clusters.size(); i++){
            for(Vulnerability vul : clusters.get(i).elements){
                samples.add(vul);
            }
        }

        List<List<Vulnerability>> res = new ArrayList<>();
        for(int i = 0; i < clusters.size(); i++){
            List<Vulnerability> clustersamples = new ArrayList<>();
            // get high prob samples
            List<Vulnerability> highsample = getHighProbability(clusters.get(i).elements, 0.8);
                //20% samples
            // get low prob samples
            List<Vulnerability> lowsample = getLowProbability(clusters.get(i).elements, 0.8);
                //80% samples
            int size = highsample.size() + lowsample.size();
            List<Vulnerability> probSamples = getSamplesByProb(highsample, lowsample, size);

            // get samples by type
//            List<Vulnerability> typeSamples = getSamplesByType(probSamples);
            res.add(probSamples);
        }

        return res;
    }

    public List<Vulnerability> getSamplesByType(List<Vulnerability> samples){


        return null;
    }

    public List<Vulnerability> getSamplesByProb(List<Vulnerability> high, List<Vulnerability> low, int size){

        int highsize = (int)(size * 0.8);
        int lowsize = size - highsize;

        List<Vulnerability> highsample = createRandomList(high, highsize);
        List<Vulnerability> lowsample = createRandomList(low, lowsize);
        List<Vulnerability> res = new ArrayList<>();
        lowsample.addAll(highsample);
        return lowsample;
    }

    public List createRandomList(List list, int n){
        Map map = new HashMap();
        List newlist = new ArrayList();
        if(list.size() <= n){
            return list;
        }
        else{
            while(map.size() < n){
                int random = (int) (Math.random() * list.size());
                if(!map.containsKey(random)){
                    map.put(random, "");
                    newlist.add(list.get(random));
                }
            }
            return newlist;
        }
    }



    public List<Vulnerability> getHighProbability(List<Vulnerability> samples, double prob){
        List<Vulnerability> res = new ArrayList<>();
        for(int i = 0; i < samples.size(); i++){
            Vulnerability vul = samples.get(i);
            if(vul.probability >= prob){
                res.add(vul);
            }
        }
        return res;
    }

    public List<Vulnerability> getLowProbability(List<Vulnerability> samples, double prob){
        List<Vulnerability> res = new ArrayList<>();
        for(int i = 0; i < samples.size(); i++){
            Vulnerability vul = samples.get(i);
            if(vul.probability < prob){
                res.add(vul);
            }
        }
        return res;
    }




}
