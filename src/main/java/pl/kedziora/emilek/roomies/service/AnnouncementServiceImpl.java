package pl.kedziora.emilek.roomies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.AnnouncementsData;
import pl.kedziora.emilek.json.objects.data.SingleAnnouncementData;
import pl.kedziora.emilek.json.objects.params.AddAnnouncementParams;
import pl.kedziora.emilek.json.objects.params.DeleteAnnouncementParams;
import pl.kedziora.emilek.roomies.annotation.Secured;
import pl.kedziora.emilek.roomies.builder.AnnouncementBuilder;
import pl.kedziora.emilek.roomies.database.objects.Announcement;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.AnnouncementRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;
import pl.kedziora.emilek.roomies.utils.CoreUtils;

import java.util.List;

@Service
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public void addAnnouncement(AddAnnouncementParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        Group group = user.getGroup();

        if(group == null) {
            throw new BadRequestException();
        }

        Announcement announcement = AnnouncementBuilder.anAnnouncement()
                .withTitle(params.getTitle())
                .withContent(params.getContent())
                .withAnonymous(params.getAnonymous())
                .withUser(user)
                .withGroup(group)
                .build();

        announcementRepository.save(announcement);
    }

    @Override
    @Secured("User delete someone's announcement")
    public void deleteAnnouncement(DeleteAnnouncementParams params) {
        User user = userRepository.findUserByMail(params.getRequestParams().getMail());
        Announcement announcement = announcementRepository.findOne(params.getAnnouncementId());

        if(!announcement.getUser().equals(user)) {
            throw new BadRequestException();
        }

        announcementRepository.delete(announcement);
    }

    @Override
    public AnnouncementsData getAnnouncementsData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return new AnnouncementsData();
        }

        List<Announcement> announcements = group.getAnnouncements();
        List<SingleAnnouncementData> announcementData = CoreUtils.generateAnnouncementData(announcements);
        return new AnnouncementsData(announcementData, user.getId());
    }

}
