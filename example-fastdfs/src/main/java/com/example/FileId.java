package com.example;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class FileId {

    public static String getFileIdByPath(String path){
        byte[] bs = Base64.encodeBase64URLSafe(path.getBytes());
        try {
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getPathByFileId(String fileId){
        //解码fileId
        byte[] bs = Base64.decodeBase64(fileId);
        try {
            return new String(bs, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fileId;
    }
}
