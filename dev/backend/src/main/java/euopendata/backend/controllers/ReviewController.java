package euopendata.backend.controllers;

import euopendata.backend.models.Review;
import euopendata.backend.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/location/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("{token}")
    public void addReview(@RequestBody Review review, @PathVariable String token) {
        reviewService.addReview(review,token);
    }

    @DeleteMapping("{token}/{id}")
    public void deleteReview(@PathVariable String token,@PathVariable int id) {
        reviewService.deleteReview(token,id);
    }
}
