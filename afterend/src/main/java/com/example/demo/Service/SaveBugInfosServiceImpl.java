package com.example.demo.Service;

import com.example.demo.Dao.BugCollectDao;
import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Entity.BugCollect;
import com.example.demo.Entity.BugInfo;
import com.example.demo.ServiceInterface.MarkBugService;
import com.example.demo.ServiceInterface.SaveBugInfosService;
import com.example.demo.Tool.DirectoryTool;
import com.example.demo.Tool.DomTool;
import com.example.demo.Tool.FileMappingTableTool;
import com.example.demo.Tool.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaveBugInfosServiceImpl implements SaveBugInfosService {
    @Autowired
    private BugInfoDao bugInfoDao;

    @Autowired
    private BugCollectDao bugCollectDao;

    @Autowired
    private MarkBugService markBugService;

    @Override
    public void save_find_sec_bugs() {
        save_bugs("find_sec_bug" , "D:\\findsecbugResult");
    }

    @Override
    public void save_spot_bugs() {
        save_bugs("spot_bug" , "D:\\SpotsBugResult");
    }

    @Override
    public void save_find_bugs() {

    }

    public void save_bugs(String tool_name , String dir_path) {
        ArrayList<String> resultPaths = DirectoryTool.getXmlFiles(dir_path);
        resultPaths.forEach(resultPath -> {
            try {
                List<BugInfo> bugInfos = DomTool.getBugs(resultPath);
                bugInfos.forEach(bugInfo -> bugInfo.setToolName(tool_name));
                bugInfoDao.saveAll(bugInfos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void dealdata(String spot_path, String find_path, String dirpath, String jarpath) throws Exception {
        List<BugCollect> spotbugs = DomTool.getBugInfo(spot_path , jarpath);
        List<BugCollect> findbugs = DomTool.getBugInfo(find_path , jarpath);
        Map<String , String> table = FileMappingTableTool.table(dirpath);
        for (BugCollect bugCollect : spotbugs) {
            String file_path = table.get(bugCollect.getSourceFile());
            if (file_path == null)
                continue;
            bugCollect.setFile_path(file_path);
            bugCollect.setToolName("spot_bug");
            bugCollectDao.saveAndFlush(bugCollect);
//            FileTool fileTool = new FileTool(file_path);
//            fileTool.run(dirpath + "\\" + bugCollect.getSourceFile().split("\\.")[0] + "\\" + bugCollect.getStart() ,  dirpath + "\\" + bugCollect.getSourceFile().split("\\.")[0] + "\\" + bugCollect.getStart()
//                    + "\\" + bugCollect.getSourceFile() , bugCollect.getStart());
        }
        for (BugCollect bugCollect : findbugs) {
            String file_path = table.get(bugCollect.getSourceFile());
            if (file_path == null)
                continue;
            bugCollect.setFile_path(file_path);
            bugCollect.setToolName("find_sec_bug");
            bugCollectDao.saveAndFlush(bugCollect);
//            FileTool fileTool = new FileTool(file_path);
//            fileTool.run(dirpath + "\\" + bugCollect.getSourceFile().split("\\.")[0] + "\\" + bugCollect.getStart() ,  dirpath + "\\" + bugCollect.getSourceFile().split("\\.")[0] + "\\" + bugCollect.getStart()
//                    + "\\" + bugCollect.getSourceFile() , bugCollect.getStart());
        }
        System.out.println("start mark");
        markBugService.MarkBugs(jarpath);
        List<BugCollect> bugCollects = bugCollectDao.findbugsbyjar(jarpath);
        System.out.println(jarpath);
        System.out.println("总共个数 " + bugCollects.size());
        for (BugCollect bugCollect : bugCollects) {
            if (bugCollect.isIs_positive()) {
                FileTool fileTool = new FileTool(bugCollect.getFile_path());

                fileTool.run(dirpath + "/" + "Positive/" + bugCollect.getSourceFile().split("\\.")[0] + "/" + bugCollect.getStart() ,  dirpath + "/" + "Positive/" + bugCollect.getSourceFile().split("\\.")[0] + "/" + bugCollect.getStart()
                        + "/" + bugCollect.getSourceFile() , bugCollect.getStart());
            }
            else {
                FileTool fileTool = new FileTool(bugCollect.getFile_path());
                fileTool.run(dirpath + "/" + "False/" + bugCollect.getSourceFile().split("\\.")[0] + "/" + bugCollect.getStart() ,  dirpath + "/" + "False/" + bugCollect.getSourceFile().split("\\.")[0] + "/" + bugCollect.getStart()
                        + "/" + bugCollect.getSourceFile() , bugCollect.getStart());
            }
        }
    }
}
