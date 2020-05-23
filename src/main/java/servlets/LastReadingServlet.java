package servlets;

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
        return lastReadingService.getTedPacket().getKwh().toPlainString();
    }

    @GetMapping("/volts")
    public String volts() {
        return lastReadingService.getTedPacket().getVoltsRms().toPlainString();
    }

    public LastReadingService getLastReadingService() {
        return lastReadingService;
    }

    public void setLastReadingService(LastReadingService lastReadingService) {
        this.lastReadingService = lastReadingService;
    }


}