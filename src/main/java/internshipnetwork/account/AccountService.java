package internshipnetwork.account;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Business rules for creating accounts. Controllers call this; it talks to the database. */
@Service
public class AccountService {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AppUserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a student account. The email is stored lower-cased and the password is hashed.
     *
     * @throws EmailAlreadyRegisteredException if the email is already taken
     */
    public AppUser register(String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (users.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException(normalizedEmail);
        }

        AppUser user = new AppUser(normalizedEmail, passwordEncoder.encode(rawPassword), Role.STUDENT);
        try {
            return users.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            // Two people registered the same email at the same moment; the unique index caught it.
            throw new EmailAlreadyRegisteredException(normalizedEmail);
        }
    }
}
