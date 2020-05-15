package com.example.demo.test;

import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Entity.BugInfo;
import com.example.demo.Resources.JarLocationInfo;
import com.example.demo.Tool.DirectoryTool;
import com.example.demo.Tool.FileTool;
import com.example.demo.Tool.WalaSlicerTool;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BugInfoDaoTest {
    @Autowired
    private BugInfoDao bugInfoDao;

    @Test
    public void test1() {
        System.out.println(bugInfoDao.findBugsByToolName("find_sec_bug").size());
        System.out.println(bugInfoDao.findBugsByToolName("spot_bug").size());
    }

    @Test
    public void test2() {
        System.out.println(bugInfoDao.findBugsByWarning(true).size());
        System.out.println(bugInfoDao.findBugsByWarning(false).size());
    }

    @Test
    public void test3() {
        System.out.println(bugInfoDao.findBugsByJarLocation("CWE15_External_Control_of_System_or_Configuration_Setting").size());
        System.out.println(bugInfoDao.findBugsByStart(0).get(0).getEnd() + bugInfoDao.findBugsByStart(0).get(0).getSourceFile() + bugInfoDao.findBugsByStart(0).get(0).getMethodName());
    }

    @Test
    public void test5() {
        System.out.println(bugInfoDao.findBugsByJarLocation("CWE80_XSS_s01").size());
    }

    @Test
    public void test4() {
        List<BugInfo> bugInfos = bugInfoDao.findbugsByFileName("CWE23_Relative_Path_Traversal__connect_tcp_01");
        String JarPath = JarLocationInfo.Jar_Directory_prefix + "\\CWE23_Relative_Path_Traversal" + ".jar";
        for (BugInfo bugInfo : bugInfos) {
            try {
//                System.out.println(bugInfo.getSourceFile() + " " + bugInfo.getStart() + " "+ bugInfo.getMethodName());
                Set<Integer> source_line_numbers = new TreeSet<>();
                if (!bugInfo.getSourceFile().equals("CWE15_External_Control_of_System_or_Configuration_Setting__File_66b") && !bugInfo.getSourceFile().equals("CWE15_External_Control_of_System_or_Configuration_Setting__File_72b")) {
                    source_line_numbers = WalaSlicerTool.doSlicing(JarPath , bugInfo.getMethodName() , bugInfo.getSourceFile() , bugInfo.getStart());
                }
                else
                    source_line_numbers.add(bugInfo.getStart());
                String source_file_path = JarLocationInfo.Jar_Directory_prefix + "\\CWE23_Relative_Path_Traversal\\" + bugInfo.getSourceFile() + ".java";
                FileTool fileTool = new FileTool(source_file_path);
                DirectoryTool.CreateResultDir("CWE23_Relative_Path_Traversal");
                fileTool.writeWalaStatements("CWE23_Relative_Path_Traversal\\" + bugInfo.getSourceFile() + ".txt", source_line_numbers , bugInfo);
            } catch (WalaException e) {
                e.printStackTrace();
            } catch (CancelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidClassFileException e) {
                e.printStackTrace();
            }
        }
    }
    public String GenerateFilePathPrefix(String JarName) {
        int length = JarName.length();
        if (JarName.substring(length - 3 , length - 1).equals("s0")) {
            return  JarName.substring(0 , length - 4) + "\\" + JarName.substring(length - 3);
        }
        return JarName;
    }
}
