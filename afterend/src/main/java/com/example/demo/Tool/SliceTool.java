package com.example.demo.Tool;

import com.ibm.wala.classLoader.IBytecodeMethod;
import com.ibm.wala.classLoader.Language;
import com.ibm.wala.ipa.callgraph.*;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.slicer.NormalStatement;
import com.ibm.wala.ipa.slicer.Slicer;
import com.ibm.wala.ipa.slicer.Statement;
import com.ibm.wala.ipa.slicer.thin.ThinSlicer;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.types.Descriptor;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.config.AnalysisScopeReader;
import com.ibm.wala.util.config.FileOfClasses;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.strings.Atom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class SliceTool {
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
    public static void doSlicing(String appJar) throws WalaException, CancelException, IOException, InvalidClassFileException {
        // create an analysis scope representing the appJar as a J2SE application
        AnalysisScope scope = AnalysisScopeReader.makeJavaBinaryAnalysisScope(appJar,null);
        scope.setExclusions(new FileOfClasses(new ByteArrayInputStream(EXCLUSIONS.getBytes("UTF-8"))));
        ClassHierarchy cha = ClassHierarchyFactory.make(scope);

        Iterable<Entrypoint> entrypoints = com.ibm.wala.ipa.callgraph.impl.Util.makeMainEntrypoints(scope, cha);
        AnalysisOptions options = new AnalysisOptions(scope, entrypoints);

        // build the call graph
        com.ibm.wala.ipa.callgraph.CallGraphBuilder cgb = Util.makeZeroCFABuilder(Language.JAVA ,options, new AnalysisCacheImpl(),cha, scope, null, null);
        CallGraph cg = cgb.makeCallGraph(options , null);
        PointerAnalysis pa = cgb.getPointerAnalysis();

        // find seed statement
        Statement statement = findCallTo(findMainMethod(cg), "println");

        Statement statement1 = new NormalStatement(findMainMethod(cg) , 0);

        Collection<Statement> slice;

        ThinSlicer ts = new ThinSlicer(cg,pa);
        slice = ts.computeBackwardThinSlice ( statement );
        dumpSlice(slice);
        System.out.println("thin 结束");
        // context-sensitive thin slice
        slice = Slicer.computeBackwardSlice(statement, cg, pa, Slicer.DataDependenceOptions.NO_BASE_PTRS,
                Slicer.ControlDependenceOptions.NONE);
        dumpSlice(slice);

        slice = Slicer.computeBackwardSlice(statement1, cg, pa, Slicer.DataDependenceOptions.NO_BASE_PTRS,
                Slicer.ControlDependenceOptions.NONE);

        dumpSlice(slice);
    }

    public static CGNode findMainMethod(CallGraph cg) {
        Descriptor d = Descriptor.findOrCreateUTF8("([Ljava/lang/String;)V");
        Atom name = Atom.findOrCreateUnicodeAtom("sink");
        for (Iterator<? extends CGNode> it = cg.iterator(); it.hasNext();) {
            CGNode n = it.next();
            if (n.getMethod().getName().equals(name)) {
                return n;
            }
        }
        Assertions.UNREACHABLE("failed to find main() method");
        return null;
    }

    public static Statement findCallTo(CGNode n, String methodName) throws InvalidClassFileException {
        IR ir = n.getIR();
        int count = 0;
        for (Iterator<SSAInstruction> it = ir.iterateAllInstructions(); it.hasNext();) {
            SSAInstruction s = it.next();
            return new com.ibm.wala.ipa.slicer.NormalStatement(n, 2);
//            if (s instanceof com.ibm.wala.ssa.SSAAbstractInvokeInstruction) {
//                com.ibm.wala.ssa.SSAAbstractInvokeInstruction call = (com.ibm.wala.ssa.SSAAbstractInvokeInstruction) s;
//                if (call.getCallSite().getDeclaredTarget().getName().toString().equals(methodName)) {
//                    com.ibm.wala.util.intset.IntSet indices = ir.getCallInstructionIndices(call.getCallSite());
//                    com.ibm.wala.util.debug.Assertions.productionAssertion(indices.size() == 1, "expected 1 but got " + indices.size());
//                    return new com.ibm.wala.ipa.slicer.NormalStatement(n, indices.intIterator().next());
//                }
//            }
        }
        Assertions.UNREACHABLE("failed to find call to " + methodName + " in " + n);
        return null;
    }

    //IRindex to LineNumber
    public static int IRIdexToLineNumber(IR ir , int instructionIndex) throws InvalidClassFileException {
        IBytecodeMethod method = (IBytecodeMethod)ir.getMethod();
        int bytecodeIndex = method.getBytecodeIndex(instructionIndex);
        int sourceLineNum = method.getLineNumber(bytecodeIndex);
        return sourceLineNum;
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
    }

    public static void main(String[] args) throws CancelException, WalaException, IOException, InvalidClassFileException {
        doSlicing("C:\\Users\\admin\\Desktop\\Main.jar");
    }
}
