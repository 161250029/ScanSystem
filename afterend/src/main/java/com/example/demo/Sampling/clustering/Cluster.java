package com.example.demo.Sampling.clustering;




import com.example.demo.Tool.Vulnerability;

import java.util.ArrayList;

public class Cluster {
    public int index;
    public Vulnerability medoid;
    public ArrayList<Vulnerability> elements;
    public ArrayList<Double> dissimilarity;

    public Cluster(Vulnerability medoid, int index) {
        this.index = index;
        this.medoid = medoid;
    }

    public String toString(){

        String res = "=======================";
        res += "Cluster: "+index + "\r\n";
        res += "Medoid: " + medoid.id +  "\r\n";
        res += "Elements:"+  "\r\n";
        for(int i = 0; i < elements.size(); i++){
            res += "Element"+ elements.get(i).id +  "\r\n";
        }

        return res;
    }


    public Cluster(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vulnerability getMedoid() {
        return medoid;
    }

    public void setMedoid(Vulnerability medoid) {
        this.medoid = medoid;
    }

    public ArrayList<Vulnerability> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Vulnerability> elements) {
        this.elements = elements;
    }

    public ArrayList<Double> getDissimilarity() {
        return dissimilarity;
    }

    public void setDissimilarity(ArrayList<Double> dissimilarity) {
        this.dissimilarity = dissimilarity;
    }




}