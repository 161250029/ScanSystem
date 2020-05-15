package com.example.demo.codefeature.tools;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;

public class CommentTools {

    public static void removeComment(MethodDeclaration declaration) {
        declaration.getAllContainedComments().forEach(Comment::remove);
    }

}
