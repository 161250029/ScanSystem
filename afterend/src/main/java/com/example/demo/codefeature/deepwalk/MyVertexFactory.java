package com.example.demo.codefeature.deepwalk;


import org.deeplearning4j.graph.api.Vertex;
import org.deeplearning4j.graph.vertexfactory.VertexFactory;

import java.util.List;

public class MyVertexFactory<String> implements VertexFactory<String> {

    private List<String> vertexList;

    MyVertexFactory(List<String> vertexList) {
        this.vertexList = vertexList;
    }

    @Override
    public Vertex<String> create(int i) {
        return new Vertex<>(i, this.vertexList.get(i));
    }

}
