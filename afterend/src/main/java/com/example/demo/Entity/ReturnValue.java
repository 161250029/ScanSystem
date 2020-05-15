package com.example.demo.Entity;

import java.util.List;

public class ReturnValue {
    private int code;
    private String msg;
    private int count;
    private List<BugCollect> data;

    public ReturnValue(int code, String msg, int count, List<BugCollect> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public ReturnValue() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BugCollect> getData() {
        return data;
    }

    public void setData(List<BugCollect> data) {
        this.data = data;
    }
}
