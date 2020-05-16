package com.example.demo.Tool;

public class Group {

    public String taskid;
    public int tasksize;
    public int taskprocess;
    public String mode;

    public Group(String taskid, int tasksize, int taskprocess, String mode) {
        this.taskid = taskid;
        this.tasksize = tasksize;
        this.taskprocess = taskprocess;
        this.mode = mode;
    }

    public Group(String taskid, int tasksize, int taskprocess) {
        this.taskid = taskid;
        this.tasksize = tasksize;
        this.taskprocess = taskprocess;
    }

    public int getTaskprocess() {
        return taskprocess;
    }

    public void setTaskprocess(int taskprocess) {
        this.taskprocess = taskprocess;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public int getTasksize() {
        return tasksize;
    }

    public void setTasksize(int tasksize) {
        this.tasksize = tasksize;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
