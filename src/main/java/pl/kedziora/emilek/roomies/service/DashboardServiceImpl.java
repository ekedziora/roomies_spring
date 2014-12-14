package pl.kedziora.emilek.roomies.service;

import com.google.api.client.util.Lists;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.ConfirmationData;
import pl.kedziora.emilek.json.objects.data.DashboardData;
import pl.kedziora.emilek.roomies.annotation.Secured;
import pl.kedziora.emilek.roomies.database.objects.EventEntryStatus;
import pl.kedziora.emilek.roomies.database.objects.ExecutionConfirmation;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.ConfirmationRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import javax.annotation.Nullable;
import java.util.List;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private PunishmentService punishmentService;

    @Override
    public DashboardData getDashboardData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return new DashboardData(null);
        }

        List<ExecutionConfirmation> userConfirmations =
                confirmationRepository.findByEventEntry_ExecutorNotAndEventEntry_Parent_MembersAndEndDateTimeGreaterThanAndStatus(user, user, new LocalDateTime(), EventEntryStatus.FINISHED);
        List<ConfirmationData> confirmations = generateConfirmationDatas(userConfirmations);

        return new DashboardData(confirmations);
    }

    private List<ConfirmationData> generateConfirmationDatas(List<ExecutionConfirmation> userConfirmations) {
        return Lists.newArrayList(
                Collections2.transform(userConfirmations, new Function<ExecutionConfirmation, ConfirmationData>() {
                    @Override
                    public ConfirmationData apply(@Nullable ExecutionConfirmation executionConfirmation) {
                        return new ConfirmationData(executionConfirmation.getId(), executionConfirmation.getEventEntry().getExecutor().getName(),
                                executionConfirmation.getEventEntry().getParent().getEventName(), executionConfirmation.getEndDateTime().toString("kk:mm:ss dd-MM-yyyy"));
                    }
                })
        );
    }

    @Override
    @Secured("Confirmation after end date or user not allowed objects")
    public void notDoneEntry(Long confirmationId, String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();
        if(group == null) {
            throw new BadRequestException();
        }

        ExecutionConfirmation confirmation = confirmationRepository.findOne(confirmationId);

        if(!confirmation.getEventEntry().getParent().getMembers().contains(user) ||
                confirmation.getEndDateTime().isBefore(new LocalDateTime())) {
            throw new BadRequestException();
        }

        confirmation.setStatus(EventEntryStatus.NOT_DONE);
        punishmentService.executePunishment(confirmation.getEventEntry());
    }

}
