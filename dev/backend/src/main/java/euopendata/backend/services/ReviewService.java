package euopendata.backend.services;

import euopendata.backend.models.Review;
import euopendata.backend.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public ResponseEntity<String> addReview(Review review, String token) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
            review.setUserId(userId);
            reviewRepository.save(review);
            return new ResponseEntity<>("Review added successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Review could not be added.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllReviewsByToken(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
        List<Review> reviews = reviewRepository.findAllByUserId(userId);

        if (reviews.isEmpty()) {
            return new ResponseEntity<>("No reviews found for the user.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
    }
    
    public ResponseEntity<String> deleteReview(String token,int id) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
        if(!reviewRepository.existsById(id)) {
            return new ResponseEntity<>("Review id doesn't exist.", HttpStatus.NOT_FOUND);
        }

        reviewRepository.deleteByUserIdAndId(userId,id);
        return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);    }
}
