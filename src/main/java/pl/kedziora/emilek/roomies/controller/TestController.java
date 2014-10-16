package pl.kedziora.emilek.roomies.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.roomies.repository.UserRepository;

/**
 * Created by dell on 2014-09-29.
 */
@RestController
@RequestMapping("test")
public class TestController {

    private static final Logger log = Logger.getLogger(TestController.class);

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "TEST";
    }

    @RequestMapping(value = "user", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String user() {
        return "{\"name\": \"Emil Kedziora\", \"gender\": \"male\"}";
    }

    @RequestMapping(value = "request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public RequestParams auth(RequestParams params) {
        return new RequestParams("return");
    }

}
