package pl.kedziora.emilek.roomies.database.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "execution_confirmations")
public class ExecutionConfirmation extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -67825515214468414L;

    @OneToOne(mappedBy = "confirmation")
    private EventEntry eventEntry;

    @Enumerated(EnumType.STRING)
    private EventEntryStatus status = EventEntryStatus.FINISHED;

    public EventEntry getEventEntry() {
        return eventEntry;
    }

    public void setEventEntry(EventEntry eventEntry) {
        this.eventEntry = eventEntry;
    }

    public EventEntryStatus getStatus() {
        return status;
    }

    public void setStatus(EventEntryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("eventEntry", eventEntry)
                .append("status", status)
                .toString();
    }

}
