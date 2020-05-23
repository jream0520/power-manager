package org.ream.power.service;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Timestamp;
import java.util.Arrays;

import org.ream.power.domain.TedPacket;
import org.ream.power.domain.UnsignedInt;
import org.springframework.stereotype.Service;

@Service
public class TedPacketTranslationService {
	private final ServiceProperties configuration;

	public TedPacketTranslationService(ServiceProperties configuration) {
		this.configuration = configuration;
	}

	public TedPacket parsePacket(ByteBuffer packet) {
		  byte escape = (byte)0x10;
		  byte start = (byte)0x04;
		  byte end = (byte)0x03;
		  boolean sawEscape = false;

	      byte[] parsedBytes = new byte[4096];
	      int x = -1;
	      while(packet.hasRemaining()) {
	    	  byte b = packet.get();
	    	  if(sawEscape) {
	    		  sawEscape = false;
		    	  if(b == escape) {
		    		  parsedBytes[x++] = escape;
		    	  } else if(b == start) {
		    		  parsedBytes = new byte[4096];
		    		  x = 0;
		    	  } else if(b == end) {
		    		  if(x >= 0) {
		    			  TedPacket tedPacket = new TedPacket();
		    			  tedPacket.setReadingTime(new Timestamp(System.currentTimeMillis()));

		    			  UnsignedInt uInt = new UnsignedInt();
		    			  uInt.setByteBuffer(ByteBuffer.wrap(Arrays.copyOfRange(parsedBytes, 251, 255)).order(ByteOrder.LITTLE_ENDIAN), 0);
		    			  tedPacket.setVoltsRms(BigDecimal.valueOf((uInt.uInt.get() * 0.1)));

		    			  UnsignedInt uInt2 = new UnsignedInt();
		    			  uInt2.setByteBuffer(ByteBuffer.wrap(Arrays.copyOfRange(parsedBytes, 247, 251)).order(ByteOrder.LITTLE_ENDIAN), 0);
		    			  tedPacket.setKwh(BigDecimal.valueOf((uInt2.uInt.get() * 0.01)));
			    		  x = -1;					    		  
			    		  return tedPacket;
		    		  }
		    	  } else {
		    		  System.out.println("Unknown byte:" + b);
		    	  }
	    	  } else if(b == escape) {
	    		  sawEscape = true;
	    	  } else if(x >= 0) {
	    		  parsedBytes[x++] = b;
	    	  }
	      }
    
		return null;
	}
}
