package com.example.demo.ServiceInterface;


import com.example.demo.Entity.Bug;
import com.example.demo.Tool.Group;
import com.example.demo.Tool.Type;

import java.util.List;

public interface GetCodeService {
    public String getCodeService(String id, String path);

    public void dataUpdate(String id, boolean res);

    public List<Bug> getBugs(String groupid);

    public void submitBug(String id, String type, String level, String comments);

    public List<Type> getstatics();

    public List<Group> getGroups();
}
