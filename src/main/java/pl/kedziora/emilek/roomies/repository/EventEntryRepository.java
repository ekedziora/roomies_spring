package pl.kedziora.emilek.roomies.repository;

import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {

    @Query("select e from EventEntry e where e.executor = ?1 and e.startDate <= ?2 and e.endDate >= ?2")
    List<EventEntry> findByExecutorAndStartDateLessThanOr(User user, LocalDate date);

    List<EventEntry> findByExecutorAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndEventEntryStatus(User user, LocalDate date, EventEntryStatus status);

    List<EventEntry> findByExecutorAndEndDateLessThan(User user, LocalDate date);

}
