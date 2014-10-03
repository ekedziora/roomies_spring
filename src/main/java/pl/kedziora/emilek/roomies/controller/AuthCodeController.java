package pl.kedziora.emilek.roomies.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dell on 2014-10-02.
 */
@RestController
@RequestMapping("auth")
public class AuthCodeController {


    @RequestMapping(value = "code", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void getAuthCode(String authCode) {
        Logger.getLogger(getClass()).info("SEE RED: " + authCode);
    }

}
