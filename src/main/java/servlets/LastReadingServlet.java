package servlets;

import java.util.Optional;

import org.ream.power.domain.TedPacket;
import org.ream.power.service.LastReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LastReadingServlet {
    @Autowired
    private LastReadingService lastReadingService;

    @GetMapping("/kwh")
    public String kwh() {
        Optional<TedPacket> packet = lastReadingService.getTedPacket();
        if(packet.isPresent()) return packet.get().getKwh().toPlainString();
        else return "-1";
    }

    @GetMapping("/volts")
    public String volts() {
        Optional<TedPacket> packet = lastReadingService.getTedPacket();
        if(packet.isPresent()) return packet.get().getVoltsRms().toPlainString();
        else return "-1";
    }

    public LastReadingService getLastReadingService() {
        return lastReadingService;
    }

    public void setLastReadingService(LastReadingService lastReadingService) {
        this.lastReadingService = lastReadingService;
    }


}