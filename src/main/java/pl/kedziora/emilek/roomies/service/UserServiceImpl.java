package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.GroupData;
import pl.kedziora.emilek.json.objects.data.GroupMemberData;
import pl.kedziora.emilek.json.objects.response.UserAccountDataResponse;
import pl.kedziora.emilek.roomies.database.objects.Gender;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUserTokens(String mail, String accessToken, @Nullable String refreshToken) {
        User user = userRepository.findUserByMail(mail);
        Validate.notNull(user);

        user.setToken(accessToken);
        if(refreshToken != null) {
            user.setRefreshToken(refreshToken);
        }
        userRepository.save(user);
    }

    @Override
    public void saveUserAccessToken(String mail, String accessToken) {
        User user = userRepository.findUserByMail(mail);
        Validate.notNull(user);

        user.setToken(accessToken);
        userRepository.save(user);
    }

    @Override
    public User getUserByMail(String mail) {
        return userRepository.findUserByMail(mail);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveVerifiedUserData(User user, UserAccountDataResponse accountData) {
        user.setFirstName(accountData.getFirstName());
        user.setGender(Gender.fromString(accountData.getGender()));
        user.setLastName(accountData.getLastName());
        user.setName(accountData.getName());
        user.setPictureLink(accountData.getPictureLink());
        user.setVerified(true);

        userRepository.save(user);
    }

    @Override
    public GroupData getUserGroupDataByMail(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return null;
        }

        User admin = group.getAdmin();
        boolean isCurrentUserAdmin = admin.getMail().equals(mail);
        List<GroupMemberData> groupMembers = generateMembersList(group.getMembers());
        return new GroupData(group.getName(), group.getAddress(), admin.getName(), isCurrentUserAdmin, groupMembers);
    }

    private List<GroupMemberData> generateMembersList(List<User> members) {
        return Lists.newArrayList(
                Collections2.transform(members, new Function<User, GroupMemberData>() {
                    @Override
                    public GroupMemberData apply(User user) {
                        return new GroupMemberData(user.getName(), user.getPictureLink());
                    }
                }));
    }
}
