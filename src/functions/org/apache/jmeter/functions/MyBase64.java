package org.apache.jmeter.functions;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import sun.misc.BASE64Encoder;

public class MyBase64 extends AbstractFunction {
	
	private static final List<String>  desc = new LinkedList<String>();

	static {
		desc.add("图片路径");
	}
	
	static {
		desc.add("图片base64后存放的变量");
	}
	
	private static final String KEY = "__MyBase64";
	
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
		String imagePath = ((CompoundVariable)this.values[0]).execute();
		String imageBase64ResultResult = getImgBase64(imagePath);
		
		System.out.println(imageBase64ResultResult);
		
		if( (localJMeterVariables != null ) && (this.values.length > 1)) {
			String imageVariable = ((CompoundVariable) this.values[1]).execute().trim();
			localJMeterVariables.put(imageVariable, imageBase64ResultResult);
		}
		return imageBase64ResultResult;
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

    public String getImgBase64(String filePath) {
        InputStream in = null;
        byte[] data = null;
        String result = null;
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();

            BASE64Encoder encoder = new BASE64Encoder();
            result = encoder.encode(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result.replaceAll("\r|\n", "");
    }	
}
