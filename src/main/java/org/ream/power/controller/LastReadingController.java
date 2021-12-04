package org.ream.power.controller;

import java.util.Optional;

import org.ream.power.domain.TedPacket;
import org.ream.power.service.LastReadingService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class LastReadingController {
    private LastReadingService lastReadingService;

    public LastReadingController(LastReadingService lastReadingService) {
        this.lastReadingService = lastReadingService;
    }

    @GetMapping(path="/kwh")
    public String kwh() {
        Optional<TedPacket> packet = lastReadingService.getTedPacket();
        if(packet.isPresent()) return packet.get().getKwh().toPlainString();
        else return "-1";
    }

    @GetMapping(path="/volts")
    public String volts() {
        Optional<TedPacket> packet = lastReadingService.getTedPacket();
        if(packet.isPresent()) return packet.get().getVoltsRms().toPlainString();
        else return "-1";
    }

}