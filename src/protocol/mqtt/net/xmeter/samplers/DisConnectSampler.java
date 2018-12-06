package net.xmeter.samplers;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;

public class DisConnectSampler extends AbstractMQTTSampler {
	private static final long serialVersionUID = 4360869021667126983L;
	private static final Logger logger = Logger.getLogger(DisConnectSampler.class.getCanonicalName());

	private transient CallbackConnection connection = null;

	@Override
	public SampleResult sample(Entry entry) {
		SampleResult result = new SampleResult();
		String name = getName();
		result.setSampleLabel(name);
		
		JMeterVariables vars = JMeterContextService.getContext().getVariables();

		connection = (CallbackConnection) vars.getObject("conn");
		UTF8Buffer clientId = (UTF8Buffer) vars.getObject("clientId");
 
		
		if (connection == null) {
			logger.info(MessageFormat.format("connection is null,{0}",connection));
			result.sampleStart();
			result.setSuccessful(false);
			result.setResponseMessage("Connection not found.");
			result.setResponseData("Failed. Connection not found.".getBytes());
			result.setResponseCode("500");
			result.sampleEnd(); // avoid endtime=0 exposed in trace log
			return result;
		}
		
		try {
			result.sampleStart();
			if (connection != null) {
				logger.info(MessageFormat.format("Disconnect connection::::    {0}.", connection));
				connection.disconnect(null);
				vars.remove("conn"); // clean up thread local var as well
				topicSubscribed.remove(clientId);
				connectionEstablished.remove(clientId);
				tracerEstablished.remove(clientId);	
			}
			
			result.sampleEnd();
			result.setSuccessful(true);
			result.setResponseData("Successful.".getBytes());
			result.setResponseMessage(MessageFormat.format("Connection {0} disconnected.", connection));
			result.setResponseCodeOK();
		} catch (Exception e) {
			logger.severe(e.getMessage());
			if (result.getEndTime() == 0) result.sampleEnd(); //avoid re-enter sampleEnd()
			result.setSuccessful(false);
			result.setResponseMessage(MessageFormat.format("Failed to disconnect Connection {0}.", connection));
			result.setResponseData(MessageFormat.format("Client [{0}] failed. Couldn't disconnect connection.", (clientId == null ? "null" : clientId.toString())).getBytes());
			result.setResponseCode("501");
		}
		return result;
	}

}
