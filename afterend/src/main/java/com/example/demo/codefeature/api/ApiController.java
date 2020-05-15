package com.example.demo.codefeature.api;

import com.example.demo.codefeature.tools.FileTools;
import com.example.demo.codefeature.tools.PythonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Value("${pythonConfig.path}")
    private String pythonPath;
    @Value("${pythonConfig.train}")
    private String trainPath;
    @Value("${pythonConfig.predict}")
    private String predictPath;

    @PostMapping("/extract")
    public List<ExtractResult> extract(@RequestBody ExtractRequest request) {
        String fileSeparator = File.separator;
        String parentPath = request.getDataPath();
        String targetPath = request.getOutputPath();
        FileTools.vectorSize = request.getFeatureSize();

        List<ExtractResult> result = new ArrayList<>();
        for (String type : new String[]{"Positive", "False"}) {
            String outputDir = targetPath + fileSeparator + type;
            FileTools.checkOutputDir(outputDir);
            List<String> javaFilePaths = FileTools.searchJavaFile(parentPath + fileSeparator + type);
            logger.info("The count of java files: " + javaFilePaths.size());
            for (int i = 0; i < javaFilePaths.size(); i ++) {
                File javaFile = new File(javaFilePaths.get(i));
                logger.info("The index of File: " + i);
                ExtractResult extractResult = FileTools.saveFeature(javaFile, outputDir);
                extractResult.setFlag("Positive".equals(type) ? "正报" : "误报");
                result.add(extractResult);
                FileTools.saveExtractResult(extractResult, outputDir);
            }
        }
        return result;
    }

    @PostMapping("/train")
    public String train(@RequestBody TrainRequest request) {
        String[] params = {pythonPath, trainPath, request.getOutputPath(), request.getModelPath()
                , String.valueOf(request.getEpochNum()), String.valueOf(request.getFeatureSize())
                , request.isValid() ? "1" : ""};

        return PythonTools.execute(params);
    }

    @GetMapping("/modelNum")
    public String getModelNumber() {
        String[] params = {pythonPath, trainPath};
        return PythonTools.execute(params);
    }

    @PostMapping("/predict")
    public String predict(@RequestBody PredictRequest request) {
        String fileSeparator = File.separator;
        String modelPath = request.getModelPath();

        FileTools.checkOutputDir(modelPath + fileSeparator + "False");
        List<String> javaFilePaths = FileTools.searchJavaFile(request.getJavaFilePath());
        for (String fileName: javaFilePaths) {
            FileTools.saveFeature(new File(fileName), modelPath + fileSeparator + "False");
        }

        String[] params = {pythonPath, predictPath, modelPath};
        String line = PythonTools.execute(params);

        for (String type: new String[]{"Text", "WordVector", "Edge", "DeepWalk", "ParagraphVec"}) {

            new File(modelPath + fileSeparator + type).delete();
        }
        return line;
    }

}
