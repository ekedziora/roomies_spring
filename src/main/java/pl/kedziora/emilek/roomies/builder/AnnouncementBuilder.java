package pl.kedziora.emilek.roomies.builder;

import pl.kedziora.emilek.roomies.database.objects.Announcement;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;

public class AnnouncementBuilder {
    private String title;
    private String content;
    private Boolean anonymous;
    private User user;
    private Group group;

    private AnnouncementBuilder() {
    }

    public static AnnouncementBuilder anAnnouncement() {
        return new AnnouncementBuilder();
    }

    public AnnouncementBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public AnnouncementBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public AnnouncementBuilder withAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
        return this;
    }

    public AnnouncementBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public AnnouncementBuilder withGroup(Group group) {
        this.group = group;
        return this;
    }

    public AnnouncementBuilder but() {
        return anAnnouncement().withTitle(title).withContent(content).withAnonymous(anonymous).withUser(user).withGroup(group);
    }

    public Announcement build() {
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setAnonymous(anonymous);
        announcement.setUser(user);
        announcement.setGroup(group);
        return announcement;
    }
}
