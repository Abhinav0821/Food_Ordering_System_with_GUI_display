import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Item implements Serializable {
    private static final long serialVersionUID = 7420193723650627658L;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private Map<Customer, Review> itemReviews = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public boolean isAvailable(int q){
        return q <= this.getQuantity();
    }
    public boolean isAvailable(){
        return this.getQuantity()>0;
    }
    public void addReview(Customer customer, Review review) {
        itemReviews.put(customer, review);
    }
    public List<Review> getTopReviews() {
        return itemReviews.values().stream()
                .limit(2)
                .collect(Collectors.toList());
    }

    public Item(String name, String category, int quant, double price){
        this.name = name;
        this.category = category;
        this.quantity = quant;
        this.price = price;
    }
    public Item(String name){
        this.name = name;
    }
}
