package pl.kedziora.emilek.roomies.service;

import org.springframework.stereotype.Service;
import pl.kedziora.emilek.roomies.database.objects.User;

@Service
public interface UserService {

    void saveUserTokens(String mail, String accessToken, String refreshToken);

    void saveUserAccessToken(String mail, String accessToken);

    User getByMail(String mail);

    void saveUser(User user);

}
