package hellomina.client;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaTimeClient {
    
    public static void main(String[] args){
        // 创建客户端连接器.
        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec",  new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
        
        // 设置连接超时检查时间
        connector.setConnectTimeoutCheckInterval(30000);
        connector.setHandler(new TimeClientHandler());
        
        // 建立连接
        final ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 1235));
        // 等待连接创建完成
        cf.awaitUninterruptibly();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			int num = 0;
			@Override
			public void run() {
				if (num==5) {
					cf.getSession().write("quit");
				}else{
					cf.getSession().write("Hi Server!");
					num++;
				}
		        
			}
		}, 0, 1000);
        
		
        // 等待连接断开
        cf.getSession().getCloseFuture().awaitUninterruptibly();
        // 释放连接
        connector.dispose();
    }
}