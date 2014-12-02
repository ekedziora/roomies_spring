package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.Event;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByAdmin(User user);

}
