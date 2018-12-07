package net.xmeter.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import net.xmeter.Constants;
import net.xmeter.samplers.DisConnectAllSampler;


public class DisConnectAllSamplerUI extends AbstractSamplerGui implements Constants {
	private static final long serialVersionUID = 1666890646673145131L;
	
	public DisConnectAllSamplerUI() {
		this.init();
	}

	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);
		JPanel mainPanel = new VerticalPanel();
		add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	public void configure(TestElement element) {
		super.configure(element);
	}

	@Override
	public TestElement createTestElement() {
		DisConnectAllSampler sampler = new DisConnectAllSampler();
		this.configureTestElement(sampler);
		return sampler;
	}

	@Override
	public String getLabelResource() {
		throw new RuntimeException();
	}

	@Override
	public String getStaticLabel() {
		return "MQTT All DisConnect";
	}

	@Override
	public void modifyTestElement(TestElement arg0) {
		DisConnectAllSampler sampler = (DisConnectAllSampler)arg0;
		this.configureTestElement(sampler);
	}

	@Override
	public void clearGui() {
		super.clearGui();
	}

}
