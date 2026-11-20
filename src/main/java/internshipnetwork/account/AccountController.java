package internshipnetwork.account;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Sign-up, log-in page and the signed-in account page. Logging in/out is handled by Spring Security. */
@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("form", new RegistrationForm());
        return "account/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegistrationForm form,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {

        boolean passwordsReady = !result.hasFieldErrors("password") && !result.hasFieldErrors("confirmPassword");
        if (passwordsReady && !form.getPassword().equals(form.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (result.hasErrors()) {
            return "account/register";
        }

        try {
            accountService.register(form.getEmail(), form.getPassword());
        } catch (EmailAlreadyRegisteredException e) {
            result.rejectValue("email", "duplicate", "An account with this email already exists");
            return "account/register";
        }

        redirectAttributes.addFlashAttribute("registered", true);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "account/login";
    }

    @GetMapping("/account")
    public String showAccount(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        return "account/account";
    }
}
