import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private Item inStockItem;
    private Item outOfStockItem;
    private static final String CART_FILE = "cart_temp.ser";


    @BeforeEach
    void setUp() {
        customer = new Customer("John Doe", "12345", "9876543210", "password", "Regular");

        inStockItem = new Item("Burger", "Snacks", 10, 100.0);
        outOfStockItem = new Item("Pizza", "Fast Food", 0, 200.0);
        TreeMap<String, List<Item>> menu = new TreeMap<>();
        menu.put("Snacks", Arrays.asList(inStockItem));
        menu.put("Fast Food", Arrays.asList(outOfStockItem));
        OrderSystem.setMenu(menu);
    }

    @Test
    void testAddInStockItem() {
        Map<Integer, Item> indexedItems = new HashMap<>();
        indexedItems.put(1, inStockItem);

        customer.addToCartUsingIndex(indexedItems, 1, 5);
        assertTrue(customer.getCart().containsKey(inStockItem));
        assertEquals(5, customer.getCart().get(inStockItem));
    }

    @Test
    void testAddOutOfStockItem() {
        Map<Integer, Item> indexedItems = new HashMap<>();
        indexedItems.put(2, outOfStockItem);

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> customer.addToCartUsingIndex(indexedItems, 2, 1)
        );
        assertEquals("We don't have sufficient quantity.", exception.getMessage());
        assertFalse(customer.getCart().containsKey(outOfStockItem));
    }
}
