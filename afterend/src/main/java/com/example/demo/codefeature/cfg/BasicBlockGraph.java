package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.Node;
import lombok.Getter;

import java.util.*;

public class BasicBlockGraph {

    // 基本块队列
    @Getter
    private List<BasicBlock> basicBlocks= new ArrayList<>();
    // 保存指向终点的基本块
    private List<BasicBlock> endBlocks = new ArrayList<>();

    public BasicBlockGraph(CFG cfg) {
        int p = 0;
        // 控制流图的源节点入队
        List<Node> nodeQueue = new ArrayList<>();
        nodeQueue.add(cfg.getSourceNode());
        basicBlocks.add(new BasicBlock());

        while (p < nodeQueue.size()) {
            // 节点出队并取得对应的基本块
            CFGNode node = cfg.findNode(nodeQueue.get(p));
            BasicBlock basicBlock = basicBlocks.get(p);
            p ++;

            while (true) {
//                int inDegree = node.getPreNodes().size() + (cfg.getSourceNode() == node.getNode() ? 1 : 0);
                // 计算出度
                int outDegree = node.getNextNodes().size();
                // 如果节点指向终点，出度+1并记录此基本块
                if (cfg.getSinkNodes().contains(node.getNode())) {
                    outDegree ++;
                    endBlocks.add(basicBlock);
                }
                // 当前节点加入基本块
                basicBlock.addNode(node.getNode());

                if (outDegree > 1) {
                    // 遍历所有下一节点，不在队列中的入队
                    for (Node n: node.getNextNodes()) {
                        this.checkAndPush(nodeQueue, n, basicBlock);
                    }
                    break;
                } else {
                    if (cfg.getSinkNodes().contains(node.getNode())) break;
                    // 获得下一个节点
                    CFGNode nextNode = cfg.findNode(node.getNextNodes().get(0));
                    if (nextNode.getPreNodes().size() > 1) {
                        // 入度不唯一表明属于另一个基本块，检查并入队
                        this.checkAndPush(nodeQueue, nextNode.getNode(), basicBlock);
                        break;
                    } else {
                        // 否则将当前节点指向下一节点
                        node = nextNode;
                    }
                }
            }
        }
    }

    public List<List<Integer>> getEdges() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < this.basicBlocks.size(); i++) {
            result.add(Arrays.asList(i, i));
            for (BasicBlock block: this.basicBlocks.get(i).getNextBlocks()) {
                result.add(Arrays.asList(i, this.basicBlocks.indexOf(block)));
            }
        }
        return result;
    }

    public String toDot() {
        StringBuilder builder = new StringBuilder("digraph cfg {\n").append("node[shape=box];\n");
        // 输出节点
        for (int i = 0; i < this.basicBlocks.size(); i++) {
            String label = this.basicBlocks.get(i).toString();
            label = label.replace("\n", "\\l");
            label = label.replace("\"", "\\\"");
            builder.append(String.format("n%d[label=\"%s\"];\n", i, label));
        }
        // 输出边
        for (int i = 0; i < this.basicBlocks.size(); i++) {
            for (BasicBlock block: this.basicBlocks.get(i).getNextBlocks()) {
                int j = this.basicBlocks.indexOf(block);
                builder.append(String.format("n%d->n%d;\n", i, j));
            }
        }
        builder.append("end[label=\"End\"];\n");
        for (BasicBlock block: endBlocks) {
            int i = this.basicBlocks.indexOf(block);
            builder.append(String.format("n%d->end;\n", i));
        }
        // 结束
        builder.append("}\n");
        return builder.toString();
    }

    private int indexOfNode(List<Node> nodeList, Node node) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (node == nodeList.get(i)) return i;
        }
        return -1;
    }

    private void checkAndPush(List<Node> nodeQueue, Node node, BasicBlock basicBlock) {
        // node不在队列中入队并生成全新基本块
        if (this.indexOfNode(nodeQueue, node) < 0) {
            nodeQueue.add(node);
            BasicBlock newBlock = new BasicBlock();
            basicBlock.addNextBlock(newBlock);
            basicBlocks.add(newBlock);
        }
        // 在队列中则只增加基本块的边
        else {
            basicBlock.addNextBlock(basicBlocks.get(this.indexOfNode(nodeQueue, node)));
        }
    }
}
