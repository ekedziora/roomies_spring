package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.EditGroupData;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.EditGroupParams;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;

import java.util.List;

public interface GroupService {
    List<JoinGroupData> getAllJoinGroupData();

    void userJoinGroup(Long groupId, String mail);

    List<MemberToAddData> getUsersAvailableToAdd(String currentUserMail);

    void createGroup(SaveGroupParams params);

    void userLeaveGroup(String mail);

    void deleteGroup(String mail);

    EditGroupData getGroupEditData(String mail);

    void editGroup(EditGroupParams params);
}
