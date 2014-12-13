package pl.kedziora.emilek.roomies.database.objects;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import pl.kedziora.emilek.json.objects.enums.EventType;
import pl.kedziora.emilek.json.objects.enums.Interval;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "events")
public class Event extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1403335630633991326L;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String eventName;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Interval eventInterval;

    private Integer intervalNumber;

    @Type(type = "true_false")
    private Boolean switchExecutor;

    private Integer confirmationNumber;

    @Type(type = "true_false")
    private Boolean withPunishment;

    @Enumerated(EnumType.STRING)
    private PunishmentType punishmentType;

    private BigDecimal punishmentAmount;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<EventEntry> entries;

    @ManyToMany
    @JoinTable(name="event_members", joinColumns={@JoinColumn(name="event_id")}, inverseJoinColumns={@JoinColumn(name="user_id")})
    List<User> members;

    public Event() {

    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
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

    public Interval getEventInterval() {
        return eventInterval;
    }

    public void setEventInterval(Interval interval) {
        this.eventInterval = interval;
    }

    public Integer getIntervalNumber() {
        return intervalNumber;
    }

    public void setIntervalNumber(Integer intervalNumber) {
        this.intervalNumber = intervalNumber;
    }

    public Boolean getSwitchExecutor() {
        return switchExecutor;
    }

    public void setSwitchExecutor(Boolean switchExecutor) {
        this.switchExecutor = switchExecutor;
    }

    public Boolean getWithPunishment() {
        return withPunishment;
    }

    public void setWithPunishment(Boolean withPunishment) {
        this.withPunishment = withPunishment;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
    }

    public BigDecimal getPunishmentAmount() {
        return punishmentAmount;
    }

    public void setPunishmentAmount(BigDecimal punishmentAmount) {
        this.punishmentAmount = punishmentAmount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<EventEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EventEntry> entries) {
        this.entries = entries;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public Integer getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(Integer confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
}
