package pl.kedziora.emilek.roomies.builder;

import org.joda.time.LocalDate;
import pl.kedziora.emilek.roomies.database.objects.Event;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.database.objects.User;

public class EventEntryBuilder {
    private LocalDate startDate;
    private LocalDate endDate;
    private EventEntryStatus eventEntryStatus = EventEntryStatus.NOT_FINISHED;
    private User executor;
    private Event parent;
    private Integer endEntrySchedulerKey;
    private Integer confirmationCheckSchedulerKey;

    private EventEntryBuilder() {
    }

    public static EventEntryBuilder anEventEntry() {
        return new EventEntryBuilder();
    }

    public EventEntryBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public EventEntryBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public EventEntryBuilder withEventEntryStatus(EventEntryStatus eventEntryStatus) {
        this.eventEntryStatus = eventEntryStatus;
        return this;
    }

    public EventEntryBuilder withExecutor(User executor) {
        this.executor = executor;
        return this;
    }

    public EventEntryBuilder withParent(Event parent) {
        this.parent = parent;
        return this;
    }

    public EventEntryBuilder withEndEntrySchedulerKey(Integer endEntrySchedulerKey) {
        this.endEntrySchedulerKey = endEntrySchedulerKey;
        return this;
    }

    public EventEntryBuilder withConfirmationCheckSchedulerKey(Integer confirmationCheckSchedulerKey) {
        this.confirmationCheckSchedulerKey = confirmationCheckSchedulerKey;
        return this;
    }

    public EventEntryBuilder but() {
        return anEventEntry().withStartDate(startDate).withEndDate(endDate).withEventEntryStatus(eventEntryStatus).withExecutor(executor).withParent(parent).withEndEntrySchedulerKey(endEntrySchedulerKey).withConfirmationCheckSchedulerKey(confirmationCheckSchedulerKey);
    }

    public EventEntry build() {
        EventEntry eventEntry = new EventEntry();
        eventEntry.setStartDate(startDate);
        eventEntry.setEndDate(endDate);
        eventEntry.setEventEntryStatus(eventEntryStatus);
        eventEntry.setExecutor(executor);
        eventEntry.setParent(parent);
        eventEntry.setEndEntrySchedulerKey(endEntrySchedulerKey);
        eventEntry.setConfirmationCheckSchedulerKey(confirmationCheckSchedulerKey);
        return eventEntry;
    }
}
