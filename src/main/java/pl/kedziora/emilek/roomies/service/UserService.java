package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.GroupData;
import pl.kedziora.emilek.json.objects.response.UserAccountDataResponse;
import pl.kedziora.emilek.roomies.database.objects.User;

public interface UserService {

    void saveUserTokens(String mail, String accessToken, String refreshToken);

    void saveUserAccessToken(String mail, String accessToken);

    User getUserByMail(String mail);

    void saveUser(User user);

    void saveVerifiedUserData(User user, UserAccountDataResponse accountData);

    GroupData getUserGroupDataByMail(String mail);
}
