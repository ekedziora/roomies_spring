package pl.kedziora.emilek.roomies.service;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserTokens(String mail, String accessToken, String refreshToken) {
        User user = userRepository.findUserByMail(mail);
        user.setToken(accessToken);
        user.setRefreshToken(refreshToken);
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

}
