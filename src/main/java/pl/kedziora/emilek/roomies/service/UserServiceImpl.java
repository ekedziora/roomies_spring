package pl.kedziora.emilek.roomies.service;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserTokens(String mail, String accessToken, @Nullable String refreshToken) {
        User user = userRepository.findUserByMail(mail);
        user.setToken(accessToken);
        if(refreshToken != null) {
            user.setRefreshToken(refreshToken);
        }
        userRepository.save(user);
    }

    @Override
    public void saveUserAccessToken(String mail, String accessToken) {
        User user = userRepository.findUserByMail(mail);
        user.setToken(accessToken);
        userRepository.save(user);
    }

    @Override
    public User getByMail(String mail) {
        return userRepository.findUserByMail(mail);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public String getToken(String mail) {
        return userRepository.findUserByMail(mail).getToken();
    }

}
