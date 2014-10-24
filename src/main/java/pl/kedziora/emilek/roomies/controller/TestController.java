package pl.kedziora.emilek.roomies.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.objects.TokenResponse;
import pl.kedziora.emilek.roomies.repository.UserRepository;
import pl.kedziora.emilek.roomies.service.UserService;

import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("test")
public class TestController {

    private static final Logger log = Logger.getLogger(TestController.class);

    @Autowired
    private UserService userService;

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
    public ResponseEntity auth(@RequestBody RequestParams params) {
        HttpClient client = HttpClientBuilder.create().build();

        String token = userService.getToken(params.getMail());
        HttpGet request = new HttpGet("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + token);

        JsonObject jsonResponse;
        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            log.error("Exception during executing request", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        jsonResponse.add("test", jsonResponse);
        return new ResponseEntity(HttpStatus.OK);
    }

}
