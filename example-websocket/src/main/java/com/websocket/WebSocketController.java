package com.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Time: 下午2:48
 * Class Comments: 系统登录登出
 * Version:
 * 1.0 创建
 */
@ServerEndpoint("/WebSocketController") 
public class WebSocketController {
	
	private static Map<String, Session> sessions = new HashMap<String, Session>();  
	
	@OnMessage  
	  public void onMessage(String message, Session session)  
	    throws IOException, InterruptedException {  
		sedMesAll(message,session);
	  }  
	     
	  @OnOpen  
	  public void onOpen(Session session, EndpointConfig config) {
		sessions.put(session.getId(), session);
	    System.out.println("客户端连接了");  
	  }  
	   
	  @OnClose  
	  public void onClose(Session session, CloseReason reason) {  
		  try {  
	            System.out.println("断开连接, id="+session.getId());  
	            synchronized (sessions){  
	                sessions.remove(session.getId());  
	            }  
	        }catch (Exception e){  
	            e.printStackTrace();  
	        }  
	  }  
   
	  
	  private void sedMesAll(String mes, Session session){
		  Set<String> keys = sessions.keySet();  
	        for(String k:keys){
	        	System.out.println("kehuduan"+k);
	            Session s = sessions.get(k);  
	            if(s.isOpen()){
	                try{
	                	if(!session.getId().equals(k)){
	                		s.getBasicRemote().sendText(mes);
	                	}
	                }catch(Exception e){  
	                    System.err.println("发送出错："+e.getMessage());  
	                }  
	            }  
	        } 
	  }
}
