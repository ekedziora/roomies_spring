package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

}
