package pl.kedziora.emilek.roomies.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziora.emilek.json.objects.data.AccountData;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.service.UserService;

@RestController
@RequestMapping("account")
public class AccountController extends BaseController {

    private static final Logger log = Logger.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "my", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AccountData getMyAccountData(@RequestBody RequestParams params) {
        preHandle(params);

        User user = userService.getUserByMail(params.getMail());

        return new AccountData(user.getName(), user.getMail(), user.getGender().getValue(), user.getPictureLink());
    }

}
