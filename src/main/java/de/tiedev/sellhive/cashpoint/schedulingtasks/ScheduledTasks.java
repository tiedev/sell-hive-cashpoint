package de.tiedev.sellhive.cashpoint.schedulingtasks;

import de.tiedev.sellhive.cashpoint.services.DataExportService;
import de.tiedev.sellhive.cashpoint.services.DataImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    DataExportService dataExportService;

    @Autowired
    DataImportService dataImportService;

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        dataExportService.updateSellHiveSoldStatus();
        log.info("The time is now {}", dateFormat.format(new Date()));
        System.out.println("The time is now " + dateFormat.format(new Date()));
        dataImportService.updateGames();
        System.out.println("sold status updated in local database");
    }
}
