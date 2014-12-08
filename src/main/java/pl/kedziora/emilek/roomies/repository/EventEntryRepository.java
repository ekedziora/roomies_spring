package pl.kedziora.emilek.roomies.repository;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.EventEntry;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {

    List<EventEntry> findByExecutorAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndEventEntryStatus(User user, LocalDate date, LocalDate date2, EventEntryStatus status);

    List<EventEntry> findByExecutorAndStartDateGreaterThan(User user, LocalDate date, Pageable pageable);

    List<EventEntry> findByExecutor(User user);

}