package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.data.DashboardData;
import pl.kedziora.emilek.json.objects.params.NotDoneEntryParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.service.DashboardService;

@RestController
@RequestMapping("dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping(value = "getData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public DashboardData getEventData(@RequestBody RequestParams params) {
        preHandle(params);

        return dashboardService.getDashboardData(params.getMail());
    }

    @RequestMapping(value = "notDone", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void notDoneEntry(@RequestBody NotDoneEntryParams params) {
        preHandle(params.getRequestParams());

        dashboardService.notDoneEntry(params.getConfirmationId(), params.getRequestParams().getMail());
    }

}
