package com.zxing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


import javax.imageio.ImageIO;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;


public class DecodeTest {
    public static void main(String[] args) throws Exception {
        // 这里可以是条形图片码或者二维码图片
        // 这是二维码图片
        BufferedImage bi = ImageIO.read(new FileInputStream(new File("new.png")));
        // 这是条形码图片
        // BufferedImage bi = ImageIO.read(new FileInputStream(new File("ean3.png")));
        if (bi != null) {
            Map<DecodeHintType, String> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            LuminanceSource source = new BufferedImageLuminanceSource(bi);
            // 这里还可以是 BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            Result res = new MultiFormatReader().decode(bitmap, hints);
            System.out.println(res);
            System.out.println("decode successfully!");
        }
    }
}