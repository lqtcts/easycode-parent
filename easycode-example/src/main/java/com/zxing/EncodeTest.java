package com.zxing;

import clojure.main;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;


public class EncodeTest {
    static final String base = "easycode-example/src/main/java/com/zxing/image";

    public static void main(String[] args) throws Exception {
        int width = 300;
        int height = 300;
        // int width = 105;
        // int height = 50;
        // 条形码的输入是13位的数字
        // String text = "6923450657713";
        // 二维码的输入是字符串
        String text = "testtesttest生成条形码图片";
        String text1 = "6923450657713";
        String format = "png";
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 条形码的格式是 BarcodeFormat.EAN_13
        // 二维码的格式是BarcodeFormat.QR_CODE
        BitMatrix bm = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        BitMatrix bm1 = new MultiFormatWriter().encode(text1, BarcodeFormat.EAN_13, width, height, hints);// 生成矩阵

//      File out = new File("new.png");
//      WriteBitMatricToFile.writeBitMatricToFile(bm, format, out);

        String fileName = base + "/new1.png";
        String fileName1 = base + "/new2.png";
        Path path = FileSystems.getDefault().getPath(fileName);
        Path path1 = FileSystems.getDefault().getPath(fileName1);


        MatrixToImageWriter.writeToPath(bm, format, path);
        MatrixToImageWriter.writeToPath(bm1, format, path1);
    }


}