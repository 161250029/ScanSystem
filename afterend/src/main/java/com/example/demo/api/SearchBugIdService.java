package com.example.demo.api;

import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Entity.BugInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchBugIdService {
    @Autowired
    BugInfoDao bugInfoDao;

    public long getBugId(String filename , int start) {
        List<BugInfo> bugs = bugInfoDao.findBugsByFileNameAndStart(filename ,start);
        return bugs.get(bugs.size() - 1).getId();
    }
}
