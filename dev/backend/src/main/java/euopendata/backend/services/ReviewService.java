package euopendata.backend.services;

import euopendata.backend.models.Review;
import euopendata.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rds.model.ResourceNotFoundException;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview(Review review) {
        reviewRepository.save(review);
    }

    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }

    public void updateReview(int id, Review reviewDetails) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No review found with id " + id + ". Cannot update review."));

        if (reviewDetails.getReviewComment() != null) {
            review.setReviewComment(reviewDetails.getReviewComment());
        }
        if (reviewDetails.getRating() != null) {
            review.setRating(reviewDetails.getRating());
        }
        if (reviewDetails.getHotel() != null) {
            review.setHotel(reviewDetails.getHotel());
        }
        if (reviewDetails.getUserId() != null) {
            review.setUserId(reviewDetails.getUserId());
        }
    }
}
