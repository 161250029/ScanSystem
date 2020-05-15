package com.example.demo.Tool;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileMappingTableTool {
    public static List<File> searchFiles(String dir_path) {
        File folder = new File(dir_path);
        List<File> result = new ArrayList<File>();
        if (folder.isFile())
            result.add(folder);
            //查找后缀名
        File[] subFolders = folder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return true;
                    }
                    if (file.getName().toLowerCase().endsWith("java")) {
                        return true;
                    }
                    return false;
                }
            });

        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                } else {
                    // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    result.addAll(searchFiles(file.getAbsolutePath()));
                }
            }
        }
        return result;
    }
    public static Map<String , String> table(String dir_path) {
        List<File> result = searchFiles(dir_path);
        Map<String , String> maptable = new HashMap<>();
        for (File file : result) {
            maptable.put(file.getName() , file.getAbsolutePath());
        }
        return maptable;
    }
    public static void main(String[] args) {
        List<File> files = searchFiles("D:\\haorui的解压文件\\haorui");
        System.out.println("共找到:" + files.size() + "个文件");
        for (File file : files) {
            System.out.println(file.getAbsolutePath() + file.getName());
        }
    }
}
