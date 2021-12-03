package org.ream.power.sources;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Logger;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fazecast.jSerialComm.SerialPort;


public class FakeTedPacketSourceAdapter extends TedPacketSourceAdapter implements MessageSource<ByteBuffer>, InitializingBean, DisposableBean {

	private static final Logger log = Logger.getLogger(FakeTedPacketSourceAdapter.class.getName());
	private String comPortName;
	private SerialPort comPort;
	private boolean running = false;

	private static final String testRecord = "1004060158020000000000000000000080001d0100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fd03000000000000000000000000000000000000000007e8030088000054454420000000000083170f270f270f27e803dc0572047c01a00405006c047d8d056e0504690008058300c9a21a0083161a00798e00006e294804e42d5f040479416d04c1047a41e30334047b416203b0037c41db0431056c41e7043e0571414905a1057241b6040b0572b926026e027341d1011702744119046b0475414c039a037641cb031c040d011b00830402051306b80504dc8102000000000044e5640000000000fd031003";

	public FakeTedPacketSourceAdapter(String comPortName) {
		super(comPortName);
	}

	@Override
	public void afterPropertiesSet() {
		log.info("Fake Bound to COM port:" + comPortName);
		running = true;
	}
	
	@Override
	public Message<ByteBuffer> receive() {
		log.fine("Sending:  FAKE");
		byte[] readBuffer = hexStringToByteArray(testRecord);
	    int numRead = testRecord.length() / 2;
		if(numRead >= 278) {
			return new GenericMessage<>(ByteBuffer.wrap(Arrays.copyOfRange(readBuffer, 0, numRead)).order(ByteOrder.LITTLE_ENDIAN));
		} else {
			return null;
		}
	}
	
	@Override
	public void destroy() {
		log.info("Closing COM port...");
		running = false;
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}
	
	public String getComPortName() {
		return comPortName;
	}
		
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
