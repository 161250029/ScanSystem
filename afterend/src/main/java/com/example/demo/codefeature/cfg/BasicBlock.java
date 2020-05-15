package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

    private List<Node> nodeList = new ArrayList<>();
    @Getter
    private List<BasicBlock> nextBlocks = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Node node: this.nodeList) {
            builder.append(node.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    void addNode(Node node) {
        this.nodeList.add(node);
    }

    void addNextBlock(BasicBlock block) {
        this.nextBlocks.add(block);
    }
}
