package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.data.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddEventData getAddEventData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        List<MemberToAddData> members = generateMembersFromUsers(group.getMembers());

        return new AddEventData(members);
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
    public void saveEvent(AddEventParams params) {
        //TODO implement
    }


}
