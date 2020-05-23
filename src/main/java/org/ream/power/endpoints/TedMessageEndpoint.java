package org.ream.power.endpoints;

import java.nio.ByteBuffer;

import org.ream.power.domain.TedPacket;
import org.ream.power.service.LastReadingService;
import org.ream.power.service.TedPacketTranslationService;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@MessageEndpoint
public class TedMessageEndpoint {
	private final TedPacketTranslationService translationService;
	private final LastReadingService lastReadingService;

	public TedMessageEndpoint(TedPacketTranslationService translationService, LastReadingService lastReadingService) {
		this.translationService = translationService;
		this.lastReadingService = lastReadingService;
	}

	@ServiceActivator
	public TedPacket fetchMessage(ByteBuffer packet) throws Exception {
		TedPacket tedPacket = this.translationService.parsePacket(packet);
		lastReadingService.setTedPacket(tedPacket);
		return tedPacket;
	}
}
