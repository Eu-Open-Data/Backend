package euopendata.backend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Review review;
    private UserDTO user;

    public ReviewDTO(Review review, UserDTO user) {
        this.review = review;
        this.user = user;
    }

}
