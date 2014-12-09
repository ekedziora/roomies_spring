package pl.kedziora.emilek.roomies.task;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;
import pl.kedziora.emilek.roomies.service.PunishmentService;

public class EntryEndTask implements Runnable {

    Logger log = Logger.getLogger(EntryEndTask.class);

    @Autowired
    private EventEntryRepository eventEntryRepository;

    @Autowired
    private PunishmentService punishmentService;

    private String entryUuid;

    public EntryEndTask() {}

    public EntryEndTask(String entryUuid) {
        this.entryUuid = entryUuid;
    }

    @Override
    @Transactional
    public void run() {
        EventEntry entry = eventEntryRepository.findByUuid(entryUuid);
        log.info("Entry end task executed at " + new LocalDate());
        if (entry != null && entry.getEventEntryStatus().equals(EventEntryStatus.NOT_FINISHED)) {
            punishmentService.executePunishment(entry);
        }
    }
}
