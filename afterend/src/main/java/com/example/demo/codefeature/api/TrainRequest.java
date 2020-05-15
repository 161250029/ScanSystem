package com.example.demo.codefeature.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class TrainRequest {

    private String outputPath;
    private String modelPath;
    private int epochNum;
    private int featureSize;
    private boolean valid;

}
