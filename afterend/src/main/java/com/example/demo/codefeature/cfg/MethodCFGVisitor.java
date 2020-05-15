package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.stream.Collectors;

public class MethodCFGVisitor extends VoidVisitorAdapter<CFG> {

    @Override
    public void visit(MethodDeclaration method, CFG cfg) {
        if (method.getBody().isPresent()) {
            BlockStmt blockStmt = method.getBody().get();
            cfg.addNode(blockStmt);
            cfg.setSourceNode(blockStmt);
            cfg.addSinkNode(blockStmt);
            super.visit(method, cfg);
        }
    }

    @Override
    public void visit(BlockStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        this.handleNodeList(stmt.getStatements(), thisCFG);
        super.visit(stmt, thisCFG);
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(IfStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        thisCFG.setSourceNode(stmt.getCondition());
        thisCFG.addEdge(stmt.getCondition(), stmt.getThenStmt());
        if (stmt.getElseStmt().isPresent()) {
            thisCFG.addEdge(stmt.getCondition(), stmt.getElseStmt().get());
            thisCFG.addSinkNode(stmt.getElseStmt().get());
        } else {
            thisCFG.addSinkNode(stmt.getCondition());
        }
        thisCFG.addSinkNode(stmt.getThenStmt());
        super.visit(stmt, thisCFG);
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(SwitchStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        this.handleNodeList(stmt.getEntries(), thisCFG);
        super.visit(stmt, thisCFG);
//        System.out.println(cfg.toDot());
//        System.out.println(thisCFG.toDot());
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(SwitchEntry entry, CFG cfg) {
        CFG thisCFG = new CFG();
        SwitchStmt stmt = (SwitchStmt) entry.getParentNode().get();
        this.handleNodeList(entry.getStatements(), thisCFG);
        if (entry.getLabels().size() != 0) {
            BinaryExpr condition = null;
            for (Expression expression: entry.getLabels()) {
                BinaryExpr labelExpr = new BinaryExpr();
                labelExpr.setLeft(stmt.getSelector());
                labelExpr.setOperator(BinaryExpr.Operator.EQUALS);
                labelExpr.setRight(expression);
                if (condition == null) {
                    condition = labelExpr;
                } else {
                    condition.setLeft(condition.clone());
                    condition.setOperator(BinaryExpr.Operator.OR);
                    condition.setRight(labelExpr);
                }
            }
            thisCFG.setSourceNode(condition);
            thisCFG.addEdge(condition, entry.getStatements().get(0));
            thisCFG.addSinkNode(condition);
        }
        super.visit(entry, thisCFG);
        cfg.mergeCFG(entry, thisCFG);
    }

    @Override
    public void visit(WhileStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        thisCFG.setSourceNode(stmt.getCondition());
        thisCFG.addEdge(stmt.getCondition(), stmt.getBody());
        thisCFG.addEdge(stmt.getBody(), stmt.getCondition());
        thisCFG.addSinkNode(stmt.getCondition());
        super.visit(stmt, thisCFG);
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(DoStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        thisCFG.setSourceNode(stmt.getBody());
        thisCFG.addEdge(stmt.getBody(), stmt.getCondition());
        thisCFG.addEdge(stmt.getCondition(), stmt.getBody());
        thisCFG.addSinkNode(stmt.getCondition());
        super.visit(stmt, thisCFG);
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(ForStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        BlockStmt initStmt = this.expressionsToBlock(stmt.getInitialization());
        BlockStmt updateSmt = this.expressionsToBlock(stmt.getUpdate());
        thisCFG.setSourceNode(initStmt);

        if (stmt.getCompare().isPresent()) {
            thisCFG.addEdge(initStmt, stmt.getCompare().get());
            thisCFG.addEdge(stmt.getCompare().get(), stmt.getBody());
            thisCFG.addEdge(stmt.getBody(), updateSmt);
            thisCFG.addEdge(updateSmt, stmt.getCompare().get());
            thisCFG.addSinkNode(stmt.getCompare().get());
        } else {
            thisCFG.addEdge(initStmt, stmt.getBody());
            thisCFG.addEdge(stmt.getBody(), stmt.getBody());
            thisCFG.addSinkNode(stmt.getBody());
        }
        super.visit(stmt, thisCFG);
        this.visit(initStmt, thisCFG);
        this.visit(updateSmt, thisCFG);
        cfg.mergeCFG(stmt, thisCFG);
    }

    @Override
    public void visit(BreakStmt stmt, CFG cfg) {
        cfg.addBreak(stmt);
        super.visit(stmt, cfg);
    }

    @Override
    public void visit(ContinueStmt stmt, CFG cfg) {

    }

    @Override
    public void visit(TryStmt stmt, CFG cfg) {
        CFG thisCFG = new CFG();
        thisCFG.setSourceNode(stmt.getTryBlock());
        thisCFG.addNode(stmt.getTryBlock());
        if (stmt.getFinallyBlock().isPresent()) {
            thisCFG.addEdge(stmt.getTryBlock(), stmt.getFinallyBlock().get());
            thisCFG.addSinkNode(stmt.getFinallyBlock().get());
        } else {
            thisCFG.addSinkNode(stmt.getTryBlock());
        }
        cfg.mergeCFG(stmt, thisCFG);
        super.visit(stmt, cfg);
    }

    private void handleNodeList(NodeList nodeList, CFG cfg) {
        for (int i = 0; i < nodeList.size(); i++) {
            cfg.addNode(nodeList.get(i));
            if (i > 0) {
                cfg.addEdge(nodeList.get(i-1), nodeList.get(i));
            }
        }
        cfg.setSourceNode(nodeList.get(0));
        cfg.addSinkNode(nodeList.get(nodeList.size() - 1));
    }

    private BlockStmt expressionsToBlock(NodeList<Expression> expressions) {
        NodeList<Statement> expressionStmts
                = expressions.stream().map(ExpressionStmt::new).collect(Collectors.toCollection(NodeList::new));
        return new BlockStmt(expressionStmts);
    }

}
