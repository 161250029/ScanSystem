package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "Project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String projectname;
    private String projectdesc;
    @Column(length=3600, nullable=true)
    private String projectjarpath;
    private String progress;
    private String date;

    public Project(long id, String projectname, String projectdesc, String projectjarpath, String progress, String date) {
        this.id = id;
        this.projectname = projectname;
        this.projectdesc = projectdesc;
        this.projectjarpath = projectjarpath;
        this.progress = progress;
        this.date = date;
    }

    public Project() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectdesc() {
        return projectdesc;
    }

    public void setProjectdesc(String projectdesc) {
        this.projectdesc = projectdesc;
    }

    public String getProjectjarpath() {
        return projectjarpath;
    }

    public void setProjectjarpath(String projectjarpath) {
        this.projectjarpath = projectjarpath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
