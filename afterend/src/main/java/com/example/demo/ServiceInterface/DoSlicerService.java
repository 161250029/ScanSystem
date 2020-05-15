package com.example.demo.ServiceInterface;

import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface DoSlicerService {
    public void WalaSlicer_Positive();
    public void WalaSlicer_False_Positive();
    public void TextSlicer_Positive();
    public void TextSlicer_False_Postive();

    public void TextSlicer_BugInfo();

    public void TextSlicer_BugInfo_fivelines();

    public String doslice(String jarpath ,String methodName , String className ,int lineNumber) throws WalaException, CancelException, InvalidClassFileException, IOException;

}
