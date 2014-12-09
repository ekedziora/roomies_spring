package pl.kedziora.emilek.roomies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.enums.PunishmentType;
import pl.kedziora.emilek.roomies.database.objects.*;
import pl.kedziora.emilek.roomies.repository.EventEntryRepository;
import pl.kedziora.emilek.roomies.repository.FinancialPunishmentRepository;

@Service
@Transactional
public class PunishmentServiceImpl implements PunishmentService {

    @Autowired
    private FinancialPunishmentRepository financialPunishmentRepository;

    @Autowired
    private EventEntryRepository eventEntryRepository;

    @Override
    public void executePunishment(EventEntry eventEntry) {
        Event event = eventEntry.getParent();

        if(PunishmentType.FINANCIAL.equals(event.getPunishmentType())) {
            Group group = eventEntry.getExecutor().getGroup();
            if(group != null) {
                FinancialPunishment financialPunishment = new FinancialPunishment();
                financialPunishment.setAmount(event.getPunishmentAmount());
                financialPunishment.setPaymentGroup(group.getPaymentGroups().first());
                financialPunishment.setUser(eventEntry.getExecutor());
                financialPunishmentRepository.save(financialPunishment);
                eventEntry.setEventEntryStatus(EventEntryStatus.FINISHED);
            }
        }
        else {
            Group group = eventEntry.getExecutor().getGroup();
            if(group != null) {

            }
        }
    }

}
