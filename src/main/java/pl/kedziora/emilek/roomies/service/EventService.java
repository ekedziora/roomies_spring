package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.params.AddEventParams;

public interface EventService {

    AddEventData getAddEventData(String mail);

    void saveEvent(AddEventParams params);
}
