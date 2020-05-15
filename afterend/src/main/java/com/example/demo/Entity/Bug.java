package com.example.demo.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Bugs")
public class Bug {

    @Id
    private Long id;

    private int threatlevel;
    private String type;
    private String location;
    private String code;
    public double probability;
    public boolean isEdit;
    private String comment;
    private String cluster;


    public Bug(Long id, int threatlevel, String type, String location, String code, double probability, boolean isEdit, String comment, String cluster) {
        this.id = id;
        this.threatlevel = threatlevel;
        this.type = type;
        this.location = location;
        this.code = code;
        this.probability = probability;
        this.isEdit = isEdit;
        this.comment = comment;
        this.cluster = cluster;
    }

    public Bug() {
    }

    public Bug(Long id, int threatlevel, String type, String location, String code, double probability) {
        this.id = id;
        this.threatlevel = threatlevel;
        this.type = type;
        this.location = location;
        this.code = code;
        this.probability = probability;
    }

    public Bug(Long id, int threatlevel, String type, String location, String code, double probability, boolean isEdit) {
        this.id = id;
        this.threatlevel = threatlevel;
        this.type = type;
        this.location = location;
        this.code = code;
        this.probability = probability;
        this.isEdit = isEdit;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getThreatlevel() {
        return threatlevel;
    }

    public void setThreatlevel(int threatlevel) {
        this.threatlevel = threatlevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
