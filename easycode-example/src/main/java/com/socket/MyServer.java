package com.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	
	public static void main(String[] args) throws IOException {
		
		
		
		ServerSocket serverSocket = new ServerSocket(4443);
		
		
		Socket accept = serverSocket.accept();
		
		InputStream inputStream = accept.getInputStream();
		OutputStream outputStream = accept.getOutputStream();
		
		
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		String readUTF = dataInputStream.readUTF();
		System.out.println(readUTF);
		
		dataOutputStream.writeUTF("ok");
		
		
		dataInputStream.close();
		dataOutputStream.close();
		accept.close();
	}
}
