package pl.kedziora.emilek.roomies.interceptor;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.kedziora.emilek.json.objects.RequestParams;
import pl.kedziora.emilek.json.utils.CoreUtils;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.database.objects.UserBuilder;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = Logger.getLogger(RequestInterceptor.class);

    private UserRepository userRepository;

    @Autowired
    public RequestInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestParams params;
        try {
            params = new Gson().fromJson(request.getReader(), RequestParams.class);
        }
        catch (JsonParseException e) {
            log.error("Exception parsing JSON", e);
            response.setStatus(400);//400-bad request
            return false;
        }

        if(params == null) {
            log.error("Params not existing");
            response.setStatus(401);
            return false;
        }

        isTokenValid(params.getAndroidClientId());

        if(!CoreUtils.ANDROID_APP_CLIENT_ID.equals(params.getAndroidClientId())) {
            log.error("Android client id is not matching id from request");
            response.setStatus(401); //403-forbidden, 401-unauthorized
            return false;
        }

        User user = userRepository.findUserByMail(params.getMail());
        if(user == null) {
            log.info("User is null");
            User newUser = UserBuilder.anUser().withMail(params.getMail()).build();
            userRepository.save(newUser);
            //get auth code
            //then get token
        }
        else if(!user.hasTokens()) {
            log.info("User don't have tokens");
            //get auth code
            //then get token
        }

        //sprawdzenie czy dobry token
        //ewentualnie refresh
        //ewentualnie auth code nowy
        //ewentualnie zwroc blad

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

    private boolean refreshToken(String refreshToken) {
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
            entity = new StringEntity(postJson.getAsString());
        } catch (UnsupportedEncodingException e) {
            log.error("Exception parsing JSON", e);
            return false;
        }
        entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setEntity(entity);

        JsonObject inputJson;
        try {
            HttpResponse response = client.execute(request);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            inputJson = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException e) {
            log.error("Exception refreshing token", e);
            return false;
        }

        if(inputJson.has("access_token")) {
            //save new token
            return true;
        }
        return false;
    }

}
