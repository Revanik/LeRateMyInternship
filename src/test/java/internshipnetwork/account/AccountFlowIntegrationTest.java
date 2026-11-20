package internshipnetwork.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * Registers and logs in through the real app and PostgreSQL (so Postgres must be running).
 * Each test is rolled back afterwards, so it leaves no accounts behind.
 */
@SpringBootTest
@Transactional
class AccountFlowIntegrationTest {

    private static final String EMAIL = "flow-test@pitt.edu";
    private static final String PASSWORD = "correct-horse";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppUserRepository users;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void registerPageLoads() throws Exception {
        mvc.perform(get("/register"))
           .andExpect(status().isOk())
           .andExpect(view().name("account/register"));
    }

    @Test
    void registeringCreatesAnAccountAndSendsYouToLogin() throws Exception {
        mvc.perform(post("/register").with(csrf())
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("confirmPassword", PASSWORD))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login"));

        assertThat(users.existsByEmailIgnoreCase(EMAIL)).isTrue();
    }

    @Test
    void registeringWithMismatchedPasswordsShowsAnError() throws Exception {
        mvc.perform(post("/register").with(csrf())
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("confirmPassword", "something-else"))
           .andExpect(status().isOk())
           .andExpect(model().attributeHasFieldErrors("form", "confirmPassword"));

        assertThat(users.existsByEmailIgnoreCase(EMAIL)).isFalse();
    }

    @Test
    void registeringTheSameEmailTwiceShowsAnError() throws Exception {
        accountService.register(EMAIL, PASSWORD);

        mvc.perform(post("/register").with(csrf())
                .param("email", EMAIL.toUpperCase())
                .param("password", PASSWORD)
                .param("confirmPassword", PASSWORD))
           .andExpect(status().isOk())
           .andExpect(model().attributeHasFieldErrors("form", "email"));
    }

    @Test
    void loggingInWithCorrectCredentialsSignsYouIn() throws Exception {
        accountService.register(EMAIL, PASSWORD);

        mvc.perform(formLogin("/login").user(EMAIL).password(PASSWORD))
           .andExpect(authenticated().withUsername(EMAIL))
           .andExpect(redirectedUrl("/account"));
    }

    @Test
    void loggingInWithAWrongPasswordSendsYouBackToLogin() throws Exception {
        accountService.register(EMAIL, PASSWORD);

        mvc.perform(formLogin("/login").user(EMAIL).password("wrong-password"))
           .andExpect(unauthenticated())
           .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void accountPageRequiresLogin() throws Exception {
        mvc.perform(get("/account"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/login"));
    }

    @Test
    void accountPageShowsTheSignedInEmail() throws Exception {
        mvc.perform(get("/account").with(user(EMAIL)))
           .andExpect(status().isOk())
           .andExpect(view().name("account/account"))
           .andExpect(content().string(org.hamcrest.Matchers.containsString(EMAIL)));
    }
}
