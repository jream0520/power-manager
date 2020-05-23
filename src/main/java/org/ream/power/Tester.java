package org.ream.power;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.ream.power.domain.UnsignedInt;

public class Tester {
	
	private static final String testRecord = "1004060158020000000000000000000080001d0100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000fd03000000000000000000000000000000000000000007e8030088000054454420000000000083170f270f270f27e803dc0572047c01a00405006c047d8d056e0504690008058300c9a21a0083161a00798e00006e294804e42d5f040479416d04c1047a41e30334047b416203b0037c41db0431056c41e7043e0571414905a1057241b6040b0572b926026e027341d1011702744119046b0475414c039a037641cb031c040d011b00830402051306b80504dc8102000000000044e5640000000000fd031003";

	public static void main(String[] args) {
		
		  byte escape = (byte)0x10;
		  byte start = (byte)0x04;
		  byte end = (byte)0x03;
		  boolean sawEscape = false;
	
	      byte[] readBuffer = new byte[4096];
	      readBuffer = hexStringToByteArray(testRecord);
	      int numRead = testRecord.length() / 2;
	      System.out.println(Hex.encodeHexString(Arrays.copyOfRange(readBuffer, 0, numRead + 10)));
	      if(numRead >= 278) {
		      byte[] parsedBytes = new byte[4096];
		      int x = -1;
		      for(byte b : Arrays.copyOfRange(readBuffer, 0, numRead)) {
		    	  if(sawEscape) {
		    		  sawEscape = false;
			    	  if(b == escape) {
			    		  parsedBytes[x++] = escape;
			    	  } else if(b == start) {
			    		  parsedBytes = new byte[4096];
			    		  x = 0;
			    	  } else if(b == end) {
			    		  if(x >= 0) {
			    			  System.out.println("PARSED: " + x + "::" + Hex.encodeHexString(Arrays.copyOfRange(parsedBytes, 0, x)));
			    			  System.out.println("PARSE2: " + x + "::" + Hex.encodeHexString(Arrays.copyOfRange(parsedBytes, 251, 255)));
			    			  UnsignedInt uInt = new UnsignedInt();
			    			  uInt.setByteBuffer(ByteBuffer.wrap(Arrays.copyOfRange(parsedBytes, 251, 255)).order(ByteOrder.LITTLE_ENDIAN), 0);
			    			  System.out.println("VRMS:" + (uInt.uInt.get() * 0.1));

			    			  System.out.println("PARSE3: " + x + "::" + Hex.encodeHexString(Arrays.copyOfRange(parsedBytes, 247, 251)));
			    			  UnsignedInt uInt2 = new UnsignedInt();
			    			  uInt2.setByteBuffer(ByteBuffer.wrap(Arrays.copyOfRange(parsedBytes, 247, 251)).order(ByteOrder.LITTLE_ENDIAN), 0);
			    			  System.out.println("KWH:" + (uInt2.uInt.get() * 0.01));
				    		  x = -1;					    		  
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
	      }
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
