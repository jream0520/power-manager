package org.ream.power.sources;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fazecast.jSerialComm.SerialPort;


public class TedPacketSourceAdapter implements MessageSource<ByteBuffer>, InitializingBean, DisposableBean {

	private static final Logger log = Logger.getLogger(TedPacketSourceAdapter.class.getName());
	private String comPortName;
	private SerialPort comPort;
	private boolean running = false;

	@Override
	public void afterPropertiesSet() {
		for(SerialPort cp : SerialPort.getCommPorts()) {
			if(comPortName.equals(cp.getSystemPortName())) {
				this.comPort = cp;
				log.info("Bound to COM port:" + comPort.getSystemPortName());
				 comPort.setComPortParameters(19200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
				 comPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
				 comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2000, 0);
				 comPort.openPort();
			}
		}
		if(comPort == null) {
			throw new RuntimeException("Unable to find COM port:" + comPortName);
		}
		running = true;
	}
	
	@Override
	public Message<ByteBuffer> receive() {
		byte[] writeBuffer = new byte[1];
		writeBuffer[0] = (byte)0xaa;
		  		  
		log.fine("Sending:" + Hex.encodeHexString(writeBuffer));
		comPort.writeBytes(writeBuffer, 1L);
		byte[] readBuffer = new byte[4096];
		int numRead = comPort.readBytes(readBuffer, readBuffer.length);
		if(numRead >= 278) {
			return new GenericMessage<>(ByteBuffer.wrap(Arrays.copyOfRange(readBuffer, 0, numRead)).order(ByteOrder.LITTLE_ENDIAN));
		} else {
			return null;
		}
	}
	
	@Override
	public void destroy() {
		log.info("Closing COM port...");
		comPort.closePort();
		running = false;
	}
	
	//@Override
	public boolean isRunning() {
		return running;
	}
	
	public String getComPortName() {
		return comPortName;
	}
	
	@Required
	public void setComPortName(String comPortName) {
		this.comPortName = comPortName;
	}

}
