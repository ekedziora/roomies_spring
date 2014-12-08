package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.AddEventData;
import pl.kedziora.emilek.json.objects.data.EventData;
import pl.kedziora.emilek.json.objects.data.EventEntryData;
import pl.kedziora.emilek.json.objects.params.AddEventParams;
import pl.kedziora.emilek.json.objects.params.DeleteEventParams;
import pl.kedziora.emilek.json.objects.params.DoneEntryParams;

import java.util.List;

public interface EventService {

    AddEventData getAddEventData(String mail);

    void saveEvent(AddEventParams params);

    EventData getEventData(String mail);

    List<EventEntryData> getAllEntriesForUser(String mail);

    void deleteEvent(DeleteEventParams params);

    void entryDone(DoneEntryParams params);
}
