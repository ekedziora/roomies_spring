package pl.kedziora.emilek.roomies.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

public class CalendarUtils {

    private static NetHttpTransport httpTransport = new NetHttpTransport();

    public static Calendar getCalendarService(String token) {
        GoogleCredential credential = new GoogleCredential().setAccessToken(token);
        return new Calendar.Builder(httpTransport, JacksonFactory.getDefaultInstance(), credential).build();
    }

}
