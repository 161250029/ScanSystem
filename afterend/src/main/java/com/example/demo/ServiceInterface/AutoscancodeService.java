package com.example.demo.ServiceInterface;

import com.example.demo.Entity.ReturnXmlPathInfo;

public interface AutoscancodeService {
    public ReturnXmlPathInfo ScanUploadJar(String JarPath);
    public void ScanCode(String save_path);
}
