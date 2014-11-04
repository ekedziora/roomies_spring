package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
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

}
