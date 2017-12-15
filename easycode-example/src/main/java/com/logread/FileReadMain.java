package com.logread;

import com.logread.datainwork.DataInwork;
import com.logread.handle.IHandle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FileReadMain implements Runnable {

    private String filePath;
    private IHandle handle;
    private DataInwork dataHandle;

    public FileReadMain(String filePath, IHandle handle, DataInwork dataHandle) {

        this.filePath = filePath;
        this.handle = handle;
        this.dataHandle = dataHandle;

    }

    @Override
    public void run() {
        File file = new File(this.filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line!=null && !"".equals(line)){
                    this.handle.handle(line);
                }
            }
            br.close();
            if (handle.getMap()!=null){
                dataHandle.inworkCounter(handle.getMap());
            }
            if (handle.getCostMap()!=null){
                dataHandle.inworkSettle(handle.getCostMap());
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("处理失败......"+e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
