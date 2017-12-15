package com.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {

	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		
		Socket socket = new Socket("localhost", 4443);
		
		InputStream inputStream = socket.getInputStream();
		OutputStream outputStream = socket.getOutputStream();

		
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		
		dataOutputStream.writeUTF("ok");

		
		String readUTF = dataInputStream.readUTF();
		System.out.println(readUTF);
		
		
		
		
		dataInputStream.close();
		dataOutputStream.close();
		socket.close();
		
		
	}
}
