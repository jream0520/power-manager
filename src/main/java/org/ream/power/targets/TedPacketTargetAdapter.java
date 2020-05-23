package org.ream.power.targets;

import org.ream.power.domain.TedPacket;

public class TedPacketTargetAdapter {

	public void writeMessage(TedPacket packet) {
		System.out.println(packet.toString());
	}
	
}
