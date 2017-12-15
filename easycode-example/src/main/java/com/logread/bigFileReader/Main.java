package com.logread.bigFileReader;

import com.logread.bigFileReader.IHandle;

public class Main {

    public static void main(String[] args) {
        BigFileReader.Builder builder = new BigFileReader.Builder("C:\\Users\\wuchengbin.POWERXENE\\Desktop\\dsp_impclk_000000019.log", new IHandle() {
            @Override
            public void handle(String line) {

            }
        });


        builder.withTreahdSize(10)
                .withCharset("utf-8")
                .withBufferSize(1024 * 1024);
        BigFileReader bigFileReader = builder.build();
        bigFileReader.start();
    }

}