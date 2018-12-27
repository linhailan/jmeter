package org.apache.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;

public class MyBase64ForString extends AbstractFunction {
	
	private static final List<String>  desc = new LinkedList<String>();

	static {
		desc.add("待编码的字符串");
	}
	
	static {
		desc.add("字符串base64编码后存放的变量");
	}
	
	private static final String KEY = "__MyBase64ForString";
	
	//存放传入参数的值的变量
	private Object[] values;
	
	
	//描述参数  
	@Override
	public List<String> getArgumentDesc() {
		return desc;
	}

	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
		JMeterVariables localJMeterVariables = getVariables();
		String dataToBeEncode = ((CompoundVariable)this.values[0]).execute();
		String encodeResult = getStringBase64(dataToBeEncode);
		

		
		if( (localJMeterVariables != null ) && (this.values.length > 1)) {
			String encodeDataVariable = ((CompoundVariable) this.values[1]).execute().trim();
			localJMeterVariables.put(encodeDataVariable, encodeResult);
		}
		return encodeResult;
	}

	@Override
	public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
		checkParameterCount(parameters, 1,2);
		this.values = parameters.toArray();
	}

	@Override
	public String getReferenceKey() {
		return KEY;
	}

    public String getStringBase64(String dataToBeEncode) {
    	     byte[] sb = dataToBeEncode.getBytes();
    	     String result = new sun.misc.BASE64Encoder().encode(sb);
    	     return result.replaceAll("\r|\n", "");
    }	
}
