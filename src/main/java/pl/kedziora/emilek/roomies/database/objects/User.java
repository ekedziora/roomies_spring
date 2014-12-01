package pl.kedziora.emilek.roomies.database.objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import pl.kedziora.emilek.roomies.Gender;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1647040101950920287L;

	public User() {
        super();
	}

    private String name;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String pictureLink;

    @Column(nullable = false, unique = true)
	private String mail;

    private String token;

    private String refreshToken;

    @Type(type = "true_false")
    private boolean verified = false;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public boolean hasTokens() {
        return StringUtils.isNotBlank(token) && StringUtils.isNotBlank(refreshToken);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("gender", gender)
                .append("pictureLink", pictureLink)
                .append("mail", mail)
                .append("token", token)
                .append("refreshToken", refreshToken)
                .append("verified", verified)
                .append("group", group)
                .toString();
    }

}
