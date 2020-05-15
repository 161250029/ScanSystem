package com.example.demo.Tool;

public class Type {

    public String typename;

    public int num;

    public Type(String name) {
        this.typename = name;
        this.num = 0;
    }

    public Type(String name, int num) {
        this.typename = name;
        this.num = num;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
