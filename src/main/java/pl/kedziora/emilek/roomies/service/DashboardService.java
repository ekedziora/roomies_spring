package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.DashboardData;

public interface DashboardService {
    DashboardData getDashboardData(String mail);

    void notDoneEntry(Long confirmationId, String mail);
}
