package pl.kedziora.emilek.roomies.database.objects;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "event_entries")
public class EventEntry extends BaseEntity implements Serializable {

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
}
