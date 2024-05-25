package euopendata.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @Column(name = "hotel_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rating", length = 50)
    private String rating;

    @Column(name = "address")
    private String address;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "rating_location")
    private Double ratingLocation;

    @Column(name = "rating_sleep")
    private Double ratingSleep;

    @Column(name = "rating_rooms")
    private Double ratingRooms;

    @Column(name = "rating_service")
    private Double ratingService;

    @Column(name = "rating_value")
    private Double ratingValue;

    @Column(name = "rating_cleanliness")
    private Double ratingCleanliness;

    @Column(name = "tripadvisor_price_level")
    private Integer tripadvisorPriceLevel;

    @ManyToMany
    @JoinTable(name = "hotels_amenities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenities = new LinkedHashSet<>();

    public Hotel() {
    }

    public Hotel(String name, String rating, String address, String description, String websiteUrl, BigDecimal latitude, BigDecimal longitude, City city, Double ratingLocation, Double ratingSleep, Double ratingRooms, Double ratingService, Double ratingValue, Double ratingCleanliness, Integer tripadvisorPriceLevel, Set<Amenity> amenities) {
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.description = description;
        this.websiteUrl = websiteUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.ratingLocation = ratingLocation;
        this.ratingSleep = ratingSleep;
        this.ratingRooms = ratingRooms;
        this.ratingService = ratingService;
        this.ratingValue = ratingValue;
        this.ratingCleanliness = ratingCleanliness;
        this.tripadvisorPriceLevel = tripadvisorPriceLevel;
        this.amenities = amenities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Double getRatingLocation() {
        return ratingLocation;
    }

    public void setRatingLocation(Double ratingLocation) {
        this.ratingLocation = ratingLocation;
    }

    public Double getRatingSleep() {
        return ratingSleep;
    }

    public void setRatingSleep(Double ratingSleep) {
        this.ratingSleep = ratingSleep;
    }

    public Double getRatingService() {
        return ratingService;
    }

    public void setRatingService(Double ratingService) {
        this.ratingService = ratingService;
    }

    public Double getRatingRooms() {
        return ratingRooms;
    }

    public void setRatingRooms(Double ratingRooms) {
        this.ratingRooms = ratingRooms;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Double getRatingCleanliness() {
        return ratingCleanliness;
    }

    public void setRatingCleanliness(Double ratingCleanliness) {
        this.ratingCleanliness = ratingCleanliness;
    }

    public Integer getTripadvisorPriceLevel() {
        return tripadvisorPriceLevel;
    }

    public void setTripadvisorPriceLevel(Integer tripadvisorPriceLevel) {
        this.tripadvisorPriceLevel = tripadvisorPriceLevel;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating='" + rating + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", city=" + city +
                ", ratingLocation=" + ratingLocation +
                ", ratingSleep=" + ratingSleep +
                ", ratingRooms=" + ratingRooms +
                ", ratingService=" + ratingService +
                ", ratingValue=" + ratingValue +
                ", ratingCleanliness=" + ratingCleanliness +
                ", tripadvisorPriceLevel=" + tripadvisorPriceLevel +
                ", amenities=" + amenities +
                '}';
    }
}