package com.example.demo.codefeature.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class ModelObject {

    static String NOT_TRAINING = "未训练";
    static String TRAINING = "进行中";
    static String TRAINED = "已训练";

    private String name;
    private int featureSize;
    private int epochNum;
    private String state;

}
