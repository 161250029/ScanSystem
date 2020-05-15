package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import lombok.Getter;

import java.util.*;

public class CFG {

    @Getter
    private Node sourceNode;
    @Getter
    private List<Node> sinkNodes = new ArrayList<>();
    @Getter
    private List<CFGNode> vertexList = new ArrayList<>();
    @Getter
    private List<CFGNode> breakNodes = new ArrayList<>();

    void setSourceNode(Node node) {
        this.sourceNode = node;
    }

    void addSinkNode(Node node) {
        this.sinkNodes.add(node);
    }

    void addNode(Node node) {
        if (! this.containNode(node)) {
            this.vertexList.add(new CFGNode(node));
        }
    }

    void addBreak(BreakStmt breakStmt) {
        this.breakNodes.add(new CFGNode(breakStmt));
    }

    void addEdge(Node fromNode, Node toNode) {
        if (! this.containNode(fromNode)) {
            this.vertexList.add(new CFGNode(fromNode));
        }
        if (! this.containNode(toNode)) {
            this.vertexList.add(new CFGNode(toNode));
        }
        CFGNode v1 = this.findNode(fromNode);
        CFGNode v2 = this.findNode(toNode);
        v1.addNextNode(toNode);
        v2.addPreNode(fromNode);
    }

    private void removeNode(Node node) {
        CFGNode cfgNode = this.findNode(node);
        this.vertexList.remove(cfgNode);
        for (CFGNode v: this.vertexList) {
            if (v.getPreNodes().contains(node))
                v.removePreNode(node);
            if (v.getNextNodes().contains(node))
                v.removeNextNode(node);
        }
    }

    void mergeCFG(Node node, CFG cfg) {
        CFGNode cfgNode = this.findNode(node);
        if (cfgNode == null) return;

        // 考虑对待合并CFG中特殊节点的处理
        if (this.isSpecialStmt(node)) {
            // 合并节点是循环节点时进行处理
            for (CFGNode n: cfg.getBreakNodes()) {
                // 删除Break的所有出边，并设为结束点
                cfg.findNode(n.getNode()).clearNextNode();
                if (this.findNodeInList(this.breakNodes, n.getNode()) == null) {
                    cfg.sinkNodes.add(n.getNode());
                }
            }
        } else {
            // 否则保留
            this.breakNodes.addAll(cfg.getBreakNodes());
        }
        // 替换的节点是起点或终点的一部分时需要更新
        if (node == this.sourceNode) {
            this.sourceNode = cfg.getSourceNode();
        }
        if (this.sinkNodes.remove(node)) {
            this.sinkNodes.addAll(cfg.getSinkNodes());
        }
        // 将所给图的两端合并入当前图
        for (Node n1: cfgNode.getPreNodes()) {
                this.addEdge(n1, cfg.getSourceNode());
        }
        for (Node n1: cfgNode.getNextNodes()) {
            for (Node n2: cfg.getSinkNodes()) {
                this.addEdge(n2, n1);
            }
        }
        // 删去替换的节点
        this.removeNode(node);
        // 把所给图中剩余的节点关系合并到本图中
        for (CFGNode v1: cfg.getVertexList()) {
            if (this.containNode(v1.getNode())) {
                CFGNode v2 = this.findNode(v1.getNode());
                v2.mergeNode(v1);
            } else {
                this.vertexList.add(v1);
            }
        }
    }

    void handleReturn() {
        for (CFGNode n: this.getVertexList()) {
            // 删除Return的所有出边，并设为结束点
            if (n.getNode() instanceof ReturnStmt) {
                n.clearNextNode();
                boolean flag = true;
                for (Node node: this.sinkNodes) {
                    if (node == n.getNode()) flag = false;
                }
                if (flag) this.sinkNodes.add(n.getNode());
            }
        }
    }

    public String toDot() {
        StringBuilder builder = new StringBuilder("digraph cfg {\n").append("node[shape=box];\n");
        // 输出节点
        for (int i = 0; i < this.vertexList.size(); i++) {
            String label = this.vertexList.get(i).toString();
            label = label.replace("\n", "\\l");
            label = label.replace("\"", "\\\"");
            builder.append(String.format("n%d[label=\"%s\"];\n", i, label));
        }
        // 输出边
        for (int i = 0; i < this.vertexList.size(); i++) {
            for (Node node: this.vertexList.get(i).getNextNodes()) {
                int j = this.vertexList.indexOf(this.findNode(node));
                builder.append(String.format("n%d->n%d;\n", i, j));
            }
        }
        // 输出起点和终点
        builder.append("start[label=\"Start\"];\n");
        builder.append("end[label=\"End\"];\n");
        builder.append(String.format("start->n%d;\n", this.vertexList.indexOf(this.findNode(this.sourceNode))));
        for (Node node: this.sinkNodes) {
            int index = this.vertexList.indexOf(this.findNode(node));
            builder.append(String.format("n%d->end;\n", index));
        }
        // 结束
        builder.append("}\n");
        return builder.toString();
    }


    private boolean isSpecialStmt(Node node) {
        return (node instanceof DoStmt) || (node instanceof WhileStmt) || (node instanceof ForStmt) || (node instanceof SwitchStmt);
    }

    private CFGNode findNodeInList(List<CFGNode> list, Node node) {
        for (CFGNode v: list) {
            if (v.equalNode(node)) {
                return v;
            }
        }
        return null;
    }

    CFGNode findNode(Node node) {
        return this.findNodeInList(this.vertexList, node);
    }

    private boolean containNode(Node node) {
        return this.findNode(node) != null;
    }

}
