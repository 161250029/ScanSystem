package com.example.demo.Entity;

public class ReturnXmlPathInfo {
    private String spot_path;
    private String find_path;

    public ReturnXmlPathInfo(String spot_path, String find_path) {
        this.spot_path = spot_path;
        this.find_path = find_path;
    }

    public ReturnXmlPathInfo() {
    }

    public String getSpot_path() {
        return spot_path;
    }

    public void setSpot_path(String spot_path) {
        this.spot_path = spot_path;
    }

    public String getFind_path() {
        return find_path;
    }

    public void setFind_path(String find_path) {
        this.find_path = find_path;
    }
}
