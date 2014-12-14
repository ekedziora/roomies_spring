package pl.kedziora.emilek.roomies.repository;

import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.database.objects.ExecutionConfirmation;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface ConfirmationRepository extends JpaRepository<ExecutionConfirmation, Long> {

    List<ExecutionConfirmation> findByEventEntry_ExecutorNotAndEventEntry_Parent_MembersAndEndDateTimeGreaterThanAndStatus(User executor, User member, LocalDateTime dateTime, EventEntryStatus status);

}
