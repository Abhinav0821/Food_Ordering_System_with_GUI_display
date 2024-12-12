import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Review implements Serializable {
    private Item item;
    private String review;
    private int rating;

    public Review(Item i, String review, int rating) {
        this.item = i;
        this.review = review;
        this.rating = rating;
    }

    public Review(Item item, int rating) {
        this(item, null, rating);
    }

    public Item getItem() {
        return this.item;
    }
    public String getReview() {
        return review;
    }

    public int getRating() {
        return rating;
    }
}
