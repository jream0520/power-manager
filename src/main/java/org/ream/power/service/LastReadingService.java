package org.ream.power.service;

import org.ream.power.domain.TedPacket;
import org.springframework.stereotype.Component;

@Component
public class LastReadingService {
    private TedPacket tedPacket;

    public TedPacket getTedPacket() {
        return tedPacket;
    }

    public void setTedPacket(TedPacket tedPacket) {
        this.tedPacket = tedPacket;
    }

}