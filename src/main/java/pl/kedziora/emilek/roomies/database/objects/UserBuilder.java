package pl.kedziora.emilek.roomies.database.objects;

public class UserBuilder {
    private String mail;
    private String token;
    private String refreshToken;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
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

    public UserBuilder but() {
        return anUser().withMail(mail).withToken(token).withRefreshToken(refreshToken);
    }

    public User build() {
        User user = new User();
        user.setMail(mail);
        user.setToken(token);
        user.setRefreshToken(refreshToken);
        return user;
    }
}
