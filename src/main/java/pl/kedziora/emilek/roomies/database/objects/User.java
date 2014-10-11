package pl.kedziora.emilek.roomies.database.objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1647040101950920287L;

	protected User() {
        super();
	}

    @Column(nullable = false, unique = true)
	private String mail;

    private String token;

    private String refreshToken;

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

    public boolean hasTokens() {
        return StringUtils.isNotBlank(token) && StringUtils.isNotBlank(refreshToken);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("uuid", uuid)
                .append("mail", mail)
                .append("token", token)
                .append("refreshToken", refreshToken)
                .toString();
    }

}
