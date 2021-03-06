package pl.kedziora.emilek.roomies.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.json.objects.response.GoogleErrorResponse;
import pl.kedziora.emilek.json.objects.response.TokenResponse;
import pl.kedziora.emilek.json.objects.response.UserAccountDataResponse;
import pl.kedziora.emilek.json.utils.CoreUtils;
import pl.kedziora.emilek.roomies.builder.UserBuilder;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.*;
import pl.kedziora.emilek.roomies.service.UserService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@ControllerAdvice
public abstract class BaseController {

    private static final Logger log = Logger.getLogger(BaseController.class);

    @Autowired
    protected UserService userService;

    protected void preHandle(RequestParams params) {
        if(params == null) {
            log.error("Params not existing");
            throw new BadRequestException();
        }

        if(params.getMail() == null) {
            log.error("Mail is null");
            throw new ConflictException();
        }

        if(!CoreUtils.ANDROID_APP_CLIENT_ID.equals(params.getAndroidClientId())) {
            log.error("Android client id is not matching id from request");
            throw new ForbiddenException();
        }

        User user = userService.getUserByMail(params.getMail());
        if(user == null) {
            log.info("User is null");
            User newUser = UserBuilder.anUser().withMail(params.getMail()).build();
            userService.saveUser(newUser);
            throw new UnauthorizedException();
        }
        else if(!user.hasTokens()) {
            log.info("User don't have tokens");
            throw new UnauthorizedException();
        }

        boolean isTokenValid = isTokenValid(user.getToken());
        if(!isTokenValid) {
            if(!refreshAndSaveToken(user.getRefreshToken(), user.getMail())) {
                throw new UnauthorizedException();
            }

            user = userService.getUserByMail(params.getMail());
            if(!isTokenValid(user.getToken())) {
                throw new UnauthorizedException();
            }
        }

        if(!user.isVerified()) {
            verifyAndSaveUserData(user);
        }
    }

    private boolean isTokenValid(String token) {
        String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        JsonElement jsonResponse;
        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            jsonResponse = new JsonParser().parse(reader);
        } catch (IOException e) {
            log.error("Exception validating token", e);
            return false;
        }

        GoogleErrorResponse errorResponse = new Gson().fromJson(jsonResponse, GoogleErrorResponse.class);

        if(errorResponse.getError() != null) {
            return false;
        }

        return true;
    }

    private boolean refreshAndSaveToken(String refreshToken, String mail) {
        String url = "https://www.googleapis.com/oauth2/v3/token";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("refresh_token", refreshToken));
        pairs.add(new BasicNameValuePair("client_id", CoreUtils.WEB_APP_CLIENT_ID));
        pairs.add(new BasicNameValuePair("client_secret", CoreUtils.WEB_APP_CLIENT_SECRET));
        pairs.add(new BasicNameValuePair("grant_type", "refresh_token"));

        TokenResponse tokenResponse;
        try {
            request.setEntity(new UrlEncodedFormEntity(pairs));
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            tokenResponse = new Gson().fromJson(reader, TokenResponse.class);
        } catch (IOException e) {
            log.error("Exception refreshing token", e);
            return false;
        }

        if(tokenResponse.getAccessToken() != null) {
            userService.saveUserAccessToken(mail, tokenResponse.getAccessToken());
            return true;
        }

        return false;
    }

    private void verifyAndSaveUserData(User user) {
        HttpClient client = HttpClientBuilder.create().build();

        String token = user.getToken();
        HttpGet request = new HttpGet("https://www.googleapis.com/oauth2/v2/userinfo?alt=json&access_token=" + token);

        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), "UTF-8");
            UserAccountDataResponse userAccountData = new Gson().fromJson(reader, UserAccountDataResponse.class);
            userService.saveVerifiedUserData(user, userAccountData);
        } catch (IOException e) {
            log.error("Exception during executing request", e);
            throw new InternalServerErrorException();
        }
    }

    @ResponseStatus(HttpStatus.LOCKED)
    @ExceptionHandler(StaleObjectStateException.class)
    public void handleLock() {}

}
