package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.JoinGroupData;
import pl.kedziora.emilek.json.objects.MemberToAddData;
import pl.kedziora.emilek.json.objects.MyGroupData;
import pl.kedziora.emilek.json.objects.params.JoinGroupParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.json.objects.params.SaveGroupParams;
import pl.kedziora.emilek.json.objects.params.UsersToAddParams;
import pl.kedziora.emilek.roomies.service.GroupService;
import pl.kedziora.emilek.roomies.service.UserService;

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
    public MyGroupData getGroupByUser(@RequestBody RequestParams params) {
        preHandle(params);

        return userService.getUserGroupDataByMail(params.getMail());
    }


    @RequestMapping(value = "join", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<JoinGroupData> getGroupsToJoin(@RequestBody RequestParams params) {
        preHandle(params);

        return groupService.getAllJoinGroupData();
    }

    @RequestMapping(value = "userJoin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void joinUserToGroup(@RequestBody JoinGroupParams params) {
        preHandle(params.getRequestParams());

        groupService.joinUserToGroup(params.getGroupId(), params.getRequestParams().getMail());
    }

    @RequestMapping(value = "usersToAdd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<MemberToAddData> joinUserToGroup(@RequestBody UsersToAddParams params) {
        preHandle(params.getRequestParams());

        return groupService.getUsersAvailableToAdd(params.getGroupId(), params.getRequestParams().getMail());
    }

    @RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void saveGroup(@RequestBody SaveGroupParams params) {
        preHandle(params.getRequestParams());

        groupService.saveGroupFromRequest(params);
    }

}
