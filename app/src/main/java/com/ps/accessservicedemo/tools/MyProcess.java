package com.ps.accessservicedemo.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MyProcess {
    private Process mProcess;

//    public MyProcess(String name) throws Exception {
//        try {
//            mProcess = Runtime.getRuntime().exec(name);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void exec(String cmd){
        try {
            mProcess = Runtime.getRuntime().exec("adb shell");
            OutputStream outputStream = mProcess.getOutputStream();
            outputStream.write((cmd + "\n").getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     /* 得到当前测试照片名称的方法
	 */
    public void get_photo_name(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("adb shell");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.getOutputStream().write(("ls" + "\n").getBytes());
            process.getOutputStream().flush();
            String s = input.readLine();
            System.out.println("照片名称是："+s);
            input.close();
            process.destroy();


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void runShellCommand(String command){
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyProcess getInstance() {
        return Processs.p;
    }
    public static class Processs {
        static MyProcess p = new MyProcess();
    }

}
