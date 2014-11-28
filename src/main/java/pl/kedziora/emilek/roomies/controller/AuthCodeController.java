package pl.kedziora.emilek.roomies.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.kedziora.emilek.json.objects.params.AuthCodeRequestParams;
import pl.kedziora.emilek.json.objects.response.GoogleErrorResponse;
import pl.kedziora.emilek.json.objects.response.TokenResponse;
import pl.kedziora.emilek.json.utils.CoreUtils;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.exception.InternalServerErrorException;
import pl.kedziora.emilek.roomies.exception.NotAcceptableException;
import pl.kedziora.emilek.roomies.service.UserService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("authcode")
public class AuthCodeController {

    public static final String INVALID_CODE_ERROR_MESSAGE = "Invalid code.";

    @Autowired
    private UserService userService;

    private static final Logger log = Logger.getLogger(AuthCodeController.class);

    @RequestMapping(value = "get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void getTokens(@RequestBody AuthCodeRequestParams requestParams) {
        if(requestParams.getMail() == null || requestParams.getAuthCode() == null) {
            throw new BadRequestException();
        }

        String url = "https://accounts.google.com/o/oauth2/token";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("code", requestParams.getAuthCode()));
        pairs.add(new BasicNameValuePair("client_id", CoreUtils.WEB_APP_CLIENT_ID));
        pairs.add(new BasicNameValuePair("client_secret", CoreUtils.WEB_APP_CLIENT_SECRET));
        pairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
        JsonElement jsonResponse;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = client.execute(post);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            jsonResponse = new JsonParser().parse(reader);
        } catch (UnsupportedEncodingException e) {
            log.error("Exception during pairs binding to request", e);
            throw new InternalServerErrorException();
        } catch (IOException e) {
            log.error("Exception during performing request", e);
            throw new InternalServerErrorException();
        }

        GoogleErrorResponse errorResponse = new Gson().fromJson(jsonResponse, GoogleErrorResponse.class);

        if(errorResponse.getError() != null && INVALID_CODE_ERROR_MESSAGE.equals(errorResponse.getErrorDescription())) {
            log.error("Bad request - there are already saved tokens, check request flow");
            throw new NotAcceptableException();
        }

        TokenResponse tokenResponse = new Gson().fromJson(jsonResponse, TokenResponse.class);

        if(tokenResponse.getAccessToken() == null) {
            log.error("Returned json don't have field 'access_token'");
            throw new BadRequestException();
        }

        userService.saveUserTokens(requestParams.getMail(), tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }

}
