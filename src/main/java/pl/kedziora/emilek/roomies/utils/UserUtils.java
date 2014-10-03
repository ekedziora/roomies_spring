package pl.kedziora.emilek.roomies.utils;

import org.apache.commons.lang3.RandomStringUtils;
import pl.kedziora.emilek.roomies.database.objects.User;

public class UserUtils {

	private static final int DATABASE_MAX_SIZE = 30;
	private static final String ANY_MAIL_DOMAIN = "@example.com";

	/**
	 * Generuje losowego użytkownika z wymaganymi polami(login, hasło, mail)
	 * 
	 * @return losowy użytkownik
	 */
	public static User generateRandomUser() {
		return getRandomUserBuilder().build();
	}

	private static User.Builder getRandomUserBuilder() {
		return new User.Builder()
				.login(RandomStringUtils.randomAlphanumeric(DATABASE_MAX_SIZE))
				.password(
						RandomStringUtils.randomAlphanumeric(DATABASE_MAX_SIZE))
				.mail(RandomStringUtils.randomAlphabetic(20) + ANY_MAIL_DOMAIN);
	}

}
