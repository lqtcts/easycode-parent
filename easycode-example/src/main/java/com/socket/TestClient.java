package com.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
/**
 * 客户端程序，在while循环中所执行的动作是：
 * 说，听，说，听，说，听....
 * @author Hongten
 *
 * @time 2012-4-29  2012
 */
public class TestClient {
	
	
    public static void main(String args[]) {
        try {
            // 创建socket对象，指定服务器的ip地址，和服务器监听的端口号
            // 客户端在new的时候，就发出了连接请求，服务器端就会进行处理，如果服务器端没有开启服务，那么
            // 这时候就会找不到服务器，并同时抛出异常==》java.net.ConnectException: Connection
            // refused: connect
            Socket s1 = new Socket("localhost", 8888);
            //打开输出流
            OutputStream os = s1.getOutputStream();
            //封装输出流
            DataOutputStream dos = new DataOutputStream(os);
            //打开输入流
            InputStream is = s1.getInputStream();
            //封装输入流
            DataInputStream dis = new DataInputStream(is);
            //读取键盘输入流
            InputStreamReader isr = new InputStreamReader(System.in);
            //封装键盘输入流
            BufferedReader br = new BufferedReader(isr);

            String info;
            while (true) {
                //客户端先读取键盘输入信息
                info = br.readLine();
                //把他写入到服务器方
                dos.writeUTF(info);
                //如果客户端自己说：bye，即结束对话
                if (info.equals("bye"))
                    break;
                //接受服务器端信息
                info = dis.readUTF();
                //打印
                System.out.println("对方说: " + info);
                if (info.equals("bye"))
                    break;
            }
            //关闭相应的输入流，输出流，socket对象
            dis.close();
            dos.close();
            s1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}