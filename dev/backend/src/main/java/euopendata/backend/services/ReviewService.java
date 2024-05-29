package euopendata.backend.services;

import euopendata.backend.models.Review;
import euopendata.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;
    private final UsersService usersService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, JwtService jwtService, UsersService usersService) {
        this.reviewRepository = reviewRepository;
        this.jwtService = jwtService;
        this.usersService = usersService;
    }

    public void addReview(Review review, String token) {
        Integer user_id = Math.toIntExact(usersService.getUserByUsername(jwtService.extractUsername(token)).getId());
        review.setUserId(user_id);
        reviewRepository.save(review);

    }

    public void deleteReview(int id) {
        reviewRepository.deleteById(id);
    }
}
