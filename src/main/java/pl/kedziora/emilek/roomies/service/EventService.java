package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.json.objects.params.DeleteEventParams;
import pl.kedziora.emilek.json.objects.params.DoneEntryParams;

public interface EventService {

    AddEventData getAddEventData(String mail);

    void saveEvent(AddEventParams params);

    EventData getEventData(String mail);

    void deleteEvent(DeleteEventParams params);

    void entryDone(DoneEntryParams params);
}
