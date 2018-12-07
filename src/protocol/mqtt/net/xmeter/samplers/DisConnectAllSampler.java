package net.xmeter.samplers;

import java.util.Set;
import java.util.logging.Logger;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;

public class DisConnectAllSampler extends AbstractMQTTSampler {
	private static final long serialVersionUID = 4360869021667126983L;
	private static final Logger logger = Logger.getLogger(DisConnectAllSampler.class.getCanonicalName());

	@Override
	public SampleResult sample(Entry entry) {
		logger.severe("=========开始执行DisConnectAllSampler断开所有未断开的连接================");
		SampleResult result = new SampleResult();
		String name = getName();
		result.setSampleLabel(name);
		result.sampleStart();

		int length = connectionEstablished.size();
		if (0 < length) {
			logger.severe("已创建连接客户端的个数： " + length);
			UTF8Buffer[] clientIds = new UTF8Buffer[length];
			CallbackConnection[] connections = new CallbackConnection[length];
			Thread[] threads = new Thread[length];

			Set<java.util.Map.Entry<UTF8Buffer, CallbackConnection>> connEntrySet = connectionEstablished.entrySet();
			int mqttConnNum = 0;
			for (java.util.Map.Entry<UTF8Buffer, CallbackConnection> connEntry : connEntrySet) {
				clientIds[mqttConnNum] = connEntry.getKey();
				connections[mqttConnNum] = connEntry.getValue();
				mqttConnNum++;
			}

			// 断掉连接
			for (int j = 0; j < length; j++) {
				UTF8Buffer clientId = clientIds[j];
				CallbackConnection connection = connections[j];
				try {
					threads[j] = new testThread(connection,clientId);
					threads[j].start();
//					logger.severe("准备断开连接： " + clientId);
//					connection.disconnect(null);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					int k = 10;
					while (null != connection && k < 1) {
						try {
							Thread.sleep(6000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						k--;
					}
					if (null != connection) {
						logger.severe("用kill方法断开连接： " + clientId);
						connection.kill(null);
					}

					if (null == connection) {
						logger.severe("删除相应的全局变量:" + clientId);
						tracerEstablished.remove(clientId);
						connectionEstablished.remove(clientId);
						topicSubscribed.remove(clientId);
					}
				}
			}
			int checkResult = connectionEstablished.size();
			logger.severe("沒有刪除的已連接客户端的个数： " + checkResult);
		}

		result.sampleEnd();
		result.setSuccessful(true);
		result.setResponseData("Successful.".getBytes());
		result.setResponseCodeOK();
		return result;
	}

	Runnable myRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				System.out.println(Thread.currentThread().getName() + " run");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	};

}
