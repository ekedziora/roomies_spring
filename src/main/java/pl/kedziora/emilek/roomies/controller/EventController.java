package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.service.EventService;

@RestController
@RequestMapping("events")
public class EventController extends BaseController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "addData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AddEventData getAddEventData(@RequestBody RequestParams params) {
        preHandle(params);

        return eventService.getAddEventData(params.getMail());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void getAddEventData(@RequestBody AddEventParams params) {
        preHandle(params.getRequestParams());

        eventService.saveEvent(params);
    }

}
