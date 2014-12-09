package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.roomies.database.objects.EventEntry;

public interface PunishmentService {

    void executePunishment(EventEntry eventEntry);
}
