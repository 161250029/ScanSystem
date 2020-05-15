package com.example.demo.Entity;

import javax.persistence.*;

@Entity
@Table(name = "BugCollect")
public class BugCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String toolName;
    private String jar_location;
    private String className;
    private String methodName;
    private String sourceFile;
    private String file_path;
    private int level;
    private int start;
    private String bugType;
    private String date;
    private boolean is_positive;

    public BugCollect(long id, String toolName, String jar_location, String className, String methodName, String sourceFile, String file_path, int level, int start, String bugType, String date, boolean is_positive) {
        this.id = id;
        this.toolName = toolName;
        this.jar_location = jar_location;
        this.className = className;
        this.methodName = methodName;
        this.sourceFile = sourceFile;
        this.file_path = file_path;
        this.level = level;
        this.start = start;
        this.bugType = bugType;
        this.date = date;
        this.is_positive = is_positive;
    }

    public BugCollect() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getBugType() {
        return bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJar_location() {
        return jar_location;
    }

    public void setJar_location(String jar_location) {
        this.jar_location = jar_location;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public boolean isIs_positive() {
        return is_positive;
    }

    public void setIs_positive(boolean is_positive) {
        this.is_positive = is_positive;
    }
}
