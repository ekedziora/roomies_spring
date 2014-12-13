package pl.kedziora.emilek.roomies.builder;

import org.joda.time.LocalDate;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;
import pl.kedziora.emilek.roomies.database.objects.Event;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.math.BigDecimal;
import java.util.List;

public class EventBuilder {
    List<User> members;
    private EventType eventType;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Interval eventInterval;
    private Integer intervalNumber;
    private Boolean switchExecutor;
    private Integer confirmationNumber;
    private Boolean withPunishment;
    private PunishmentType punishmentType;
    private BigDecimal punishmentAmount;
    private Group group;
    private User admin;
    private List<EventEntry> entries;

    private EventBuilder() {
    }

    public static EventBuilder anEvent() {
        return new EventBuilder();
    }

    public EventBuilder withEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventBuilder withEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public EventBuilder withStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public EventBuilder withEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public EventBuilder withEventInterval(Interval eventInterval) {
        this.eventInterval = eventInterval;
        return this;
    }

    public EventBuilder withIntervalNumber(Integer intervalNumber) {
        this.intervalNumber = intervalNumber;
        return this;
    }

    public EventBuilder withSwitchExecutor(Boolean switchExecutor) {
        this.switchExecutor = switchExecutor;
        return this;
    }

    public EventBuilder withConfirmationNumber(Integer confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
        return this;
    }

    public EventBuilder withWithPunishment(Boolean withPunishment) {
        this.withPunishment = withPunishment;
        return this;
    }

    public EventBuilder withPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
        return this;
    }

    public EventBuilder withPunishmentAmount(BigDecimal punishmentAmount) {
        this.punishmentAmount = punishmentAmount;
        return this;
    }

    public EventBuilder withGroup(Group group) {
        this.group = group;
        return this;
    }

    public EventBuilder withAdmin(User admin) {
        this.admin = admin;
        return this;
    }

    public EventBuilder withEntries(List<EventEntry> entries) {
        this.entries = entries;
        return this;
    }

    public EventBuilder withMembers(List<User> members) {
        this.members = members;
        return this;
    }

    public EventBuilder but() {
        return anEvent().withEventType(eventType).withEventName(eventName).withStartDate(startDate).withEndDate(endDate).withEventInterval(eventInterval).withIntervalNumber(intervalNumber).withSwitchExecutor(switchExecutor).withConfirmationNumber(confirmationNumber).withWithPunishment(withPunishment).withPunishmentType(punishmentType).withPunishmentAmount(punishmentAmount).withGroup(group).withAdmin(admin).withEntries(entries).withMembers(members);
    }

    public Event build() {
        Event event = new Event();
        event.setEventType(eventType);
        event.setEventName(eventName);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setEventInterval(eventInterval);
        event.setIntervalNumber(intervalNumber);
        event.setSwitchExecutor(switchExecutor);
        event.setConfirmationNumber(confirmationNumber);
        event.setWithPunishment(withPunishment);
        event.setPunishmentType(punishmentType);
        event.setPunishmentAmount(punishmentAmount);
        event.setGroup(group);
        event.setAdmin(admin);
        event.setEntries(entries);
        event.setMembers(members);
        return event;
    }
}
