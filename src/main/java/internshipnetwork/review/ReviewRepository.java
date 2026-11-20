package internshipnetwork.review;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findByCompanyNameContainingIgnoreCaseOrPositionTitleContainingIgnoreCaseOrTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String companyName,
            String positionTitle,
            String title
    );
}