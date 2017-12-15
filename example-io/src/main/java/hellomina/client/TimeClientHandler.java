package hellomina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeClientHandler extends IoHandlerAdapter {

	  /**
     * 消息接收事件
     */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		String content = message.toString();
		System.out.println("client receive a message is : " + content);
	}

	/**
	 * 发送消息
	 */
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent -> ：" + message);
	}

}