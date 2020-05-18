package com.example.demo.Controller;

import com.example.demo.Dao.ProjectDao;
import com.example.demo.Entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectDao projectDao;

    @RequestMapping(value="/getProjects" , method = RequestMethod.GET)
    @ResponseBody
    public List<Project> getProjects() {
        return projectDao.findAll();
    }

    @RequestMapping(value="/saveproject" , method = RequestMethod.GET)
    @ResponseBody
    public void saveProject(@RequestParam("projectname")String projectname , @RequestParam("projectdesc")String projectdesc
    ,@RequestParam("filepath") String filepath) throws UnsupportedEncodingException {
        System.out.println("开始保存任务");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        Project project = new Project();
        project.setDate(date);
        project.setProjectname(projectname);
        project.setProjectdesc(projectdesc);
        project.setProjectjarpath(filepath);
        project.setProgress("doing");
        projectDao.saveAndFlush(project);
    }

    @RequestMapping(value="/updateproject" , method = RequestMethod.GET)
    @ResponseBody
    public List<Project> updateProject(@RequestParam("projectname")String projectname) throws UnsupportedEncodingException {
        Project project = projectDao.findProjectbyName(projectname);
        project.setProgress("finished");
        projectDao.saveAndFlush(project);
        return projectDao.findAll();
    }

}
