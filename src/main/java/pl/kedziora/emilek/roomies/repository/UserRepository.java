package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByMail(String mail);

}