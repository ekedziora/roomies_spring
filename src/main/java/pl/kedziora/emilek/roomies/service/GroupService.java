package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.EditGroupData;
import pl.kedziora.emilek.json.objects.data.JoinGroupData;
import pl.kedziora.emilek.json.objects.data.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.EditGroupParams;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;

import java.util.List;

public interface GroupService {
    List<JoinGroupData> getJoinGroupData();

    void userJoinGroup(Long groupId, String mail);

    List<MemberToAddData> getCreateGroupData(String currentUserMail);

    void createGroup(SaveGroupParams params);

    void userLeaveGroup(String mail);

    void deleteGroup(String mail);

    EditGroupData getGroupEditData(String mail);

    void editGroup(EditGroupParams params);
}
