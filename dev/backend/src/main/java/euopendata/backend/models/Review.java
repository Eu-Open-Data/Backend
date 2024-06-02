package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "review_comment", length = Integer.MAX_VALUE)
    private String reviewComment;

    @Column(name = "rating")
    private Double rating;

    public Review() {
    }

    public Review(Hotel hotel, Integer userId, String reviewComment, Double rating) {
        this.hotel = hotel;
        this.userId = userId;
        this.reviewComment = reviewComment;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", hotel=" + hotel.getName() +
                ", userId=" + userId +
                ", reviewComment='" + reviewComment + '\'' +
                ", rating=" + rating +
                '}';
    }
}