package internshipnetwork.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Fast unit test: no Spring context and no database. */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mock
    private AppUserRepository users;

    @Test
    void registerStoresLowerCasedEmailAndHashedPassword() {
        AccountService service = new AccountService(users, encoder);
        when(users.existsByEmailIgnoreCase("student@pitt.edu")).thenReturn(false);
        when(users.saveAndFlush(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.register("  Student@Pitt.edu ", "correct-horse");

        ArgumentCaptor<AppUser> saved = ArgumentCaptor.forClass(AppUser.class);
        verify(users).saveAndFlush(saved.capture());
        assertThat(saved.getValue().getEmail()).isEqualTo("student@pitt.edu");
        assertThat(saved.getValue().getRole()).isEqualTo(Role.STUDENT);
        assertThat(saved.getValue().getPasswordHash()).isNotEqualTo("correct-horse");
        assertThat(encoder.matches("correct-horse", saved.getValue().getPasswordHash())).isTrue();
    }

    @Test
    void registerRejectsAnEmailThatIsAlreadyTaken() {
        AccountService service = new AccountService(users, encoder);
        when(users.existsByEmailIgnoreCase("taken@pitt.edu")).thenReturn(true);

        assertThatThrownBy(() -> service.register("taken@pitt.edu", "correct-horse"))
                .isInstanceOf(EmailAlreadyRegisteredException.class);

        verify(users, never()).saveAndFlush(any(AppUser.class));
    }
}
