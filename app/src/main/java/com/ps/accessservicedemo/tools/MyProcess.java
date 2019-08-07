package com.ps.accessservicedemo.tools;

import java.io.OutputStream;

public class MyProcess {
    private Process mProcess;

    public MyProcess(String name) throws Exception {
        try {
            mProcess = Runtime.getRuntime().exec(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exec(String cmd) throws Exception {
        OutputStream outputStream = mProcess.getOutputStream();
        try {
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
