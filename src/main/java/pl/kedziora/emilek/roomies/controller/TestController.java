package pl.kedziora.emilek.roomies.controller;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.objects.UserAccountData;
import pl.kedziora.emilek.roomies.exception.InternalServerErrorException;
import pl.kedziora.emilek.roomies.service.UserService;

import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("test")
public class TestController extends BaseController {

    private static final Logger log = Logger.getLogger(TestController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "test", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "TEST";
    }

    @RequestMapping(value = "request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserAccountData auth(@RequestBody RequestParams params) {
        preHandle(params);

        HttpClient client = HttpClientBuilder.create().build();

        String token = userService.getToken(params.getMail());
        HttpGet request = new HttpGet("https://www.googleapis.com/oauth2/v2/userinfo?alt=json&access_token=" + token);

        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
            UserAccountData userAccountData = new Gson().fromJson(reader, UserAccountData.class);
            return userAccountData;
        } catch (IOException e) {
            log.error("Exception during executing request", e);
            throw new InternalServerErrorException();
        }
    }

}
