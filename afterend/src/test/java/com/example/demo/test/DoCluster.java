package com.example.demo.test;

import com.example.demo.Service.VulnerServiceImpl;
import com.example.demo.ServiceInterface.DoClusterService;
import com.example.demo.ServiceInterface.VulnerService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DoCluster {
    @Autowired
    private DoClusterService doClusterService;

    @Autowired
    VulnerService vulnerService;


//    @Test
//    public void test1() {
//        doClusterService.doClusterService();
//    }



    @Test
    public void test2() {

//        VulnerServiceImpl vul = new VulnerServiceImpl();

        String path = "C:\\Users\\ningl\\Desktop\\BugSlice";
        vulnerService.readDataset(path);

        doClusterService.clustering();

    }



}
