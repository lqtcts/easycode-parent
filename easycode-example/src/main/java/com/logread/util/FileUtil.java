package com.logread.util;

import java.io.File;
import java.io.FileFilter;

public class FileUtil {

    //create a FileFilter and override its accept-method
    public static FileFilter filefilter = new FileFilter() {
        public boolean accept(File file) {
            if (file.getName().endsWith(".log")) {
                return true;
            }
            return false;
        }
    };


}