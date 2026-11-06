package internshipnetwork.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Placeholder User Interface subsystem controller. Replace with real feature controllers. */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Student Internship Network");
        return "home";
    }
}
