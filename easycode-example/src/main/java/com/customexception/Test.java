package com.customexception;
public class Test {
	public static void main(String[] args) {
		Numbertest n = new Numbertest();

		// 捕获异常
		try {
			System.out.println("商=" + n.shang(1, -3));
		} catch (ChushulingException yc) {
			System.out.println(yc.getMessage());
			yc.printStackTrace();
		} catch (ChushufuException yx) {
			System.out.println(yx.getMessage());
			yx.printStackTrace();
		} catch (Exception y) {
			System.out.println(y.getMessage());
			y.printStackTrace();
		}

		finally {
			System.out.println("finally!");
		} // //finally不管发没发生异常都会被执行AdvertiserAccountBean.java

	}
}

class ChushulingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChushulingException(String msg) {
		super(msg);
	}
}

class ChushufuException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChushufuException(String msg) {
		super(msg);
	}
}

/* 自定义异常 End */

class Numbertest {
	public int shang(int x, int y) throws ChushulingException, ChushufuException {
		if (y < 0) {
			throw new ChushufuException("您输入的是" + y + ",规定除数不能为负数!");// 抛出异常
		}
		if (y == 0) {
			throw new ChushulingException("您输入的是" + y + ",除数不能为0!");
		}

		int m = x / y;
		return m;
	}
}
