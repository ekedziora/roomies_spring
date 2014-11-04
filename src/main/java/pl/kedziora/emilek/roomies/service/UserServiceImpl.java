package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kedziora.emilek.json.objects.GroupMember;
import pl.kedziora.emilek.json.objects.MyGroupData;
import pl.kedziora.emilek.json.objects.UserAccountData;
import pl.kedziora.emilek.roomies.Gender;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.util.List;

@Service
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
    public void saveVerifiedUserData(User user, UserAccountData accountData) {
        user.setFirstName(accountData.getFirstName());
        user.setGender(Gender.fromString(accountData.getGender()));
        user.setLastName(accountData.getLastName());
        user.setName(accountData.getName());
        user.setPictureLink(accountData.getPictureLink());
        user.setVerified(true);

        userRepository.save(user);
    }

    @Override
    public MyGroupData getUserGroupDataByMail(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return null;
        }

        List<GroupMember> groupMembers = generateMembersList(group.getMembers());
        return new MyGroupData(group.getName(), group.getAddress(), group.getAdmin().getName(), groupMembers);
    }

    private List<GroupMember> generateMembersList(List<User> members) {
        return Lists.newArrayList(Collections2.transform(members, new Function<User, GroupMember>() {
            @Override
            public GroupMember apply(User user) {
                return new GroupMember(user.getName(), user.getPictureLink());
            }
        }));
    }
}
