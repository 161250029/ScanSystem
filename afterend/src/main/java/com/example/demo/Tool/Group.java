package com.example.demo.Tool;

public class Group {

    public String id;
    public int size;
    public String mode;

    public Group(String id, int size, String mode) {
        this.id = id;
        this.size = size;
        this.mode = mode;
    }

    public Group(String id, int size) {
        this.id = id;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
