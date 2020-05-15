package com.example.demo.test;

import com.example.demo.ServiceInterface.DoSlicerService;
import com.example.demo.ServiceInterface.MarkBugService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DoSlicer {
    @Autowired
    private MarkBugService markBugService;
    @Autowired
    DoSlicerService doSlicerService;

    @Test
    public void test1() {
        markBugService.MarkBugs();
    }

    @Test
    public void  test2() {
        doSlicerService.WalaSlicer_Positive();
    }

    @Test
    public void test3() {
        doSlicerService.TextSlicer_Positive();
    }

    @Test
    public void test4() {
        doSlicerService.TextSlicer_False_Postive();
    }

    @Test
    public void test5() {
        doSlicerService.WalaSlicer_False_Positive();
    }

    @Test
    public void test6()  {
        doSlicerService.TextSlicer_BugInfo_fivelines();
    }

}
