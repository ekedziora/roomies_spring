package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.GroupRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<JoinGroupData> getAllJoinGroupData() {
        List<Group> allGroups = groupRepository.findAll();

        return generateGroupDataList(allGroups);
    }

    private List<JoinGroupData> generateGroupDataList(List<Group> groups) {
        return Lists.newArrayList(
                Collections2.transform(groups, new Function<Group, JoinGroupData>() {
                    @Override
                    public JoinGroupData apply(Group group) {
                        return new JoinGroupData(group.getName(), group.getAddress(), group.getAdmin().getName(), group.getId());
                    }
                })
        );
    }

    @Override
    public void joinUserToGroup(Long groupId, String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = groupRepository.findOne(groupId);

        user.setGroup(group);
        userRepository.save(user);
    }

    @Override
    public List<MemberToAddData> getUsersAvailableToAdd(Long groupId, String currentUserMail) {
        List<User> users;
        if(groupId == null) {
            users = userRepository.findByMailNot(currentUserMail);
        }
        else {
            users = userRepository.findByGroupIsNullAndMailNot(currentUserMail);
        }

        return generateMembersToAdd(users);
    }

    private List<MemberToAddData> generateMembersToAdd(List<User> users) {
        return Lists.newArrayList(
                Collections2.transform(users, new Function<User, MemberToAddData>() {
                    @Override
                    public MemberToAddData apply(User user) {
                        return new MemberToAddData(user.getName(), user.getId());
                    }
                })
        );
    }


    @Override
    public void saveGroupFromRequest(SaveGroupParams params) {
        if(StringUtils.isBlank(params.getName())) {
            throw new BadRequestException();
        }

        List<MemberToAddData> members = params.getMembers();
        List<Long> membersIds = generateMembersIds(members);

        User admin = userRepository.findUserByMail(params.getRequestParams().getMail());
        List<User> userMembers = Lists.newArrayList(userRepository.findAll(membersIds));
        userMembers.add(admin);

        Group newGroup = new Group();
        newGroup.setName(params.getName());
        newGroup.setAddress(params.getAddress());
        newGroup.setAdmin(admin);
        groupRepository.save(newGroup);

        for(User user : userMembers) {
            user.setGroup(newGroup);
            userRepository.save(user);
            newGroup.getMembers().add(user);
        }
    }

    private List<Long> generateMembersIds(List<MemberToAddData> members) {
        return Lists.newArrayList(
                Collections2.transform(members, new Function<MemberToAddData, Long>() {
                    @Override
                    public Long apply(MemberToAddData memberToAddData) {
                        return memberToAddData.getId();
                    }
                })
        );
    }

}
