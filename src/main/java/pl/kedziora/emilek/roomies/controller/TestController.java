package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.UserRepository;
import pl.kedziora.emilek.roomies.utils.UserUtils;

/**
 * Created by dell on 2014-09-29.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "TEST";
    }

    @RequestMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User create() {
        User user = UserUtils.generateRandomUser();
        userRepository.save(user);
        return user;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String user() {
        return "{\"name\": \"Emil Kędziora\", \"gender\": \"male\"}";
    }

}
