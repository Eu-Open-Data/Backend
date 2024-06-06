package euopendata.backend.services;

import euopendata.backend.models.*;
import euopendata.backend.repositories.ReviewRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final JwtService jwtService;
    private final UsersService usersService;
    private final HotelService hotelService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, JwtService jwtService, UsersService usersService, HotelService hotelService) {
        this.reviewRepository = reviewRepository;
        this.jwtService = jwtService;
        this.usersService = usersService;
        this.hotelService = hotelService;
    }

    public ResponseEntity<?> addReview(Review review, String token) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
            review.setUserId(userId);
            Review review1 = reviewRepository.save(review);
            Hotel hotel = hotelService.getHotelById(review.getHotel().getId());
            review1.setHotel(hotel);
            Users user = usersService.getUserById(userId);
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
            ReviewDTO reviewDTO = new ReviewDTO(review1, userDTO);
            return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Review could not be added.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAllReviewsByToken(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
        List<ReviewDTO> reviews = new ArrayList<>();

        for (Review review : reviewRepository.findAllByUserId(userId)) {
            Users user = usersService.getUserByUsername(username);
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());

            reviews.add(new ReviewDTO(review, userDTO));
        }

        if (reviews.isEmpty()) {
            return new ResponseEntity<>("No reviews found for the user.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

    }
    public ResponseEntity<?> getAllReviewsByHotel(Integer hotelId) {
        List<ReviewDTO> reviews = new ArrayList<>();

        for (Review review : reviewRepository.findAllByHotelId(hotelId)) {
            Users user = usersService.getUserById(review.getUserId());
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());

            reviews.add(new ReviewDTO(review, userDTO));
        }

        if (reviews.isEmpty()) {
            return new ResponseEntity<>("No reviews found for the user.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

    }


    public ResponseEntity<String> updateReview(String token, int review_id, Review reviewDetails) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());

            Review review = reviewRepository.findById(review_id)
                    .orElseThrow(() -> new RuntimeException("No review found with id " + review_id + ". Cannot update review."));
            if (review.getUserId() != userId) {
                throw new RuntimeException("User with id " + userId + " does not have permission to update review with id " + review_id);
            }
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

            reviewRepository.save(review);

            return new ResponseEntity<>("Review added successfully.", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Review could not be updated.", HttpStatus.BAD_REQUEST);
        }
    }

    
    public ResponseEntity<String> deleteReview(String token,int id) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        Integer userId = Math.toIntExact(usersService.getUserByUsername(username).getId());
        if(!reviewRepository.existsById(id)) {
            return new ResponseEntity<>("Review id doesn't exist.", HttpStatus.NOT_FOUND);
        }

        Review review=(reviewRepository.findById(id)).get();

        if (!review.getUserId().equals(userId)) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        reviewRepository.deleteByUserIdAndId(userId,id);
        return new ResponseEntity<>("Review deleted successfully.", HttpStatus.OK);    }

    public ResponseEntity<Object> getReview(int id) {
        Optional<Review> reviewRepositoryById = reviewRepository.findById(id);



        if(reviewRepositoryById.isPresent()) {
            Users user = usersService.getUserById(reviewRepositoryById.get().getUserId());
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername());
            ReviewDTO reviewDTO = new ReviewDTO(reviewRepositoryById.get(), userDTO);
            return ResponseEntity.ok(reviewDTO);
        }
        else {
            Map<String, String> response = new HashMap<>();
            response.put("error","Review not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }
}
