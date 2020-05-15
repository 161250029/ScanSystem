package com.example.demo.codefeature.ast;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<List<String>> {

    public void visit(AssertStmt stmt, List<String> result) {
        result.add("assert");
        super.visit(stmt, result);
    }

    public void visit(BreakStmt stmt, List<String> result) {
        result.add("break");
        super.visit(stmt, result);
    }

    public void visit(CatchClause stmt, List<String> result) {
        result.add("catch");
        super.visit(stmt, result);
    }

    public void visit(ContinueStmt stmt, List<String> result) {
        result.add("continue");
        super.visit(stmt, result);
    }

    public void visit(DoStmt stmt, List<String> result) {
        result.add("do");
        super.visit(stmt, result);
    }

    public void visit(ForEachStmt stmt, List<String> result) {
        result.add("forEach");
        super.visit(stmt, result);
    }

    public void visit(ForStmt stmt, List<String> result) {
        result.add("for");
        super.visit(stmt, result);
    }

    public void visit(IfStmt stmt, List<String> result) {
        result.add("if");
        super.visit(stmt, result);
    }

    public void visit(LabeledStmt stmt, List<String> result) {
        result.add("label");
        super.visit(stmt, result);
    }

    public void visit(ReturnStmt stmt, List<String> result) {
        result.add("return");
        super.visit(stmt, result);
    }

    public void visit(SwitchEntry stmt, List<String> result) {
        result.add("case");
        super.visit(stmt, result);
    }

    public void visit(SwitchStmt stmt, List<String> result) {
        result.add("switch");
        super.visit(stmt, result);
    }

    public void visit(TryStmt stmt, List<String> result) {
        result.add("try");
        super.visit(stmt, result);
    }

    public void visit(WhileStmt stmt, List<String> result) {
        result.add("while");
        super.visit(stmt, result);
    }

    public void visit(ArrayCreationExpr expr, List<String> result) {
        result.add("array");
        result.add(expr.getElementType().asString());
        super.visit(expr, result);
    }

    public void visit(AssignExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(BinaryExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(CastExpr expr, List<String> result) {
        result.add(expr.toString());
        super.visit(expr, result);
    }

    public void visit(CharLiteralExpr expr, List<String> result) {
        result.add(Character.toString(expr.asChar()));
        super.visit(expr, result);
    }

    public void visit(ClassExpr expr, List<String> result) {
        result.add(expr.toString());
        super.visit(expr, result);
    }

    public void visit(FieldAccessExpr expr, List<String> result) {
        result.add(expr.getName().asString());
        super.visit(expr, result);
    }

    public void visit(IntegerLiteralExpr expr, List<String> result) {
        result.add(Integer.toString(expr.asInt()));
        super.visit(expr, result);
    }

    public void visit(MethodCallExpr expr, List<String> result) {
        result.add(expr.getName().asString());
        super.visit(expr, result);
    }

    public void visit(NameExpr expr, List<String> result) {
        result.add(expr.getName().asString());
        super.visit(expr, result);
    }

    public void visit(ObjectCreationExpr expr, List<String> result) {
        result.add(expr.getType().asString());
        super.visit(expr, result);
    }

    public void visit(StringLiteralExpr expr, List<String> result) {
        result.add(expr.asString());
        super.visit(expr, result);
    }

    public void visit(SuperExpr expr, List<String> result) {
        result.add("super");
        super.visit(expr, result);
    }

    public void visit(ThisExpr expr, List<String> result) {
        result.add("this");
        super.visit(expr, result);
    }

    public void visit(UnaryExpr expr, List<String> result) {
        result.add(expr.getOperator().asString());
        super.visit(expr, result);
    }

    public void visit(VariableDeclarator declarator, List<String> result) {
        result.add(declarator.getType().asString());
        result.add(declarator.getName().asString());
        super.visit(declarator, result);
    }

}
