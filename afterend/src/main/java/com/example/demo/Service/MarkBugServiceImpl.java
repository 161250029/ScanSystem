package com.example.demo.Service;

import com.example.demo.Dao.BugCollectDao;
import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Entity.BugCollect;
import com.example.demo.Entity.BugInfo;
import com.example.demo.ServiceInterface.MarkBugService;
import com.example.demo.Tool.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class MarkBugServiceImpl implements MarkBugService {
    @Autowired
    private BugInfoDao bugInfoDao;
    @Autowired
    private BugCollectDao bugCollectDao;

    @Override
    public void MarkBugs() {
        Set<String> fileNames = findAllFileHasBug();
        for (String fileName : fileNames) {
            List<BugInfo>  spot_bugs = bugInfoDao.findbugsByFileNameAndToolName(fileName , "spot_bug");
            List<BugInfo>  find_sec_bugs = bugInfoDao.findbugsByFileNameAndToolName(fileName , "find_sec_bug");
            if (spot_bugs == null || find_sec_bugs == null || spot_bugs.size() == 0 || find_sec_bugs.size() == 0)
                continue;
            for (BugInfo spot_bugInfo : spot_bugs) {
                for (BugInfo find_sec_bugInfo : find_sec_bugs) {
                    if (spot_bugInfo.getStart() == find_sec_bugInfo.getStart()) {
                        spot_bugInfo.setWarning(true);
                        find_sec_bugInfo.setWarning(true);
                    }
                }
            }
            bugInfoDao.saveAll(spot_bugs);
            bugInfoDao.saveAll(find_sec_bugs);
        }
//        System.out.println(fileNames.size());
    }

    @Override
    public void MarkBugs(String jarpath) {
        Set<String> fileNames = findFileHasBug(jarpath);
        for (String fileName : fileNames) {
            System.out.println(fileName);
            List<BugCollect>  spot_bugs = bugCollectDao.findbugsByFileNameAndToolName(fileName , "spot_bug");
            List<BugCollect>  find_sec_bugs = bugCollectDao.findbugsByFileNameAndToolName(fileName , "find_sec_bug");
            if (spot_bugs == null || find_sec_bugs == null || spot_bugs.size() == 0 || find_sec_bugs.size() == 0)
                continue;
            for (BugCollect spot_bugInfo : spot_bugs) {
                for (BugCollect find_sec_bugInfo : find_sec_bugs) {
                    if (spot_bugInfo.getStart() == find_sec_bugInfo.getStart()) {
                        spot_bugInfo.setIs_positive(true);
                        find_sec_bugInfo.setIs_positive(true);
                    }
                }
            }
            bugCollectDao.saveAll(spot_bugs);
            bugCollectDao.saveAll(find_sec_bugs);
        }
    }

    public Set<String> findAllFileHasBug() {
        Set<String> fileNames = new HashSet<>();
        List<BugInfo> bugInfos = bugInfoDao.findAll();
        bugInfos.forEach(bugInfo -> fileNames.add(bugInfo.getSourceFile()));
        return fileNames;
    }

    public Set<String> findFileHasBug(String jarpath) {
        Set<String> fileNames = new HashSet<>();
        List<BugCollect> bugCollects = bugCollectDao.findbugsbyjar(jarpath);
        bugCollects.forEach(bugCollect -> fileNames.add(bugCollect.getSourceFile()));
        return fileNames;
    }
}
