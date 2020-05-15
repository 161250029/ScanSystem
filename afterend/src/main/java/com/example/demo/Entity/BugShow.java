package com.example.demo.Entity;

public class BugShow {
    private long id;
    private String toolName;
    private String methodName;
    private String sourceFile;
    private int level;
    private int start;
    private String bugType;

    public BugShow(long id, String toolName, String methodName, String sourceFile, int level, int start, String bugType) {
        this.id = id;
        this.toolName = toolName;
        this.methodName = methodName;
        this.sourceFile = sourceFile;
        this.level = level;
        this.start = start;
        this.bugType = bugType;
    }

    public BugShow() {
    }
    public BugShow(BugInfo bugInfo) {
        this.id = bugInfo.getId();
        this.toolName = bugInfo.getToolName();
        this.methodName = bugInfo.getMethodName();
        this.sourceFile = bugInfo.getSourceFile();
        this.level = bugInfo.getLevel();
        this.start = bugInfo.getStart();
        this.bugType = bugInfo.getBugType();
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
}
