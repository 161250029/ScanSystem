package com.example.demo.Tool;
import com.example.demo.Resources.JarLocationInfo;
import com.example.demo.Resources.SlicerType;
import com.example.demo.Resources.StatementInfo;
import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.AllApplicationEntrypoints;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.modref.ModRef;
import com.ibm.wala.ipa.slicer.NormalStatement;
import com.ibm.wala.ipa.slicer.SDG;
import com.ibm.wala.ipa.slicer.Slicer;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.ipa.slicer.thin.ThinSlicer;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.config.FileOfClasses;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.util.graph.GraphSlicer;
import com.ibm.wala.util.strings.Atom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class WalaSlicerTool {
    private static final String EXCLUSIONS =
            "java\\/awt\\/.*\n"
                    + "javax\\/swing\\/.*\n"
                    + "sun\\/awt\\/.*\n"
                    + "sun\\/swing\\/.*\n"
                    + "com\\/sun\\/.*\n"
                    + "sun\\/.*\n"
                    + "org\\/netbeans\\/.*\n"
                    + "org\\/openide\\/.*\n"
                    + "com\\/ibm\\/crypto\\/.*\n"
                    + "com\\/ibm\\/security\\/.*\n"
                    + "org\\/apache\\/xerces\\/.*\n"
                    + "java\\/security\\/.*\n"
                    + "";
    public static List<String> methods = new ArrayList<>();
//    com.ibm.wala.ipa.callgraph.impl.
//    com.ibm.wala.ipa.callgraph.
//        System.out.println(appJar + " " + methodName + " " + className + " " + lineNumber);

    public static Set<Integer> doSlicing(String appJar , String methodName , String className , int lineNumber) throws WalaException, CancelException, IOException, InvalidClassFileException {
        System.out.println(appJar + " " +  methodName + " "  + className + " "  + lineNumber);
        Set<Integer> sourcelines = new TreeSet<>();
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,null);
        scope.setExclusions(new FileOfClasses(new ByteArrayInputStream(EXCLUSIONS.getBytes("UTF-8"))));
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);
        Iterable<Entrypoint> entrypoints = new AllApplicationEntrypoints(scope,cha);
                Util.makeMainEntrypoints(scope, cha);
        AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
        CallGraphBuilder cgb = Util.makeZeroCFABuilder(Language.JAVA ,options, new AnalysisCacheImpl(),cha, scope, null, null);
        CallGraph cg = cgb.makeCallGraph(options , null);
        PointerAnalysis pa = cgb.getPointerAnalysis();
        List<StatementInfo> statementInfos = findSeedStatementInfoByLineNumber(findMainMethod(cg  ,methodName , className), lineNumber);
        ModRef<InstanceKey> modRef = ModRef.make();
        SDG<?> sdg = new SDG<>(cg, pa, modRef, Slicer.DataDependenceOptions.REFLECTION, Slicer.ControlDependenceOptions.NO_EXCEPTIONAL_EDGES, null);
        findAllMethod(cg , className);
        for (StatementInfo statementInfo : statementInfos) {
            Collection<Statement> slice;
            if (statementInfo == null) {
                sourcelines.add(lineNumber);
                continue;
            }
//            if (statementInfo.getSlicerType() == SlicerType.BackwardSlice) {
//                System.out.println("back start");
//                slice = Slicer.computeBackwardSlice(statementInfo.getSeedStatement(), cg, pa, Slicer.DataDependenceOptions.NO_BASE_PTRS,
//                        Slicer.ControlDependenceOptions.NONE);
//            }
//            else {
//                System.out.println("forward start");
//                slice = Slicer.computeForwardSlice(statementInfo.getSeedStatement(), cg, pa, Slicer.DataDependenceOptions.NO_BASE_PTRS,
//                        Slicer.ControlDependenceOptions.NONE);
//            }

//            ThinSlicer ts = new ThinSlicer(cg,pa);
//            slice = ts.computeBackwardThinSlice ( statementInfo.getSeedStatement() );
//
//            for (Statement s : slice) {
//                System.out.println(s);
//            }


            slice = Slicer.computeBackwardSlice(statementInfo.getSeedStatement(), cg, pa, Slicer.DataDependenceOptions.FULL,
                        Slicer.ControlDependenceOptions.NONE);
            for (Statement s : slice)
                System.out.println(s);
            System.out.println(slice.size());


//            for (Statement s : slice) {
//                System.out.println(s);
//            }
//            for (String methodname : methods)
//                System.out.println(methodname);
            Predicate<Statement> filter = o -> slice.contains(o) && !o.toString().contains("Primordial") && o.getKind() == Statement.Kind.NORMAL;
            Graph<Statement> graph = GraphSlicer.prune(sdg, filter);
            for (Statement s : graph) {
                if (methods.contains(s.getNode().getMethod().getName().toString()))
                    sourcelines.add(getStatementLineNumber(s));
                System.out.println(s);
                System.out.println(getStatementLineNumber(s));
            }

            break;
        }
        sourcelines.add(lineNumber);
        System.out.println(sourcelines.size());
        return sourcelines;
    }

    //find the method where the seed statement is included.
    public static CGNode findMainMethod(CallGraph cg , String appJarName , String methodName , String className) {
        Atom name = Atom.findOrCreateUnicodeAtom(methodName);
        for (Iterator<? extends CGNode> it = cg.iterator(); it.hasNext();) {
            CGNode n = it.next();
//            System.out.println(n.getMethod().getName());
            if (n.getMethod().getName().equals(name) && n.getMethod().getDeclaringClass().getName().toString().equals("LMain"))
                return n;

            if (n.getMethod().getName().equals(name)  && n.getMethod().getDeclaringClass().getName().toString().equals("L" + "testcases/" + appJarNameToDir(appJarName) + "/" + className)) {
                System.out.println(n.getMethod().getDeclaringClass().getName().toString() + " " + n.getMethod().getName().toString());
                return n;
            }
        }
        return null;
    }

    public static CGNode findMainMethod(CallGraph cg , String methodName , String className) {
        Atom name = Atom.findOrCreateUnicodeAtom(methodName);
        for (Iterator<? extends CGNode> it = cg.iterator(); it.hasNext();) {
            CGNode n = it.next();
//            System.out.println(n.getMethod().getName());
            if (n.getMethod().getName().equals(name) && n.getMethod().getDeclaringClass().getName().toString().equals("LMain"))
                return n;

            if (n.getMethod().getName().equals(name)  && n.getMethod().getDeclaringClass().getName().toString().equals("L"  + className)) {
                System.out.println(n.getMethod().getDeclaringClass().getName().toString() + " " + n.getMethod().getName().toString());
                return n;
            }
        }
//        Assertions.UNREACHABLE("failed to find " + methodName +" method");
        return null;
    }

    public static void findAllMethod(String appJar , String className) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException {
        CallGraph cg = GenerateCallGraph(appJar);
        Iterator<? extends CGNode> it = cg.iterator();
        while (it.hasNext()) {
            CGNode n = it.next();
            if (n.getMethod().getDeclaringClass().getName().toString().equals("LMain" )) {
                methods.add(n.getMethod().getName().toString());
            }

            if (n.getMethod().getDeclaringClass().getName().toString().equals("L" + "testcases/" + appJarNameToDir(appJar.split("\\\\")[appJar.split("\\\\").length - 1].split("\\.")[0]) + "/" + className)) {
                methods.add(n.getMethod().getName().toString());
            }
        }
    }

    public static void findAllMethod(CallGraph cg ,String className) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException {
        Iterator<? extends CGNode> it = cg.iterator();
        while (it.hasNext()) {
            CGNode n = it.next();
            if (n.getMethod().getDeclaringClass().getName().toString().equals("LMain" )) {
                methods.add(n.getMethod().getName().toString());
            }

            if (n.getMethod().getDeclaringClass().getName().toString().equals("L" +  className)) {
                methods.add(n.getMethod().getName().toString());
            }
        }
    }

    public static List<StatementInfo> findSeedStatementInfoByLineNumber(CGNode n, int LineNumber) throws InvalidClassFileException {
        List<StatementInfo> result = new ArrayList<>();
        if (n == null) {
            System.out.println("未找到对应函数");
            return null;
        }
        IR ir = n.getIR();
        for (int i = 0 ; i < ir.getInstructions().length ; i ++) {
//            System.out.println(IRIdexToLineNumber(ir , i));
            if (IRIdexToLineNumber(ir , i) == LineNumber && ir.getInstructions()[i] != null) {
                StatementInfo statementInfo = new StatementInfo();
                if (ir.getInstructions()[i].getNumberOfUses() <= ir.getInstructions()[i].getNumberOfDefs())
                    statementInfo.setSlicerType(SlicerType.BackwardSlice);
                else
                    statementInfo.setSlicerType(SlicerType.ForwardSlice);
                statementInfo.setSeedStatement(new NormalStatement(n , i));
                result.add(statementInfo);
            }
        }
        System.out.println(result.size());
        return result;
    }
    //According to LineNumber , find the seed statement
    public static Statement findSeedStatementByLineNumber(CGNode n, int LineNumber) throws InvalidClassFileException {
        IR ir = n.getIR();
        for (int i = 0 ; i < ir.getInstructions().length ; i ++) {
//            System.out.println(IRIdexToLineNumber(ir , i));
            if (IRIdexToLineNumber(ir , i) == LineNumber && ir.getInstructions()[i] != null) {
                return new NormalStatement(n , i);
            }
        }
        Assertions.UNREACHABLE("failed to find call to "  + " in " + n);
        return null;
    }

    public static AnalysisScope GenerateAnalysisScope(String appJar) throws IOException {
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,null);
        scope.setExclusions(new FileOfClasses(new ByteArrayInputStream(EXCLUSIONS.getBytes("UTF-8"))));
        return scope;
    }

    public static boolean CanBeSeedStatement(CGNode n, int LineNumber) throws InvalidClassFileException {
        return findSeedStatementByLineNumber(n , LineNumber) == null ? false : true;
    }

    public static boolean CanBeSeedStatement(CallGraph cg , String appJarName , String methodName , String className , int LineNumber) throws InvalidClassFileException {
        return CanBeSeedStatement(findMainMethod(cg , appJarName , methodName , className) , LineNumber);
    }

    public static boolean CanBeSeedStatement(String appJar , String appJarName , String methodName , String className , int LineNumber) throws ClassHierarchyException, CallGraphBuilderCancelException, IOException, InvalidClassFileException {
        return CanBeSeedStatement(GenerateCallGraph(appJar) , appJarName ,methodName , className , LineNumber);
    }

    public static CallGraph GenerateCallGraph(AnalysisOptions options , String appJar) throws IOException, ClassHierarchyException, CallGraphBuilderCancelException {
        return GenerateCallGraphBuilder(appJar).makeCallGraph(options , null);
    }

    public static CallGraph GenerateCallGraph(String appJar) throws IOException, ClassHierarchyException, CallGraphBuilderCancelException {
        return GenerateCallGraph(GenerateAnalysisOptions(appJar) , appJar);
    }

    public static AnalysisOptions GenerateAnalysisOptions(String appJar) throws IOException, ClassHierarchyException {
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,null);
        scope.setExclusions(new FileOfClasses(new ByteArrayInputStream(EXCLUSIONS.getBytes("UTF-8"))));
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);
        Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
        AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
        return options;
    }

    public static CallGraphBuilder GenerateCallGraphBuilder(String appJar) throws IOException, ClassHierarchyException {
        // create an analysis scope representing the appJar as a J2SE application
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,null);
//        AnalysisScopeReader.readJavaScope(appJar , null , slicer.class.getClassLoader());
        scope.setExclusions(new FileOfClasses(new ByteArrayInputStream(EXCLUSIONS.getBytes("UTF-8"))));
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);

        Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
        AnalysisOptions options = new AnalysisOptions(scope, entrypoints);
        com.ibm.wala.ipa.callgraph.CallGraphBuilder cgb = Util.makeZeroCFABuilder(Language.JAVA ,options, new AnalysisCacheImpl(),cha, scope, null, null);
        return cgb;
    }

    //IRindex to LineNumber
    public static int IRIdexToLineNumber(IR ir , int instructionIndex) throws InvalidClassFileException {
        IBytecodeMethod method = (IBytecodeMethod)ir.getMethod();
        int bytecodeIndex = method.getBytecodeIndex(instructionIndex);
        int sourceLineNum = method.getLineNumber(bytecodeIndex);
        return sourceLineNum;
    }

    public static int getStatementLineNumber(Statement statement) throws InvalidClassFileException {
        NormalStatement n = (NormalStatement) statement;
        IMethod method = n.getNode().getMethod();
        return method.getSourcePosition(n.getInstructionIndex()).getFirstLine();
    }

    public static String appJarNameToDir(String appJarName) {
        return appJarName.substring(appJarName.length() - 4 ,appJarName.length() - 1).equals("_s0") ?
                appJarName.substring(0 , appJarName.length() - 4) + "/" + appJarName.substring(appJarName.length() - 3)
                : appJarName;
    }

    public static void dumpSlice(Collection<Statement> slice) throws InvalidClassFileException {
        for (Statement s : slice) {
//            System.out.println(s.getNode().getMethod().getDeclaringClass().getName());
            if (s.getNode().getMethod().getDeclaringClass().getName().toString().equals("LMain")) {
                System.out.println(s);
                if (s.getKind() == Statement.Kind.NORMAL)
                    System.out.println(s.getNode().getMethod().getName().toString() + s.getNode().getMethod().getSourcePosition(
                            ((NormalStatement)s).getInstructionIndex()).getFirstLine());
            }
        }
//        for (Statement s : slice) {
//            if (s == null)
//                continue;
//            System.out.println(StmtFormater.format(s));
//        }
    }
    public static void main(String[] args) throws IOException, WalaException, CancelException, InvalidClassFileException {
//        findAllMethod("src\\main\\java\\CWE15_External_Control_of_System_or_Configuration_Setting.jar" , "CWE15_External_Control_of_System_or_Configuration_Setting__connect_tcp_02");

//        System.out.println(doSlicing("src\\main\\java\\CWE15_External_Control_of_System_or_Configuration_Setting.jar" , "goodG2B1" , "CWE15_External_Control_of_System_or_Configuration_Setting__connect_tcp_02" ,166));

        System.out.println(doSlicing(JarLocationInfo.Jar_Directory_prefix + "\\CWE369_Divide_by_Zero_s04.jar" , "badSink" , "testcases/CWE369_Divide_by_Zero/s04/CWE369_Divide_by_Zero__int_zero_divide_75b" , 39));
//        System.out.println(doSlicing("C:\\Users\\admin\\Desktop\\Main.jar" , "sink" , "LMain" , 13));
//        System.out.println(doSlicing("C:\\Users\\admin\\Desktop\\Main.jar" , "main" , "LMain" , 7));  forward

//          System.out.println(doSlicing("D:\\upload\\CWE259_Hard_Coded_Password\\CWE259_Hard_Coded_Password.jar" , "bad" , "testcases/CWE259_Hard_Coded_Password/CWE259_Hard_Coded_Password__driverManager_05" , 59));

    }
}
