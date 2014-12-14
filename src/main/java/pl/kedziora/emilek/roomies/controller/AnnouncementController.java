package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.data.AnnouncementsData;
import pl.kedziora.emilek.json.objects.params.AddAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.DeleteAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.service.AnnouncementService;

@RestController
@RequestMapping("announcements")
public class AnnouncementController extends BaseController {

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping(value = "getData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnnouncementsData getAnnouncementsData(@RequestBody RequestParams params) {
        preHandle(params);

        return announcementService.getAnnouncementsData(params.getMail());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addAnnouncement(@RequestBody AddAnnouncementParams params) {
        preHandle(params.getRequestParams());

        announcementService.addAnnouncement(params);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAnnouncement(@RequestBody DeleteAnnouncementParams params) {
        preHandle(params.getRequestParams());

        announcementService.deleteAnnouncement(params);
    }
}
