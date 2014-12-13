package pl.kedziora.emilek.roomies.database.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_entries")
public class EventEntry extends BaseEntity implements Serializable, Comparable<EventEntry> {

    private static final long serialVersionUID = 3057969906390599792L;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private EventEntryStatus eventEntryStatus = EventEntryStatus.NOT_FINISHED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User executor;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event parent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "confirmation_id")
    private ExecutionConfirmation confirmation;

    @Transient
    private Integer endEntrySchedulerKey;

    @Transient
    private Integer confirmationCheckSchedulerKey;

    public EventEntry() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public EventEntryStatus getEventEntryStatus() {
        return eventEntryStatus;
    }

    public void setEventEntryStatus(EventEntryStatus eventEntryStatus) {
        this.eventEntryStatus = eventEntryStatus;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public Event getParent() {
        return parent;
    }

    public void setParent(Event parent) {
        this.parent = parent;
    }

    public ExecutionConfirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(ExecutionConfirmation confirmation) {
        this.confirmation = confirmation;
    }

    public Integer getEndEntrySchedulerKey() {
        return endEntrySchedulerKey;
    }

    public void setEndEntrySchedulerKey(Integer endEntrySchedulerKey) {
        this.endEntrySchedulerKey = endEntrySchedulerKey;
    }

    public Integer getConfirmationCheckSchedulerKey() {
        return confirmationCheckSchedulerKey;
    }

    public void setConfirmationCheckSchedulerKey(Integer confirmationCheckSchedulerKey) {
        this.confirmationCheckSchedulerKey = confirmationCheckSchedulerKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("parent", parent)
                .append("executor", executor)
                .append("eventEntryStatus", eventEntryStatus)
                .append("endDate", endDate)
                .append("startDate", startDate)
                .toString();
    }

    @Override
    public int compareTo(EventEntry o) {
        if(o == null || o.getStartDate() == null) {
            return 1;
        }
        return startDate.compareTo(o.getStartDate());
    }
}
