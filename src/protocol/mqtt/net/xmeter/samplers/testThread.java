package net.xmeter.samplers;

import java.util.logging.Logger;

import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;




public class testThread extends Thread {
	private static final Logger logger = Logger.getLogger(DisConnectAllSampler.class.getCanonicalName());
	private CallbackConnection connection = null;
	private UTF8Buffer clientId = null;


	
	public testThread(CallbackConnection connection, UTF8Buffer clientId) {
		this.connection = connection;
		this.clientId = clientId;
	}

	
	public void run() {
		logger.severe("线程"+this.getId()+"准备断开连接： " + clientId);
		connection.disconnect( null);
		
	}


	@Override
	public synchronized void start() {
		logger.severe("线程"+this.getId()+"启动"  );
		// TODO Auto-generated method stub
		super.start();
	}


	@Override
	public void destroy() {
		logger.severe("线程"+this.getId()+"结束"  );
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	
}
