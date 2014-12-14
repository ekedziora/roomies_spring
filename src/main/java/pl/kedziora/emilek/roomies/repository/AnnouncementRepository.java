package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
