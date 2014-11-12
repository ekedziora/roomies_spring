package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByMail(String mail);

    List<User> findByGroupIsNullAndMailNot(String mail);

    List<User> findByGroupOrGroupIsNull(Group group);

}