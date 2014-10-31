package pl.kedziora.emilek.roomies.builder;

import pl.kedziora.emilek.roomies.Gender;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.User;

public class UserBuilder {
    private String name;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String pictureLink;
    private String mail;
    private String token;
    private String refreshToken;
    private Group group;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserBuilder withPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
        return this;
    }

    public UserBuilder withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public UserBuilder withToken(String token) {
        this.token = token;
        return this;
    }

    public UserBuilder withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public UserBuilder withGroup(Group group) {
        this.group = group;
        return this;
    }

    public UserBuilder but() {
        return anUser().withName(name).withFirstName(firstName).withLastName(lastName).withGender(gender).withPictureLink(pictureLink).withMail(mail).withToken(token).withRefreshToken(refreshToken).withGroup(group);
    }

    public User build() {
        User user = new User();
        user.setName(name);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender(gender);
        user.setPictureLink(pictureLink);
        user.setMail(mail);
        user.setToken(token);
        user.setRefreshToken(refreshToken);
        user.setGroup(group);
        return user;
    }
}
