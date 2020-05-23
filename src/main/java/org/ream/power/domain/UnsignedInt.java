package org.ream.power.domain;

import java.nio.ByteOrder;

import javolution.io.Struct;

public class UnsignedInt extends Struct {
	public Unsigned16 uInt = new Unsigned16();

	@Override
	public boolean isPacked() {
		return false;
	}
	
	 public ByteOrder byteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
	 }

}
