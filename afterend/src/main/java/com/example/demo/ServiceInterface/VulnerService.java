package com.example.demo.ServiceInterface;

import com.example.demo.Entity.Vulnerability;

import java.util.List;

public interface VulnerService {

    public void readDataset(String path);

    public void probUpdate(Long id, double prob);

    public List<Vulnerability> loadDataset();

}
