package com.example.demo.codefeature.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonTools {

    private static final Logger logger = LoggerFactory.getLogger(PythonTools.class);

    public static String execute(String[] command) {
        try {
            logger.info(String.join(" ", command));
            Process proc = Runtime.getRuntime().exec(String.join(" ", command));
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = in.readLine();
            in.close();
            proc.waitFor();
            return line;
        }  catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
