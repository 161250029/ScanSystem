package com.example.demo.codefeature.tools;

import com.example.demo.codefeature.api.ExtractResult;
import com.example.demo.codefeature.ast.ASTFeature;
import com.example.demo.codefeature.cfg.BasicBlock;
import com.example.demo.codefeature.cfg.BasicBlockGraph;
import com.example.demo.codefeature.cfg.CFGFeature;
import com.example.demo.codefeature.deepwalk.GraphFeature;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.example.demo.codefeature.word2vec.Word2VecModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileTools {

    private static final Logger logger = LoggerFactory.getLogger(FileTools.class);

    public static void checkOutputDir(String outputDir) {
        File target = new File(outputDir);
        if (! target.exists()) {
            target.mkdir();
        }
        for (String type: new String[]{"Text", "WordVector", "Edge", "DeepWalk", "ParagraphVec"}) {
            target = new File(outputDir + File.separator + type);
            if (! target.exists()) {
                target.mkdir();
            }
        }
    }

    public static List<String> searchJavaFile(String rootPath) {
        List<String> result = new ArrayList<>();

        LinkedList<File> queue = new LinkedList<>();
        queue.add(new File(rootPath));
        while (queue.size() > 0) {
            File dir = queue.pop();
            if ((! dir.exists()) || dir.isFile()) continue;
            for (File file: dir.listFiles()) {
                if (file.isDirectory()) {
                    queue.push(file);
                } else {
                    if (file.getName().matches("\\w*.java"))
                    result.add(file.getAbsolutePath());
                }
            }
        }
        return result;
    }

    public static ExtractResult saveFeature(File javaFile, String outputDir, int featureSize) {
        ExtractResult extractResult = new ExtractResult();
        String javaFileName = javaFile.getName().split("\\.")[0];
        extractResult.setName(javaFileName);

        logger.info("File: " + javaFileName);
        try {
            CompilationUnit cu = StaticJavaParser.parse(javaFile);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
            for (MethodDeclaration m : methodList) {
                if (m.isConstructorDeclaration()) continue;
                CommentTools.removeComment(m);
                SymbolTools.nameSubstitute(m);
                try {
                    String outputName = javaFileName + "_" + m.getNameAsString() + ".csv";
                    List<String> astWords = ASTFeature.extract(m);
                    extractResult.setSequence(astWords.size());
                    FileTools.saveASTWords(astWords, outputDir, outputName);
                    FileTools.saveWordVector(astWords, outputDir, outputName, featureSize);
                    BasicBlockGraph graph = CFGFeature.extractBasicBlocks(m);
                    if (graph == null) {
                        extractResult.setSuccess(false);
                        extractResult.setBasicBlock(0);
                    } else {
                        extractResult.setBasicBlock(graph.getBasicBlocks().size());
                        FileTools.saveGraph(graph, outputDir, outputName, featureSize);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e1) {
            logger.error("File Not Found: " + javaFile.getAbsolutePath());
            e1.printStackTrace();
            extractResult.setSuccess(false);
        } catch (ParseProblemException e2) {
            logger.error("Parser Error: " + javaFile.getAbsolutePath());
            e2.printStackTrace();
            extractResult.setSuccess(false);
        }
        return extractResult;
    }

    public static void saveExtractResult(ExtractResult result, String outputDir) {
        try {
            String outputPath = outputDir + File.separator + "result.csv";
            File file = new File(outputPath);
            if (! file.exists()) file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath, true)));
            FileTools.saveList(writer, Arrays.asList(result.getName(), ""+result.getSequence(), ""+result.getBasicBlock(), result.getFlag(), ""+result.isSuccess()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveASTWords(List<String> astWords, String outputDir, String outputName) throws IOException {
        String outputPath = String.join(File.separator, new String[]{outputDir, "Text", outputName});
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        FileTools.saveList(writer, astWords);
        writer.close();
    }

    private static void saveWordVector(List<String> words, String outputDir, String outputName, int featureSize) throws IOException {
        String outputPath = String.join(File.separator, new String[]{outputDir, "WordVector", outputName});
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
        for (List<Double> line: Word2VecModel.transformWords(words, featureSize)) {
            FileTools.saveList(writer, line);
        }
        writer.close();
    }

    private static void saveGraph(BasicBlockGraph graph, String outputDir, String outputName, int featureSize) throws IOException {
        String edgePath = String.join(File.separator, new String[]{outputDir, "Edge", outputName});
        String deepWalkPath = String.join(File.separator, new String[]{outputDir, "DeepWalk", outputName});
        String paragraphVecPath = String.join(File.separator, new String[]{outputDir, "ParagraphVec", outputName});

        BufferedWriter writer = new BufferedWriter(new FileWriter(edgePath));
        for (List<Integer> line: graph.getEdges()) {
            FileTools.saveList(writer, line);
        }
        writer.close();

        List<List<Double>> deepWalkResult = GraphFeature.extract(graph, featureSize);
        if (deepWalkResult.size() > 0) {
            writer = new BufferedWriter(new FileWriter(deepWalkPath));
            for (List<Double> line: deepWalkResult) {
                FileTools.saveList(writer, line);
            }
            writer.close();
        }

        writer = new BufferedWriter(new FileWriter(paragraphVecPath));
        List<String> paragraph = graph.getBasicBlocks()
                .stream()
                .map(BasicBlock::toString)
                .collect(Collectors.toList());
        for (List<Double> line: Word2VecModel.transformParagraph(paragraph, featureSize)) {
            FileTools.saveList(writer, line);
        }
        writer.close();
    }

    private static void saveList(BufferedWriter bw, List list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) bw.write(list.get(0).toString());
            else bw.write("," + list.get(i).toString());
        }
        bw.newLine();
    }
}

