package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;

@RestController
@RequestMapping("groups")
public class GroupController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void getGroupByUser(@RequestBody RequestParams params) {
        preHandle(params);

        User user = userRepository.findUserByMail(params.getMail());
        Group userGroup = user.getGroup();


    }

}
