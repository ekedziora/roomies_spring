package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.JoinGroupData;

import java.util.List;

public interface GroupService {
    List<JoinGroupData> getAllJoinGroupData();

    void joinUserToGroup(Long groupId, String mail);
}
