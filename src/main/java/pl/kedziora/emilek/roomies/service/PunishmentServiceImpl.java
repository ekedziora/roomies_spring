package pl.kedziora.emilek.roomies.service;

import com.google.api.client.util.Lists;
import com.google.common.collect.Iterables;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;
import pl.kedziora.emilek.roomies.builder.EventEntryBuilder;
import pl.kedziora.emilek.roomies.database.objects.*;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;
import pl.kedziora.emilek.roomies.repository.FinancialPunishmentRepository;
import pl.kedziora.emilek.roomies.utils.CalendarUtils;
import pl.kedziora.emilek.roomies.utils.CoreUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
public class PunishmentServiceImpl implements PunishmentService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FinancialPunishmentRepository financialPunishmentRepository;

    @Autowired
    private EventEntryRepository eventEntryRepository;

    @Override
    public void executePunishment(EventEntry eventEntry) {
        Event event = eventEntry.getParent();

        if(PunishmentType.FINANCIAL.equals(event.getPunishmentType())) {
            Group group = eventEntry.getExecutor().getGroup();
            if(group != null) {
                FinancialPunishment financialPunishment = new FinancialPunishment();
                financialPunishment.setAmount(event.getPunishmentAmount());
                financialPunishment.setPaymentGroup(group.getPaymentGroups().first());
                financialPunishment.setUser(eventEntry.getExecutor());
                financialPunishmentRepository.save(financialPunishment);
                eventEntry.setEventEntryStatus(EventEntryStatus.FINISHED);
            }
        }
        else {
            Group group = eventEntry.getExecutor().getGroup();
            if(group != null) {
                eventEntry.setEventEntryStatus(EventEntryStatus.NOT_FINISHED);

                if(event.getEventType().equals(EventType.ONCE)) {
                    Period entryPeriod = new Period(eventEntry.getStartDate(), eventEntry.getEndDate());
                    CoreUtils.handleSchedulersOnEntryDelete(Arrays.asList(eventEntry));
                    eventEntryRepository.delete(eventEntry);

                    LocalDate startDate = new LocalDate();
                    LocalDate endDate = startDate.plus(entryPeriod);
                    EventEntry newEntry = EventEntryBuilder.anEventEntry()
                            .withStartDate(startDate)
                            .withEndDate(endDate)
                            .withExecutor(eventEntry.getExecutor())
                            .withParent(event)
                            .build();
                    eventEntryRepository.save(newEntry);
                }
                else {
                    List<EventEntry> entriesAfterNotFinished =
                            eventEntryRepository.findByParentAndStartDateGreaterThan(event, eventEntry.getStartDate());
                    CoreUtils.handleSchedulersOnEntryDelete(entriesAfterNotFinished);
                    eventEntryRepository.delete(entriesAfterNotFinished);

                    Collections.sort(entriesAfterNotFinished);
                    EventEntry first = entriesAfterNotFinished.get(0);
                    LocalDate startDate = first.getStartDate();
                    LocalDate endDate = first.getEndDate();

                    Period period = CoreUtils.createCyclicEventPeriod(eventEntry.getStartDate(), eventEntry.getEndDate(), event.getEventInterval(), event.getIntervalNumber());
                    List<User> members = event.getMembers();
                    CoreUtils.moveElementToFirstPlace(members, eventEntry.getExecutor());

                    List<EventEntry> entries = generateEntriesForCyclicEvent(startDate, endDate, period, event, members);
                    eventEntryRepository.save(entries);
                }
            }
        }
    }

    private List<EventEntry> generateEntriesForCyclicEvent(LocalDate start, LocalDate end, Period period, Event event, List<User> executors) {
        List<EventEntry> entries = Lists.newArrayList();
        Iterator<User> cycleIterator = Iterables.cycle(executors).iterator();
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        for(; end.isBefore(CalendarUtils.MAGIC_END_DATE); start = start.plus(period), end = end.plus(period)) {
            EventEntry newEntry = EventEntryBuilder.anEventEntry()
                    .withStartDate(start)
                    .withEndDate(end)
                    .withParent(event)
                    .withExecutor(cycleIterator.next())
                    .build();
            if(event.getWithPunishment()) {
                ScheduledFuture<?> task = scheduler.schedule(
                        (Runnable) applicationContext.getBean("entryEndTask", newEntry.getUuid()),
                        new LocalDateTime().plusSeconds(30).toDate());
                int taskKey = CoreUtils.addTask(task);
                newEntry.setEndEntrySchedulerKey(taskKey);
            }

            entries.add(newEntry);
        }

        return entries;
    }

}
