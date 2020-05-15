package com.example.demo.Entity;

import java.util.List;

public class ReturnGraph {
    private List<String> type;
    private List<Integer> count;

    public ReturnGraph(List<String> type, List<Integer> count) {
        this.type = type;
        this.count = count;
    }

    public ReturnGraph() {
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }
}
