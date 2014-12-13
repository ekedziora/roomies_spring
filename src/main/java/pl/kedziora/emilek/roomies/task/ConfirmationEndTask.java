package pl.kedziora.emilek.roomies.task;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;
import pl.kedziora.emilek.roomies.service.PunishmentService;

public class ConfirmationEndTask implements Runnable {

    Logger log = Logger.getLogger(ConfirmationEndTask.class);

    @Autowired
    private EventEntryRepository eventEntryRepository;

    @Autowired
    private PunishmentService punishmentService;

    private Long entryId;

    public ConfirmationEndTask() {}

    public ConfirmationEndTask(Long entryId) {
        this.entryId = entryId;
    }

    @Override
    @Transactional
    public void run() {
        EventEntry entry = eventEntryRepository.findOne(entryId);
        log.info("Confirmation end task executed at " + new LocalDate());
        if (entry != null && entry.getConfirmation() != null && entry.getConfirmation().getStatus().equals(EventEntryStatus.NOT_FINISHED)) {
            punishmentService.executePunishment(entry);
        }
    }
}
