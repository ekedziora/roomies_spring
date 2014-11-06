package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;

import java.util.List;

public interface GroupService {
    List<JoinGroupData> getAllJoinGroupData();

    void joinUserToGroup(Long groupId, String mail);

    List<MemberToAddData> getUsersAvailableToAdd(Long groupId, String currentUserMail);

    void saveGroupFromRequest(SaveGroupParams params);
}
