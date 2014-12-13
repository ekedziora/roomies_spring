package pl.kedziora.emilek.roomies.service;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.ExecutionConfirmation;
import pl.kedziora.emilek.roomies.utils.CoreUtils;

import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
public class ConfirmationServiceImpl implements ConfirmationService {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void startConfirmationProcess(EventEntry eventEntry) {
            ExecutionConfirmation confirmation = new ExecutionConfirmation();
            confirmation.setEventEntry(eventEntry);
            eventEntry.setConfirmation(confirmation);

            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize();

            ScheduledFuture<?> task = scheduler.schedule(
                    (Runnable) applicationContext.getBean("confirmationEndTask", eventEntry.getId()),
                    new LocalDateTime().plusMinutes(5).toDate()); //TODO zmienić na normalny czas z eventu
            int taskKey = CoreUtils.addTask(task);
            eventEntry.setConfirmationCheckSchedulerKey(taskKey);
    }

}
