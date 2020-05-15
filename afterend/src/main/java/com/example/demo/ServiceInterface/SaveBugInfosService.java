package com.example.demo.ServiceInterface;


public interface SaveBugInfosService {
    public void save_find_sec_bugs();
    public void save_spot_bugs();
    public void save_find_bugs();

    public void dealdata(String spot_path , String find_path , String dirpath , String jarpath) throws Exception;
}
