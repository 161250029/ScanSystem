package com.example.demo.codefeature.api;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class AddModelRequest {

    private List<String> models;
    private int featureSize;
    private int epochNum;

}
