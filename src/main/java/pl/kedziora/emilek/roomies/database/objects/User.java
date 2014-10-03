package pl.kedziora.emilek.roomies.database.objects;

import pl.kedziora.emilek.roomies.database.objects.base.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User extends BaseIdEntity implements Serializable {

	private static final long serialVersionUID = -1647040101950920287L;

	protected User() {

	}

	private User(Builder builder) {
		this.login = builder.login;
		this.password = builder.password;
		this.mail = builder.mail;
		this.name = builder.name;
		this.surname = builder.surname;
		this.age = builder.age;
	}

	@Column(length = 30, unique = true, nullable = false)
	private String login;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String mail;

	private String name;

	private String surname;

	private int age;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password
				+ ", mail=" + mail + ", name=" + name + ", surname=" + surname
				+ ", age=" + age + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}

	public static class Builder {
		private String login;
		private String password;
		private String mail;
		private String name;
		private String surname;
		private int age;

		public Builder login(String login) {
			this.login = login;
			return this;
		}

		public Builder password(String password) {
			this.password = password;
			return this;
		}

		public Builder mail(String mail) {
			this.mail = mail;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder surname(String surname) {
			this.surname = surname;
			return this;
		}

		public Builder age(int age) {
			this.age = age;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

}
