package com.example.demo.codefeature.tools;

import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import java.util.HashMap;
import java.util.Map;

public class SymbolVisitor extends ModifierVisitor<Void> {

    private Map<String, Integer> numberMap = new HashMap<>();
    private Map<String, String> nameMap = new HashMap<>();

    public VariableDeclarator visit(VariableDeclarator declarator, Void arg) {
        putSymbol(declarator.getTypeAsString(), declarator.getNameAsString());
        super.visit(declarator, arg);
        return declarator;
    }

    public Parameter visit(Parameter parameter, Void arg) {
        putSymbol(parameter.getTypeAsString(), parameter.getNameAsString());
        super.visit(parameter, arg);
        return parameter;
    }

    public SimpleName visit(SimpleName name, Void arg) {
        if (nameMap.containsKey(name.asString())) {
            name.setIdentifier(nameMap.get(name.asString()));
        }
        super.visit(name, arg);
        return name;
    }

    private void putSymbol(String type, String oldName) {
        int num = 1;
        if (numberMap.containsKey(type)) {
            num = numberMap.get(type);
            num ++;
            numberMap.put(type, num);
        } else {
            numberMap.put(type, 1);
        }
        String newName = type.toLowerCase() + num;
        nameMap.put(oldName, newName);
    }
}
