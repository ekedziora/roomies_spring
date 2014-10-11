package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.repository.CrudRepository;
import pl.kedziora.emilek.roomies.database.objects.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByMail(String mail);

}