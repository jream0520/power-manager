package org.ream.power.service;

import java.util.Optional;

import org.ream.power.domain.TedPacket;
import org.springframework.stereotype.Component;

@Component
public class LastReadingService {
    private TedPacket tedPacket;

    public Optional<TedPacket> getTedPacket() {
        return Optional.ofNullable(tedPacket);
    }

    public void setTedPacket(TedPacket tedPacket) {
        this.tedPacket = tedPacket;
    }

}