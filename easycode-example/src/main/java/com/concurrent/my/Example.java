package com.concurrent.my;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Example {
	public static void main(String[] args) {
		System.out.println("---------------");
		readFile("C:\\Users\\Administrator\\Desktop\\a.txt", 200000);
//		readFile("proxy.txt", 1);
//		readFile("proxy.txt", 4);
//		execSystemCmd("notepad"); // windows cmd
//		execSystemCmd("ls /home/whucs");
//		execSystemCmd("tail -n /home/whucs/vote.py");
//		execSystemCmd("wc -l php-fpm.log"); // count lines of a file. 500MB ~ 2s
	}

	public static void readFile(String path, int beginLine) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(path);
			sc = new Scanner(inputStream, "UTF-8");
			int begin = 0;
			if (beginLine == 0)
				beginLine = 1;
			while (sc.hasNextLine()) {
				begin++;
				if (begin >= beginLine) {
					String line = sc.nextLine();
					// TODO...
					System.out.println(line);
				} else {
					sc.nextLine();
				}
			}
			if (begin < beginLine) {
				System.out.println("error! beginLine > file's total lines.");
			}
			inputStream.close();
			sc.close();
		} catch (IOException e) {
			System.out.println("FileReader IOException!");
			e.printStackTrace();
		}
	}

	public static void execSystemCmd(String cmd) {
		String outPut = null;
		System.out.println("cmd=" + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				buf.append(line + "\n");
			outPut = buf.toString();
			System.out.printf("outPut = %s", outPut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}