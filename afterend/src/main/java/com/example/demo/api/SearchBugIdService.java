package com.example.demo.api;

import com.example.demo.Dao.BugInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchBugIdService {
    @Autowired
    BugInfoDao bugInfoDao;

    public long getBugId(String filename , int start) {
        return bugInfoDao.findBugsByFileNameAndStart(filename ,start).getId();
    }
}
