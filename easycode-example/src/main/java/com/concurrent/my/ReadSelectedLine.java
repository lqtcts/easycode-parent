package com.concurrent.my;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class ReadSelectedLine {
	/**
	 * 读取文件指定行。
	 */
	public static void main(String[] args) throws IOException {
		// 指定读取的行号
		int lineNumber = 12;
		// 读取文件
		String file="C:\\Users\\wuchengbin\\Desktop\\新建文件夹 (3)\\a.txt"; 
		File sourceFile = new File(file);

		// 读取指定的行
		readAppointedLineNumber(sourceFile, 3);
		// 获取文件的内容的总行数
		System.out.println(getTotalLines(sourceFile));
	}

	// 读取文件指定行。
	static void readAppointedLineNumber(File sourceFile, int lineNumber) throws IOException {
		FileReader in = new FileReader(sourceFile);
		LineNumberReader reader = new LineNumberReader(in);
		String s ="";
		if (lineNumber < 0 || lineNumber > getTotalLines(sourceFile)) {
			System.out.println("不在文件的行数范围之内。");
		}
				System.out.println("当前行号为:" + reader.getLineNumber()); 
				reader.setLineNumber(lineNumber);
//				System.out.println(s);
				System.out.println("更改后行号为:" + reader.getLineNumber());
				s = reader.readLine();
				System.out.println(s);
		reader.close();
		in.close();
	}

	// 文件内容的总行数。
	static int getTotalLines(File file) throws IOException {
		FileReader in = new FileReader(file);
		LineNumberReader reader = new LineNumberReader(in);
		String s = reader.readLine();
		int lines = 0;
		while (s != null) {
			lines++;
			s = reader.readLine();
		}
		reader.close();
		in.close();
		return lines;
	}
}