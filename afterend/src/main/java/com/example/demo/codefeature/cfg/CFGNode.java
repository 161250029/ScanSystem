package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.Node;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CFGNode {

    @Getter
    private Node node;
    @Getter
    private List<Node> preNodes = new ArrayList<>();
    @Getter
    private List<Node> nextNodes = new ArrayList<>();

    CFGNode(Node node) {
        this.node = node;
    }

    public String toString() {
        return this.node.toString();
    }

    void addPreNode(Node node) {
        if (! this.preNodes.contains(node))
            this.preNodes.add(node);
    }

    void addNextNode(Node node) {
        if (! this.nextNodes.contains(node))
            this.nextNodes.add(node);
    }

    void removePreNode(Node node) {
        this.preNodes.remove(node);
    }

    void removeNextNode(Node node) {
        this.nextNodes.remove(node);
    }

    void clearNextNode() {
        this.nextNodes.clear();
    }

    boolean equalNode(Node node) {
        return this.node == node;
    }

    void mergeNode(CFGNode cfgNode) {
        this.preNodes.addAll(cfgNode.getPreNodes());
        this.nextNodes.addAll(cfgNode.getNextNodes());
    }

}
