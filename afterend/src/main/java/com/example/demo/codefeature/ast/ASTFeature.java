package com.example.demo.codefeature.ast;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ASTFeature {

    private static Logger logger = LoggerFactory.getLogger("ASTFeature");

    public static List<String> extract(MethodDeclaration method) {
        List<String> words = new ArrayList<>();
        VoidVisitor<List<String>> voidVisitor = new MethodVisitor();
        voidVisitor.visit(method, words);
        // 将字符串的空格替换为下划线，防止之后被分词
        words = words.stream()
                .map(s -> s.replace(' ', '_'))
                .collect(Collectors.toList());
        logger.info(method.getNameAsString() + words);
        return words;
    }

}
