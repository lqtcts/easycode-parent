package com.ssh;

public class Test {
	public static void main(String[] args) {
		
	SSHHelper aHelper = new SSHHelper();
	String exec = SSHHelper.exec("192.168.3.61", "root", "pxene123", 22, "ls");
	System.out.println(exec);
		
	}
}
