package euopendata.backend.controllers;

import euopendata.backend.models.Review;
import euopendata.backend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/location/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<String> addReview(@RequestBody Review review, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return reviewService.addReview(review,token);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllReviews(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return reviewService.getAllReviewsByToken(token);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteReview(@RequestHeader("Authorization") String authorizationHeader, @PathVariable int id) {
        String token = authorizationHeader.replace("Bearer ", "");
        return reviewService.deleteReview(token, id);

    }
}
