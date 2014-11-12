package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kedziora.emilek.json.objects.EditGroupData;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.EditGroupParams;
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
    public void userJoinGroup(Long groupId, String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = groupRepository.findOne(groupId);

        user.setGroup(group);
        userRepository.save(user);
    }

    @Override
    public List<MemberToAddData> getUsersAvailableToAdd(String currentUserMail) {
        List<User> users;
        users = userRepository.findByGroupIsNullAndMailNot(currentUserMail);

        return generateMembersFromUsers(users);
    }

    private List<MemberToAddData> generateMembersFromUsers(List<User> users) {
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
    public void createGroup(SaveGroupParams params) {
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

    @Override
    public void userLeaveGroup(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        user.setGroup(null);
        userRepository.save(user);
        group.getMembers().remove(user);
        groupRepository.save(group);
    }

    @Override
    public void deleteGroup(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        for(User member : group.getMembers()) {
            member.setGroup(null);
            userRepository.save(member);
        }

        groupRepository.delete(group);
    }

    @Override
    public EditGroupData getGroupEditData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        List<MemberToAddData> members = generateMembersFromUsers(group.getMembers());
        List<MemberToAddData> availableMembers = generateMembersFromUsers(userRepository.findByGroupOrGroupIsNull(group));

        return new EditGroupData(group.getName(), group.getAddress(), members, availableMembers);
    }

    @Override
    public void editGroup(EditGroupParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        Group group = user.getGroup();

        group.setName(params.getName());
        group.setAddress(params.getAddress());
        group.setAdmin(userRepository.findOne(params.getAdmin().getId()));

        List<User> currentMembersList = group.getMembers();
        List<User> newMembersList = generateUsersFromMembers(params.getMembers());

        List<User> deletedMembers = Lists.newArrayList(currentMembersList);
        deletedMembers.removeAll(newMembersList);

        List<User> addedMembers = Lists.newArrayList(newMembersList);
        addedMembers.removeAll(currentMembersList);

        for(User userMember : deletedMembers) {
            userMember.setGroup(null);
            userRepository.save(userMember);
            currentMembersList.remove(userMember);
        }

        for(User userMember : addedMembers) {
            userMember.setGroup(group);
            userRepository.save(userMember);
            currentMembersList.add(userMember);
        }

        groupRepository.save(group);
    }

    private List<User> generateUsersFromMembers(final List<MemberToAddData> members) {
        return Lists.newArrayList(
                Collections2.transform(members, new Function<MemberToAddData, User>() {
                    @Override
                    public User apply(MemberToAddData memberToAddData) {
                        return userRepository.findOne(memberToAddData.getId());
                    }
                })
        );
    }
}
