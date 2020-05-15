package com.example.demo.codefeature.cfg;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.codefeature.tools.CommentTools;

public class CFGFeature {

    private static Logger logger = LoggerFactory.getLogger("CFGFeature");

    public static CFG extract(MethodDeclaration method) {
        // 清除注释
        CommentTools.removeComment(method);
        CFG cfg = new CFG();
        VoidVisitor<CFG> visitor = new MethodCFGVisitor();
        visitor.visit(method, cfg);
        cfg.handleReturn();
        logger.info("Extract CFG Feature From " + method.getNameAsString());
        return cfg;
    }

    public static BasicBlockGraph extractBasicBlocks(MethodDeclaration method) {
        try {
            CFG cfg = extract(method);
            logger.info("Extract Basic Graph From " + method.getNameAsString());
            return new BasicBlockGraph(cfg);
        } catch (Exception e) {
            return null;
        }
    }

}
