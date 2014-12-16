package pl.kedziora.emilek.roomies.task;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;

public class ConfirmationEndTask implements Runnable {

    Logger log = Logger.getLogger(ConfirmationEndTask.class);

    @Autowired
    private EventEntryRepository eventEntryRepository;

    private String entryUuid;

    public ConfirmationEndTask() {
    }

    public ConfirmationEndTask(String entryUuid) {
        this.entryUuid = entryUuid;
    }

    @Override
    @Transactional
    public void run() {
        EventEntry entry = eventEntryRepository.findByUuid(entryUuid);
        log.info("Confirmation end task executed at " + new LocalDate());
        if (entry != null && entry.getConfirmation() != null && entry.getConfirmation().equals(EventEntryStatus.FINISHED)) {
            entry.setEventEntryStatus(EventEntryStatus.FINISHED);
            eventEntryRepository.save(entry);
        }
    }


}
