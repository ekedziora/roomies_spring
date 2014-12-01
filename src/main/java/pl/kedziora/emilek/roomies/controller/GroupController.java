package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.data.EditGroupData;
import pl.kedziora.emilek.json.objects.data.GroupData;
import pl.kedziora.emilek.json.objects.data.JoinGroupData;
import pl.kedziora.emilek.json.objects.data.MemberToAddData;
import pl.kedziora.emilek.json.objects.params.EditGroupParams;
import pl.kedziora.emilek.json.objects.params.JoinGroupParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;
import pl.kedziora.emilek.roomies.service.GroupService;
import pl.kedziora.emilek.roomies.service.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("groups")
public class GroupController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GroupData getGroupByUser(@RequestBody RequestParams params) {
        preHandle(params);

        return userService.getUserGroupDataByMail(params.getMail());
    }


    @RequestMapping(value = "join", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<JoinGroupData> getGroupsToJoin(@RequestBody RequestParams params) {
        preHandle(params);

        return groupService.getJoinGroupData();
    }

    @RequestMapping(value = "userJoin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void userJoinGroup(@RequestBody JoinGroupParams params) {
        preHandle(params.getRequestParams());

        groupService.userJoinGroup(params.getGroupId(), params.getRequestParams().getMail());
    }

    @RequestMapping(value = "usersToAdd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MemberToAddData> getAvailableUsersToAdd(@RequestBody RequestParams params) {
        preHandle(params);

        return groupService.getCreateGroupData(params.getMail());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void saveGroup(@RequestBody SaveGroupParams params) throws IOException {
        preHandle(params.getRequestParams());

        groupService.createGroup(params);
    }

    @RequestMapping(value = "leave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void leaveGroup(@RequestBody RequestParams params) {
        preHandle(params);

        groupService.userLeaveGroup(params.getMail());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(@RequestBody RequestParams params) throws IOException {
        preHandle(params);

        groupService.deleteGroup(params.getMail());
    }

    @RequestMapping(value = "editData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public EditGroupData getEditGroupData(@RequestBody RequestParams params) {
        preHandle(params);

        return groupService.getGroupEditData(params.getMail());
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void editGroup(@RequestBody EditGroupParams params) throws IOException {
        preHandle(params.getRequestParams());

        groupService.editGroup(params);
    }

}
