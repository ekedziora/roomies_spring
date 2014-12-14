package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.AnnouncementsData;
import pl.kedziora.emilek.json.objects.params.AddAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.DeleteAnnouncementParams;

public interface AnnouncementService {
    void addAnnouncement(AddAnnouncementParams params);

    void deleteAnnouncement(DeleteAnnouncementParams params);

    AnnouncementsData getAnnouncementsData(String mail);
}
