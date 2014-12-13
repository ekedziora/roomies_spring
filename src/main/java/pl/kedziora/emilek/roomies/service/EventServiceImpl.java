package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.*;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.json.objects.params.DeleteEventParams;
import pl.kedziora.emilek.json.objects.params.DoneEntryParams;
import pl.kedziora.emilek.roomies.annotation.Secured;
import pl.kedziora.emilek.roomies.builder.EventBuilder;
import pl.kedziora.emilek.roomies.builder.EventEntryBuilder;
import pl.kedziora.emilek.roomies.database.objects.*;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;
import pl.kedziora.emilek.roomies.repository.EventRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;
import pl.kedziora.emilek.roomies.utils.CalendarUtils;
import pl.kedziora.emilek.roomies.utils.CoreUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEntryRepository eventEntryRepository;

    @Override
    public AddEventData getAddEventData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        List<MemberToAddData> members = generateMembersFromUsers(group.getMembers());

        return new AddEventData(members);
    }

    private List<MemberToAddData> generateMembersFromUsers(List<User> users) {
        return Lists.newArrayList(
                Collections2.transform(users, new Function<User, MemberToAddData>() {
                    @Override
                    public MemberToAddData apply(User user) {
                        return new MemberToAddData(user.getName(), user.getId());
                    }
                })
        );
    }

    @Override
    public void saveEvent(AddEventParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        Event event = EventBuilder.anEvent()
                .withGroup(group)
                .withAdmin(user)
                .withEndDate(new LocalDate(params.getEndDate()))
                .withEventType(params.getEventType())
                .withEventInterval(params.getInterval())
                .withIntervalNumber(params.getIntervalNumber())
                .withPunishmentAmount(params.getPunishmentAmount())
                .withPunishmentType(params.getPunishmentType())
                .withEventName(params.getName())
                .withStartDate(new LocalDate(params.getStartDate()))
                .withSwitchExecutor(params.getSwitchExecutor())
                .withWithPunishment(params.getWithPunishment())
                .withMembers(generateUsersFromMembers(params.getMembers()))
                .build();
        event.setEntries(handleCreateEventEntries(event, params.getMembers()));

        eventRepository.save(event);
    }

    private List<EventEntry> handleCreateEventEntries(Event event, List<MemberToAddData> members) {
        List<EventEntry> entries = Lists.newArrayList();
        List<User> executors = generateUsersFromMembers(members);

        if(EventType.ONCE.equals(event.getEventType())) {
            EventEntry eventEntry = EventEntryBuilder.anEventEntry()
                    .withStartDate(event.getStartDate())
                    .withEndDate(event.getEndDate())
                    .withExecutor(executors.get(0))
                    .withParent(event)
                    .build();

            entries.add(eventEntry);
        }
        else {
            LocalDate start = event.getStartDate();
            LocalDate end = event.getEndDate();
            Period period = CoreUtils.createCyclicEventPeriod(start, end, event.getEventInterval(), event.getIntervalNumber());

            Iterator<User> cycleIterator = Iterables.cycle(executors).iterator();
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.initialize();

            for(; end.isBefore(CalendarUtils.MAGIC_END_DATE); start = start.plus(period), end = end.plus(period)) {
                EventEntry eventEntry = EventEntryBuilder.anEventEntry()
                        .withStartDate(start)
                        .withEndDate(end)
                        .withExecutor(cycleIterator.next())
                        .withParent(event)
                        .build();

                if(event.getWithPunishment()) {
                    ScheduledFuture<?> task = scheduler.schedule(
                            (Runnable) applicationContext.getBean("entryEndTask", eventEntry.getUuid()),
                            new LocalDateTime().plusSeconds(30).toDate());
                    int taskKey = CoreUtils.addTask(task);
                    eventEntry.setEndEntrySchedulerKey(taskKey);
                }

                entries.add(eventEntry);
            }
        }

        return entries;
    }

    private List<User> generateUsersFromMembers(List<MemberToAddData> members) {
        return Lists.newArrayList(
                Collections2.transform(members, new Function<MemberToAddData, User>() {
                    @Override
                    public User apply(MemberToAddData memberToAddData) {
                        return userRepository.findOne(memberToAddData.getId());
                    }
                })
        );
    }

    @Override
    public EventData getEventData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return new EventData();
        }

        List<EventEntryData> currentEntries = getCurrentEntries(user);
        List<EventEntryData> nextEntries = getNextEntries(user);
        List<SingleEventData> events = getEvents(user);

        return new EventData(currentEntries, nextEntries, events);
    }

    private List<SingleEventData> getEvents(User user) {
        List<Event> events = eventRepository.findByAdmin(user);

        events = Lists.newArrayList(Collections2.filter(events, new Predicate<Event>() {
            @Override
            public boolean apply(@Nullable Event event) {
                return EventType.CYCLIC.equals(event.getEventType()) ||
                        EventType.ONCE.equals(event.getEventType()) &&
                                EventEntryStatus.NOT_FINISHED.equals(event.getEntries().get(0).getEventEntryStatus());
            }
        }));

        return Lists.newArrayList(
                Collections2.transform(events, new Function<Event, SingleEventData>() {
                    @Override
                    public SingleEventData apply(@Nullable Event event) {
                        return new SingleEventData(event.getId(), event.getEventName(), event.getEventType(), event.getIntervalNumber(),
                                event.getEventInterval(), event.getWithPunishment(), generateMembersFromUsers(event.getMembers()));
                    }
                })
        );
    }

    private List<EventEntryData> getNextEntries(User user) {
        List<EventEntry> nextEntries = eventEntryRepository.findByExecutorAndStartDateGreaterThan(user, new LocalDate(), new PageRequest(0, 10));

        return generateEventEntryData(nextEntries);
    }

    private List<EventEntryData> getCurrentEntries(User user) {
        List<EventEntry> currentEntries = eventEntryRepository.findByExecutorAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndEventEntryStatus(user, new LocalDate(), new LocalDate(), EventEntryStatus.NOT_FINISHED);

        return generateEventEntryData(currentEntries);
    }

    private List<EventEntryData> generateEventEntryData(List<EventEntry> entries) {
        return Lists.newArrayList(
                Collections2.transform(entries, new Function<EventEntry, EventEntryData>() {
                    @Override
                    public EventEntryData apply(@Nullable EventEntry eventEntry) {
                        return new EventEntryData(eventEntry.getId(), eventEntry.getParent().getEventName(), eventEntry.getStartDate().toString(),
                                eventEntry.getEndDate().toString(), eventEntry.getEventEntryStatus().getLabel());
                    }
                })
        );
    }

    @Override
    public List<EventEntryData> getAllEntriesForUser(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        List<EventEntry> entries = eventEntryRepository.findByExecutor(user);
        return generateEventEntryData(entries);
    }

    @Override
    @Secured("Other user deleting someone's event")
    public void deleteEvent(DeleteEventParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        Event event = eventRepository.findOne(params.getPaymentId());

        if(!event.getAdmin().equals(user)) {
            throw new BadRequestException();
        }

        eventRepository.delete(event);
    }

    @Override
    @Secured("Other user mark as done someone's event entry")
    public void entryDone(DoneEntryParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        EventEntry eventEntry = eventEntryRepository.findOne(params.getEntryId());

        if(!eventEntry.getExecutor().equals(user)) {
            throw new BadRequestException();
        }

        eventEntry.setEventEntryStatus(EventEntryStatus.FINISHED);
        //jesli z punishmentem to dodatkowe rzeczy

        eventEntryRepository.save(eventEntry);
    }
}
