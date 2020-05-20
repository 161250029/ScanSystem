package com.example.demo.codefeature.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.Dao.BugInfoDao;
import com.example.demo.Dao.VulnerabilityDao;
import com.example.demo.Entity.BugInfo;
import com.example.demo.Entity.Vulnerability;
import com.example.demo.ServiceInterface.VulnerService;
import com.example.demo.codefeature.tools.FileTools;
import com.example.demo.codefeature.tools.PythonTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Value("${pythonPath}")
    private String pythonPath;
    @Value("${fileConfig.trainDataPath}")
    private String trainDataPath;
    @Value("${fileConfig.tempPath}")
    private String tempPath;
    @Value("${fileConfig.modelPath}")
    private String modelPath;
    @Autowired
    private BugInfoDao bugInfoDao;
    @Autowired
    private VulnerService vulnerService;

    private List<ModelObject> modelList = new ArrayList<>();
    private String predictPath = "";
    private String predictResult = "";

    @PostMapping("/addModel")
    public void addModel(@RequestBody ModelObject model) {
        this.modelList.add(model);
    }

    @GetMapping("/trainList")
    public List<ModelObject> getTrainList() {
        List<ModelObject> result = this.modelList.stream()
                .filter(m -> ! ModelObject.TRAINED.equals(m.getState()))
                .collect(Collectors.toList());
        File modelDir = new File(this.modelPath);
        if (modelDir.isDirectory()) {
            for (File f : modelDir.listFiles()) {
                if (f.getName().matches("\\w*.h5")) {
                    String[] info = f.getName().split("\\.")[0].split("_");
                    boolean flag = true;
                    for (ModelObject m : result) {
                        if (m.getName().equals(info[0]) && m.getFeatureSize() == Integer.parseInt(info[1])
                                && m.getEpochNum() == Integer.parseInt(info[2])) {
                            m.setState(ModelObject.TRAINED);
                            flag = false;
                        }
                    }
                    if (flag)
                        result.add(new ModelObject(info[0], Integer.parseInt(info[1]),
                            Integer.parseInt(info[2]), ModelObject.TRAINED));
                }
            }
        }
        this.modelList = result;
        return result;
    }

    @PostMapping("/detail")
    public String getDetail(@RequestBody ModelObject model) {
        String fileName = String.join("_", new String[]{model.getName(), ""+model.getFeatureSize(), ""+model.getEpochNum()});
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.modelPath + File.separator + fileName));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @PostMapping("/extract")
    public List<ExtractResult> extract(@RequestBody ExtractRequest request) {
        String fileSeparator = File.separator;
        String parentPath = request.getDataPath();
        String targetPath = request.getOutputPath();

        List<ExtractResult> result = new ArrayList<>();
        for (String type : new String[]{"Positive", "False"}) {
            String outputDir = targetPath + fileSeparator + type;
            FileTools.checkOutputDir(outputDir);
            List<String> javaFilePaths = FileTools.searchJavaFile(parentPath + fileSeparator + type);
            logger.info("The count of java files: " + javaFilePaths.size());
            for (int i = 0; i < javaFilePaths.size(); i ++) {
                File javaFile = new File(javaFilePaths.get(i));
                logger.info("The index of File: " + i);
                FileTools.saveFeature(javaFile, outputDir + fileSeparator + "8", 8);
                FileTools.saveFeature(javaFile, outputDir + fileSeparator + "16", 16);
                ExtractResult extractResult = FileTools.saveFeature(javaFile, outputDir + fileSeparator + "32", 32);
                extractResult.setFlag("Positive".equals(type) ? "正报" : "误报");
                result.add(extractResult);
                FileTools.saveExtractResult(extractResult, outputDir);
            }
        }
        return result;
    }

    @PostMapping("/train")
    public void train(@RequestBody ModelObject model) {
        for (ModelObject m : this.modelList) {
            if (model.getName().equals(m.getName()) && model.getFeatureSize() == m.getFeatureSize() && m.getEpochNum() == model.getEpochNum())
                m.setState(ModelObject.TRAINING);
        }
        String[] params = {pythonPath, "./python/Train.py", this.trainDataPath + File.separator + model.getFeatureSize(), this.modelPath, model.getName()
                , String.valueOf(model.getEpochNum()), String.valueOf(model.getFeatureSize())};
        PythonTools.execute(params);
    }

    @GetMapping("/modelNum")
    public String getModelNumber() {
        String[] params = {pythonPath, "./python/Train.py"};
        return PythonTools.execute(params);
    }

    @GetMapping("/modelName")
    public String getModelName() {
        List<String> names = new ArrayList<>();
        File modelDir = new File(this.modelPath);
        if (modelDir.isDirectory()) {
            for (File f : modelDir.listFiles()) {
                if (f.getName().matches("\\w*.h5")) {
                    names.add("\""+f.getName()+"\"");
                }
            }
        }
        return "[" + String.join(",", names) + "]";
    }

    @PostMapping("/predict")
    public String predict(@RequestBody JSONObject request, HttpSession session) {
//        String newPath = (String) session.getAttribute("predictPath");
//        if (predictPath.equals(newPath)) return predictResult;
//        else predictPath = newPath;
//
        String fileSeparator = File.separator;
//        FileTools.checkOutputDir(tempPath + fileSeparator + "8" + fileSeparator + "False");
//        FileTools.checkOutputDir(tempPath + fileSeparator + "16" + fileSeparator + "False");
//        FileTools.checkOutputDir(tempPath + fileSeparator + "32" + fileSeparator + "False");
//        List<String> javaFilePaths = FileTools.searchJavaFile((String) session.getAttribute("predictPath"));
//        for (String fileName: javaFilePaths) {
//            FileTools.saveFeature(new File(fileName), tempPath + fileSeparator + "8" + fileSeparator + "False", 8);
//            FileTools.saveFeature(new File(fileName), tempPath + fileSeparator + "16" + fileSeparator + "False", 16);
//            FileTools.saveFeature(new File(fileName), tempPath + fileSeparator + "32" + fileSeparator + "False", 32);
//        }

        String[] params = {pythonPath, "./python/Predict.py", modelPath, tempPath};
        List<String> paramList = Arrays.asList(params);
        JSONArray models = request.getJSONArray("models");
        for (Object s : models) {
            paramList.add((String) s);
        }
        String line = PythonTools.execute(paramList.toArray(new String[paramList.size()]));

        JSONArray jsonArray = JSON.parseArray(line);
        for (Object object : jsonArray) {
            JSONObject o = (JSONObject) object;
            String[] info = o.getString("name").split("#");
            double max = 0.0;
            for (String key : o.keySet()) {
                if (! "name".equals(key)) {
                    max += o.getDouble(key);
                }
            }
            String name = info[0];
            String lineNumber = info[1];
            BugInfo bugInfo = bugInfoDao.findBugsByFileNameAndStart(name, Integer.parseInt(lineNumber));
            vulnerService.probUpdate(bugInfo.getId(), max / (o.keySet().size() - 1));
        }

        for (String type: new String[]{"Text", "WordVector", "Edge", "DeepWalk", "ParagraphVec"}) {
            new File(tempPath + fileSeparator + "8" + fileSeparator + "False" + fileSeparator + type).delete();
            new File(tempPath + fileSeparator + "16" + fileSeparator + "False" + fileSeparator + type).delete();
            new File(tempPath + fileSeparator + "32" + fileSeparator + "False" + fileSeparator + type).delete();
        }

        predictResult = line;
        return line;
    }

}
