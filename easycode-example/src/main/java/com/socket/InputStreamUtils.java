package com.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author Andy.Chen
 * @mail Chenjunjun.ZJ@gmail.com
 *
 */
public class InputStreamUtils {

	final static int BUFFER_SIZE = 4096;

	/**
	 * 将InputStream转换成String
	 * 
	 * @param in
	 *            InputStream
	 * @return String
	 * @throws Exception
	 * 
	 */
	public static String InputStreamTOString(InputStream in) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), "ISO-8859-1");
	}

	/**
	 * 将InputStream转换成某种字符编码的String
	 * 
	 * @param in
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String InputStreamTOString(InputStream in, String encoding) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), encoding);
	}

	/**
	 * 将String转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream StringTOInputStream(String in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
		return is;
	}

	/**
	 * 将InputStream转换成byte数组
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return outStream.toByteArray();
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/**
	 * 将byte数组转换成String
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static String byteTOString(byte[] in) throws Exception {

		InputStream is = byteTOInputStream(in);
		return InputStreamTOString(is);
	}

	/**
	 * 将流转成字符串
	 * */
	public static String Stream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return new String(baos.toByteArray(), "UTF-8");

	};

	/**
	 * 将输入流存储到byte数组中
	 * 
	 * @param
	 */
	public static byte[] in2byte(InputStream is) throws IOException {
		byte[] bs = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = -1;
		while ((len = is.read(bs)) != -1) {
			bos.write(bs, 0, len);
		}
		bs = bos.toByteArray();
		return bs;
	}

	/**
	 * byte数组转换成string
	 * 
	 * @param
	 */
	public static String toString(byte[] input) throws IOException {
		return new String(input);
	}

	/**
	 * byte数组转换成string
	 * 
	 * @param charset
	 *            编码格式
	 */
	public static String toString(byte[] input, String charset) throws IOException {
		return new String(input, charset);
	}

	/**
	 * 将字符串转换为输入流
	 * 
	 * @param charset
	 *            编码格式
	 */
	public static InputStream toInputStream(String input) {
		byte[] bytes = input.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * 将byte数组转换为输入流
	 * 
	 * @param charset
	 *            编码格式
	 */
	public static InputStream byte2InputStream(byte[] b) {
		return new ByteArrayInputStream(b);
	};

	/**
	 * 将字符串转换为输入流
	 * 
	 * @param input
	 *            需要转换的字符串
	 * @param encoding
	 *            编码格式
	 */
	public static InputStream toInputStream(String input, String encoding) throws IOException {
		byte[] bytes = encoding != null ? input.getBytes(encoding) : input.getBytes();
		return new ByteArrayInputStream(bytes);
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		while (true) {
			int len = in.read(buf);
			if (len < 0)
				break;
			out.write(buf, 0, len);
		}
	}

}