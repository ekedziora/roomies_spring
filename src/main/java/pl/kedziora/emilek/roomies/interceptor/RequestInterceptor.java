package pl.kedziora.emilek.roomies.interceptor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.database.objects.UserBuilder;
import pl.kedziora.emilek.json.utils.CoreUtils;
import pl.kedziora.emilek.roomies.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = Logger.getLogger(RequestInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestParams params;
        try {
            params = new Gson().fromJson(request.getReader(), RequestParams.class);
        }
        catch (JsonParseException e) {
            log.error("Exception parsing JSON", e);
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            return false;
        }

        if(params == null) {
            log.error("Params not existing");
            response.setStatus(org.apache.http.HttpStatus.SC_BAD_REQUEST);
            return false;
        }

        if(!CoreUtils.ANDROID_APP_CLIENT_ID.equals(params.getAndroidClientId())) {
            log.error("Android client id is not matching id from request");
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return false;
        }

        User user = userService.getByMail(params.getMail());
        if(user == null) {
            log.info("User is null");
            User newUser = UserBuilder.anUser().withMail(params.getMail()).build();
            userService.saveUser(newUser);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }
        else if(!user.hasTokens()) {
            log.info("User don't have tokens");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return false;
        }

        boolean isTokenValid = isTokenValid(user.getToken());
        if(!isTokenValid) {
            if(!refreshToken(user.getRefreshToken(), params.getMail())) {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                return false;
            }
        }

        return true;
    }

    private boolean isTokenValid(String token) {
        String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        JsonObject jsonObject;
        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            jsonObject = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            log.error("Exception validating token", e);
            return false;
        }

        if(jsonObject.has("error")) {
            return false;
        }
        return true;
    }

    private boolean refreshToken(String refreshToken, String mail) {
        String url = "https://accounts.google.com/o/oauth2/token";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        JsonObject postJson = new JsonObject();
        postJson.addProperty("refresh_token", refreshToken);
        postJson.addProperty("client_id", CoreUtils.WEB_APP_CLIENT_ID);
        postJson.addProperty("client_secret", CoreUtils.WEB_APP_CLIENT_SECRET);
        postJson.addProperty("grant_type", "refresh_token");

        StringEntity entity;
        try {
            entity = new StringEntity(postJson.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Exception parsing JSON", e);
            return false;
        }
        entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setEntity(entity);

        JsonObject jsonResponse;
        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            jsonResponse = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            log.error("Exception refreshing token", e);
            return false;
        }

        if(jsonResponse.has("access_token")) {
            userService.saveUserAccessToken(mail, jsonResponse.get("access_token").getAsString());
            return true;
        }

        return false;
    }

}
