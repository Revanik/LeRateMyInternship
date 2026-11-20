package internshipnetwork.review;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository; // Spring passes this in
    }

    @GetMapping("/reviews")
    public String list(
            @RequestParam(name = "q", defaultValue = "") String q,
            Model model) {

        String keyword = q.trim();

        var reviews = keyword.isEmpty()
                ? reviewRepository.findAllByOrderByCreatedAtDesc()
                : reviewRepository
                        .findByCompanyNameContainingIgnoreCaseOrPositionTitleContainingIgnoreCaseOrTitleContainingIgnoreCaseOrderByCreatedAtDesc(
                                keyword, keyword, keyword);

        model.addAttribute("reviews", reviews);
        model.addAttribute("q", keyword);

        return "reviews";
    }

    @GetMapping("/reviews/new")
    public String newForm(Model model) {
        model.addAttribute("review", new Review());
        return "review-form";
    }

    @PostMapping("/reviews")
    public String create(@Valid @ModelAttribute("review") Review review, BindingResult result) {
        if (result.hasErrors()) {
            return "review-form"; // show the form again with error messages
        }
        reviewRepository.save(review);
        return "redirect:/reviews";
    }
}