package com.example.demo.codefeature.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class PredictRequest {

    private String modelPath;
    private String javaFilePath;

}
