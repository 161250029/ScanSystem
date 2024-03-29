package com.example.demo.Service;

import com.example.demo.Dao.BugDao;
import com.example.demo.Entity.Bug;
import com.example.demo.ServiceInterface.GetCodeService;
import com.example.demo.Tool.Group;
import com.example.demo.Tool.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;


@Service
public class GetCodeImpl implements GetCodeService {

    @Autowired
    private BugDao bugDao;

    @Override
    public String getCodeService(String id, String path) {
        Long dataid = Long.valueOf(id);
        Bug bug = bugDao.findById(dataid).get();
        String filepath = bug.getCode().replaceAll("\\\\","/");
        String[] tmp = filepath.split("Desktop");
        String tmp1 = tmp[1];
        path = "/root"+tmp1;

        File file = new File(path);
        String content = "";
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file)); // 与上等效
            while (null != (line = br.readLine()) ) {
                content += line + "\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("code: "+ content);
        return content;
    }

    @Override
    public String getCodeSlice(String id, String path) {
        Long dataid = Long.valueOf(id);
        Bug bug = bugDao.findById(dataid).get();
        String filepath = bug.getCode().replaceAll("\\\\","/");
        String[] tmp = filepath.split("BugSlice");
        String tmp1 = tmp[1];
        path = "/root/predict"+tmp1;

        File file = new File(path);
        String content = "";
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file)); // 与上等效
            while (null != (line = br.readLine()) ) {
                content += line + "\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("code: "+ content);
        return content;
    }

    @Override
    public void probUpdate(String id, double prob) {
        System.out.println("Now: "+ id);
        Long dataid = Long.valueOf(id);

        Bug bug = bugDao.findById(dataid).get();
        bug.setProbability(prob);
        bugDao.saveAndFlush(bug);
    }

    @Override
    public List<Bug> getBugs(String id) {
        System.out.println("taskid "+id);
        if(null == id){
            return bugDao.findAll();
        }
        else{
            List<Bug> res = new ArrayList<>();
            List<Bug> list = bugDao.findAll();
            for(Bug bug : list){
                if(bug.getCluster().equals(id)){
                    res.add(bug);
                }
            }
            return res;
        }
    }

    @Override
    public void submitBug(String id, String type, String level, String comments) {
        System.out.println("IN "+ id);
        Long dataid = Long.valueOf(id);
        Bug bug = bugDao.findById(dataid).get();
        if(null != type){
            bug.setType(type);
        }
        if(null != level){
            bug.setThreatlevel(Integer.parseInt(level));
        }
        bug.setComment(comments);
        bug.setReviewed(true);
        bugDao.saveAndFlush(bug);
    }

    @Override
    public List<Type> getstatics() {
        Map<String, Integer> map = new HashMap<>();
        int k = 10;

        List<Bug> bugs = bugDao.findAll();
        for (Bug bug : bugs) {
            map.put(bug.getType(), map.getOrDefault(bug.getType(), 0) + 1);
        }


        List<String>[] buckets = new ArrayList[bugs.size() + 1];
        for (String key : map.keySet()) {
            System.out.println(key);
            int frequency = map.get(key);
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(key);
        }
        List<String> topK = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && topK.size() < k; i--) {
            if (buckets[i] == null) {
                continue;
            }
            if (buckets[i].size() <= (k - topK.size())) {
                topK.addAll(buckets[i]);
            } else {
                topK.addAll(buckets[i].subList(0, k - topK.size()));
            }
        }

        List<Type> types = new ArrayList<>();
        for(String name : topK){
            int num = map.get(name);
            types.add(new Type(name, num));
        }
        return types;
    }

    @Override
    public List<Group> getGroups() {
        List<Group> res = new ArrayList<>();
        List<Bug> bugs = bugDao.findAll();
        Map<String, ArrayList<Bug>> groups = new HashMap<>();
        for (Bug bug : bugs) {
            ArrayList<Bug> tmpbug = new ArrayList<>();
            if(groups.containsKey(bug.getCluster())){
                groups.get(bug.getCluster()).add(bug);
            }
            else{
                tmpbug.add(bug);
                groups.put(bug.getCluster(), tmpbug);
            }
        }
        for (String key : groups.keySet()) {
            int size = groups.get(key).size();
            Map<String, Integer> map = new HashMap<>();
            List<Bug> subbug = groups.get(key);
            int taskprocess = 0;
            for (Bug bug : subbug) {
                map.put(bug.getType(), map.getOrDefault(bug.getType(), 0) + 1);
                if(bug.isReviewed == true) taskprocess++;
            }
            String mode = "";
            int tmp = 0;
            for(String subkey: map.keySet()){
                if(map.get(subkey) > tmp){
                    tmp = map.get(subkey);
                    mode = subkey;
                }
            }
            res.add(new Group(key, size, taskprocess, mode));

        }
//        System.out.println("tasksize: "+ res.tasksize());
//        System.out.println("sample: "+ res.get(0).taskid);
//        System.out.println("tasksize: "+ res.get(0).mode);
        return res;
    }

    @Override
    public Bug getOneBug(String id) {
        Long bugid = Long.valueOf(id);
        Bug bug = bugDao.findById(bugid).get();
        return bug;
    }

    @Override
    public void reviewPass(String id) {
        Long dataid = Long.valueOf(id);
        Bug bug = bugDao.findById(dataid).get();
        bug.isReviewed = true;
        bug.isFalse = false;
        bugDao.saveAndFlush(bug);
    }

    @Override
    public void reviewFalse(String id) {
        Long dataid = Long.valueOf(id);
        Bug bug = bugDao.findById(dataid).get();
        bug.isReviewed = true;
        bug.isFalse = true;
        bugDao.saveAndFlush(bug);
    }
}
