import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Customer implements Serializable{
    private static final long serialVersionUID = 7420193723650627658L;
    private String name;
    private String rollnum;
    private String mob_no;
    private String password;
    private static final String CART_FILE = "cart_temp.ser";

    public Customer(String name, String r, String m, String pwd, String status){
        this.name = name;
        this.rollnum = r;
        this.mob_no = m;
        this.password = pwd;
        this.status = status;
    }
    public String getPassword(){
        return this.password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollnum() {
        return rollnum;
    }

    public void setRollnum(String rollnum) {
        this.rollnum = rollnum;
    }

    public String getContact() {
        return mob_no;
    }

    public void setContact(String m) {
        this.mob_no = m;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<LocalDateTime, Order> getOrderHistory() {
        return OrderHistory;
    }

    public void setOrderHistory(Map<LocalDateTime, Order> orderHistory) {
        OrderHistory = orderHistory;
    }

    public HashMap<Item, Integer> getCart() {
        return Cart;
    }

    public void setCart(HashMap<Item, Integer> cart) {
        Cart = cart;
    }
    public double calculateBill(){
        double p = 0;
        for(Item k : Cart.keySet()){
            p+=(Cart.get(k)*k.getPrice());
        }
        return p;
    }
    private String status;
    private Map<LocalDateTime, Order> OrderHistory= new HashMap<>();
    private HashMap<Item, Integer> Cart = new HashMap<>();
    private Map<Item, List<Review>> itemReviews = new HashMap<>();  // Store reviews for each item

    private void viewOrderHistory(){
        Scanner scan_cvoh = new Scanner(System.in);
        System.out.println();
        if(OrderHistory.size() == 0){
            System.out.println("You have no prior orders with us.");
            System.out.println("Press Enter to go back to Previous Menu");
            scan_cvoh.nextLine();
            displayUserMenu();
        }else{
            //iterate over OrderHistory and print Order date, Items, Total Price
        }
    }
    private void displayAllItems(TreeMap<String, List<Item>> menu) {
        for (Map.Entry<String, List<Item>> entry : menu.entrySet()) {
            String category = entry.getKey();
            List<Item> items = entry.getValue();
            System.out.println("\nCategory: " + category);
            for (Item item : items) {
                System.out.println(" - " + item);
            }
        }
    }
    public void viewMenu() {
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Full Menu:");
        OrderSystem.displayFullMenu();
        System.out.println("Would you like to search for items by keyword/prefix? (yes/no)");
        String ks = scanner.nextLine();
        if(ks.equals("yes")){
            Item i;
            Scanner scan_auid5 = new Scanner(System.in);
            System.out.println("Enter keyword(prefix of item name) of the item: ");
            String k = scan_auid5.nextLine();
            System.out.println("List of items related to the keyword: ");
            for (Item a : OrderSystem.searchItemByPrefix(k)) {
                int b = 1;
                System.out.println(b + ". " + a.getName() + " " + a.getPrice() + " " + a.getCategory() + " " + a.getQuantity());
                b++;
            }
            System.out.println("Enter the index of the item from above list: ");
            int z = scan_auid5.nextInt();
            i = OrderSystem.searchItemByPrefix(k).get(z-1);
            System.out.println("Item: "+i.getName()+" "+i.getPrice()+" Top Reviews:"+i.getTopReviews());
        }
        System.out.println("Would you like to sort items by price? (yes/no)");
        String sortChoice = scanner.nextLine();
        if(sortChoice.equalsIgnoreCase("yes")) {
            boolean sortByPrice = sortChoice.equalsIgnoreCase("yes");
            Scanner scan1 = new Scanner(System.in);
            showMenu(OrderSystem.getMenu(), sortByPrice, scan1);
            System.out.println("\nWould you like to filter by category? (yes/no)");
            String filterChoice = scanner.nextLine();
            if (filterChoice.equalsIgnoreCase("yes")) {
                showCategories();
                System.out.println("Enter index of the category:");
                int i = scanner.nextInt();
                String category = OrderSystem.findCategoryByIndex(i);

                List<Item> filteredItems = OrderSystem.filteredMenu(category);
                if (filteredItems.isEmpty()) {
                    System.out.println("No items found in this category.");
                } else {
                    Map<String, List<Item>> filteredMenu = Map.of(category, filteredItems);
                    showMenu(filteredMenu, sortByPrice, scanner);
                }
            }
        }else{
            System.out.println("\nWould you like to filter by category? (yes/no)");
            String filterChoice = scanner.nextLine();
            if (filterChoice.equalsIgnoreCase("yes")) {
                showCategories();
                System.out.println("Enter index of the category:");
                int x = scanner.nextInt();
                if(!OrderSystem.findCategoryByIndex(x).equals("")) {
                    String category = OrderSystem.findCategoryByIndex(x);
                    List<Item> filteredItems = OrderSystem.filteredMenu(category);
                    if (filteredItems.isEmpty()) {
                        System.out.println("No items found in this category.");
                    } else {
                        for(Item i :filteredItems){
                            System.out.println(" - " + i.getName() + " | Price: " + i.getPrice() + " | Available: " + i.isAvailable());
                        }
                    }
                }else{
                    System.out.println("Invalid Category. Redirecting...");
                    viewMenu();
                }
            }
        }
        System.out.println("Press Enter to go back to Previous Menu.");
        scanner.nextLine();
        displayUserMenu();
    }
    private void showCategories(){
        System.out.println();
        System.out.println("Categories ");
        int i=1;
        for(String k: OrderSystem.getMenu().keySet()){
            System.out.println((i)+". "+k);
        }
    }
    private void showMenu(Map<String, List<Item>> menu, boolean sortByPrice, Scanner scanner) {
        System.out.println("Press 1 to sort: Lowest to Highest");
        System.out.println("Press 2 to sort: Highest to Lowest");
        String choice = "";
        int op = scanner.nextInt();
        if(op == 1){choice = "ascending";}
        else if(op == 1){choice = "descending";}
        else{viewMenu();}
        System.out.println("Our Menu");
        for (Map.Entry<String, List<Item>> entry : menu.entrySet()) {
            System.out.println("Category: " + entry.getKey());

            List<Item> items = entry.getValue();
            if (sortByPrice && choice.equalsIgnoreCase("descending")) {
                items.sort(Comparator.comparingDouble(Item::getPrice));
            }else if(sortByPrice && choice.equalsIgnoreCase("ascending")){
                items.sort(Comparator.comparingDouble(Item::getPrice).reversed());
            }

            for (Item item : items) {
                System.out.println(" - " + item.getName() + " | Price: " + item.getPrice() + " | Available: " + item.isAvailable());

                List<Review> reviews = item.getTopReviews();
                if (reviews.isEmpty()) {
                    System.out.println("   No reviews available.");
                } else {
                    System.out.println("   Reviews:");
                    int counter = 1;
                    for (Review review : reviews) {
                        System.out.print("    - Customer " + counter + " | Rating: " + review.getRating());
                        if (review.getReview() != null && !review.getReview().isEmpty()) {
                            System.out.print(" | Review: " + review.getReview());
                        }
                        System.out.println();
                        counter++;
                    }
                }
            }
        }
    }
    public void placeOrder(){
        Scanner scan_cpo = new Scanner(System.in);
        System.out.println();
        System.out.println("Items in Cart: ");
        System.out.println();
        Cart.forEach((k,v) -> {
            System.out.println(k.getName() +" "+v+" "+k.getPrice()*v);});
        String da = "";
        if(!OrderHistory.isEmpty()) {
            System.out.println("Press 1 to enter new delivery address.");
            System.out.println("Press 2 to use the same address as previous order.");
            int choice = scan_cpo.nextInt();
            switch(choice){
                case 1:
                    Scanner scan_cpo2 = new Scanner(System.in);
                    System.out.println("Enter delivery address: ");
                    da = scan_cpo2.nextLine();
                    break;
                case 2:
                    LocalDateTime latestTime = Collections.max(OrderHistory.keySet());
                    Order lastOrder = OrderHistory.get(latestTime);
                    da = lastOrder.getDeliveryAddress(); // Assuming getDeliveryAddress() exists in Order class
                    System.out.println("Using previous delivery address: " + da);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    placeOrder();
                    break;
            }
        }else{
            Scanner scan_cpo1 = new Scanner(System.in);
            System.out.println("Enter delivery address: ");
            da = scan_cpo1.nextLine();
        }
        System.out.println("Do you wish to add a special request for the order? (y/n)");
        Scanner scan_cpo6 = new Scanner(System.in);
        String sr = "";
        String c1 =scan_cpo6.nextLine();
        if(c1.equals("y")){
            Scanner scan_cpo4 = new Scanner(System.in);
            System.out.println("Enter your request: ");
            sr = scan_cpo4.nextLine();
        }

        String s = "";
        String d = "";
        System.out.println("Choose the mode of Payment: ");
        System.out.println("1 Cash/UPI on Delivery");
        System.out.println("2 Pay now with UPI");
        System.out.println("3 Credit or Debit Card");
        Payment p;
        Scanner scan_cpo5 = new Scanner(System.in);
        int c = scan_cpo5.nextInt();
        switch(c){
            case 1:
                s = "Cash/UPI on Delivery";
                break;
            case 2:
                Scanner scan_cpo2 = new Scanner(System.in);
                s = "Pay now with UPI";
                System.out.println("Enter your UPI id: ");
                d = scan_cpo2.nextLine();
                break;
            case 3:
                Scanner scan_cpo3 = new Scanner(System.in);
                s = "Credit or Debit Card";
                System.out.println("Enter you Card Number <space> Expiry Date <space> CVV : ");
                d = scan_cpo3.nextLine();
        }
        Scanner scan_cpo1 = new Scanner(System.in);
        System.out.println("Are you sure you want to order? (y/n)");
        String choice1 = scan_cpo1.nextLine();
        switch(choice1) {
            case "y":
                p = new Payment(s);
                LocalDateTime t = LocalDateTime.now();
                Order o;
                HashMap<Item, Integer> itemList = new HashMap<>();
                for(Item i : this.getCart().keySet()){
                    itemList.put(i,this.getCart().get(i));
                }
                o = new Order(this, t, "Pending",da, this.getStatus(),this.calculateBill(), itemList, p, sr);
                o.setPaymentDetail(d);
                for (Item i : this.getCart().keySet()) {
                    i.setQuantity(i.getQuantity() - this.getCart().get(i));
                }
                System.out.println(o.getItemlist().size());
                this.getOrderHistory().put(t, o);
                OrderSystem.getPendingOrders().add(o);
                OrderSystem.getAllOrders().add(o);
                System.out.println("Your Order has been placed. Thank you.");
                System.out.println("Press Enter to return to Previous Menu.");
                Cart.clear();
                System.out.println(o.getItemlist().size());
                Scanner scan1 = new Scanner(System.in);
                scan1.nextLine();
                displayUserMenu();
                break;
            case "n":
                System.out.println("Redirecting to Previous Menu");
                break;
        }

    }
    public void addToCart() {
        System.out.println();
        Scanner scan_catc = new Scanner(System.in);
        System.out.println();
        Item selectedItem;
        int index = 1;

        // Display menu with index numbers
        System.out.println("Menu:");
        Map<Integer, Item> indexedItems = new HashMap<>();
        for (Map.Entry<String, List<Item>> categoryEntry : OrderSystem.getMenu().entrySet()) {
            System.out.println("Category: " + categoryEntry.getKey());
            for (Item item : categoryEntry.getValue()) {
                System.out.println(index + ". " + item.getName() + " - Price: " + item.getPrice());
                indexedItems.put(index, item);
                index++;
            }
        }

        System.out.println("\nPress 1 to add using index number.");
        System.out.println("Press 2 to add using item name.");
        System.out.println("Press 3 to change quantity in cart.");
        System.out.println("Press 4 to remove item(s) from cart.");
        System.out.println("Press 5 to return to Previous Menu.");
        int choice = scan_catc.nextInt();
        scan_catc.nextLine();

        switch (choice) {
            case 1:
                System.out.println("Enter index of the item: ");
                int i = scan_catc.nextInt();
                selectedItem = indexedItems.get(i);
                if (selectedItem != null) {
                    System.out.println("Enter quantity of the item: ");
                    int quantity = scan_catc.nextInt();
                    if(quantity <= selectedItem.getQuantity()) {
                        Cart.put(selectedItem, quantity);
                        System.out.println(Cart.size());
                        System.out.println("Item added to Cart. Redirecting to Previous Menu...");
                        saveCartToFile();
                        System.out.println(Cart.size());
                    }else{
                        System.out.println("We don't have sufficient quantity. Redirecting to Previous Menu...");
                    }
                } else {
                    System.out.println("Invalid index. Returning to Previous Menu...");
                }
                break;

            case 2:
                System.out.println("Enter name of the item: ");
                String itemName = scan_catc.nextLine();
                selectedItem = OrderSystem.searchItemByName(itemName);
                if (selectedItem != null) {
                    System.out.println("Enter quantity of the item: ");
                    int quantity = scan_catc.nextInt();
                    Cart.put(selectedItem, quantity);
                    System.out.println("Item added to Cart. Redirecting to Previous Menu...");
                    saveCartToFile();
                } else {
                    System.out.println("Invalid name. Redirecting to Previous Menu...");
                }
                break;
            case 5:
                displayUserMenu();
                break;
            case 3:
                modifyItemQuantity();
                break;
            case 4:
                removeItemFromCart();
                break;
            default:
                System.out.println("Invalid choice. Returning to Previous Menu...");
        }
        saveCartToFile();
        Scanner scan_catc1 = new Scanner(System.in);
        System.out.println("Press Enter to return to Previous Menu");
        scan_catc1.nextLine();
        displayUserMenu();
    }
    // Helper method for testing indexed item addition
    public void addToCartUsingIndex(Map<Integer, Item> indexedItems, int index, int quantity) {
        Item selectedItem = indexedItems.get(index);
        if (selectedItem == null) {
            throw new IllegalArgumentException("Invalid index.");
        }

        if (quantity > selectedItem.getQuantity()) {
            throw new IllegalArgumentException("We don't have sufficient quantity.");
        }

        Cart.put(selectedItem, Cart.getOrDefault(selectedItem, 0) + quantity);
    }

    public void modifyItemQuantity() {
        System.out.println();
        if (Cart.isEmpty()) {
            System.out.println("Your cart is currently empty.");
            displayUserMenu();
            return;
        }

        // Display items in the cart with their quantities
        System.out.println("Items in your cart:");
        int index = 1;
        Map<Integer, Item> indexedCart = new HashMap<>();
        for (Map.Entry<Item, Integer> entry : Cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(index + ". " + item.getName() + " | Quantity: " + quantity + " | Price per unit: " + item.getPrice());
            indexedCart.put(index, item);
            index++;
        }

        // Get user input for the item index
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the index of the item you want to modify: ");
        int selectedIndex = scanner.nextInt();

        if (!indexedCart.containsKey(selectedIndex)) {
            System.out.println("Invalid index. Please try again.");
            return;
        }

        Item selectedItem = indexedCart.get(selectedIndex);
        System.out.print("Enter the new quantity for " + selectedItem.getName() + ": ");
        int newQuantity = scanner.nextInt();

        if (newQuantity < 0) {
            System.out.println("Quantity cannot be negative. Please enter a valid quantity.");
        } else if (newQuantity == 0) {
            Cart.remove(selectedItem);
            System.out.println(selectedItem.getName() + " has been removed from your cart.");
        } else if (newQuantity > selectedItem.getQuantity()) {
            System.out.println("We don't have sufficient quantity available.");
        } else {
            Cart.put(selectedItem, newQuantity);
            System.out.println("Quantity for " + selectedItem.getName() + " has been updated to " + newQuantity + ".");
        }
        System.out.println("Press Enter to return to Previous Menu.");
        saveCartToFile();
        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        displayUserMenu();
    }

    public void removeItemFromCart() {
        System.out.println();
        if (Cart.isEmpty()) {
            System.out.println("Your cart is currently empty.");
            return;
        }

        // Display items in the cart with their quantities
        System.out.println("Items in your cart:");
        int index = 1;
        Map<Integer, Item> indexedCart = new HashMap<>();
        for (Map.Entry<Item, Integer> entry : Cart.entrySet()) {
            Item item = entry.getKey();
            System.out.println(index + ". " + item.getName() + " | Quantity: " + entry.getValue());
            indexedCart.put(index, item);
            index++;
        }

        // Get user input for the item index
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the index of the item you want to remove: ");
        int selectedIndex = scanner.nextInt();

        if (!indexedCart.containsKey(selectedIndex)) {
            System.out.println("Invalid index. Please try again.");
        } else {
            Item selectedItem = indexedCart.get(selectedIndex);
            Cart.remove(selectedItem);
            System.out.println(selectedItem.getName() + " has been removed from your cart.");
        }
        saveCartToFile();
        System.out.println("Press Enter to return to Previous Menu.");
        scanner.nextLine();
        displayUserMenu();
    }

    public void changeDetails(){
        Scanner scan_ccd = new Scanner(System.in);
        System.out.println();
        System.out.println("Select the detail you want to change: ");
        if(this.getStatus().equals("Regular")) {
            System.out.println("1  Name");
            System.out.println("2  Contact Number");
            System.out.println("3  Get VIP");
            System.out.println("4  Return to Main Menu");
            int choice = scan_ccd.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Enter the new Address: ");
                    setName(scan_ccd.nextLine());
                    System.out.println("Your name has been updated. Redirecting to Previous Menu");
                    changeDetails();
                    break;
                case 2:
                    System.out.println("Enter the new Contact Number: ");
                    setContact(scan_ccd.nextLine());
                    System.out.println("Your contact number has been updated. Redirecting to Previous Menu");
                    changeDetails();
                    break;
                case 3:
                    System.out.println("Do you want to buy the VIP for 1 month @Rs.15? (y/n");
                    Scanner scan_ccd1 = new Scanner(System.in);
                    String c = scan_ccd1.nextLine();
                    switch(c){
                        case "y":
                            getVIP();
                            break;
                        case "n":
                            break;
                    }
                    changeDetails();
                    break;
                case 4:
                    displayUserMenu();
                    break;
            }
        }else{
            System.out.println("1  Name");
            System.out.println("2  Contact Number");
            System.out.println("3  Return to Main Menu");
            int choice = scan_ccd.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Enter the new Address: ");
                    setName(scan_ccd.nextLine());
                    System.out.println("Your name has been updated. Redirecting to Previous Menu");
                    changeDetails();
                    break;
                case 2:
                    System.out.println("Enter the new Contact Number: ");
                    setContact(scan_ccd.nextLine());
                    System.out.println("Your contact number has been updated. Redirecting to Previous Menu");
                    changeDetails();
                    break;
                case 3:
                    displayUserMenu();
                    break;
            }
        }
    }
    //get VIP plan for ByteMe
    public void getVIP(){
        setStatus("VIP");
        System.out.println();
        System.out.println("Thank you for choosing our VIP services. The VIP service charge will be added to your next order.");
        System.out.println("Redirecting to Previous Menu");
        displayUserMenu();
    }

    public void cancelOrder() {
        Scanner scanner = new Scanner(System.in);

        // Filter orders with status "Pending", "Being Processed", or "Processed"
        List<Map.Entry<LocalDateTime, Order>> cancellableOrders = OrderHistory.entrySet().stream()
                .filter(entry -> {
                    String status = entry.getValue().getStatus();
                    return status.equals("Pending") || status.equals("Being Processed") || status.equals("Processed");
                })
                .collect(Collectors.toList());

        if (cancellableOrders.isEmpty()) {
            System.out.println("No orders available for cancellation.");
            return;
        }
        System.out.println("Cancellable Orders:");
        for (int i = 0; i < cancellableOrders.size(); i++) {
            Order order = cancellableOrders.get(i).getValue();
            System.out.println((i + 1) + ". Order ID: " + order.getId() +" | Items: "+ order.displayItem() + " | Bill: Rs."+order.getBill() +" | Status: " + order.getStatus());
        }
        System.out.print("Enter the number of the order you wish to cancel: ");
        int choice = scanner.nextInt();
        if (choice < 1 || choice > cancellableOrders.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Order selectedOrder = cancellableOrders.get(choice - 1).getValue();
        selectedOrder.setStatus("Cancelled");
        OrderSystem.getPendingOrders().remove(selectedOrder);
        OrderSystem.saveAllData();
        OrderSystem.loadAllData();
        System.out.println("Order ID " + selectedOrder.getId() + " has been cancelled.");
        if(! selectedOrder.getMoP().getS().equals("Cash/UPI on Delivery")){
            System.out.println("Your refund will be processed withing 2 hours of order time.");
        }
        System.out.println("Press Enter to return to Previous Menu.");
        Scanner s1 = new Scanner(System.in);
        s1.nextLine();
        displayUserMenu();
    }

    public List<Item> displayUniqueItems() {
        Set<Item> uniqueItemsSet = new HashSet<>();
        for (Order order : OrderHistory.values()) {
            uniqueItemsSet.addAll(order.getItemlist().keySet());
        }

        List<Item> uniqueItems = new ArrayList<>(uniqueItemsSet);
        System.out.println("Previously Ordered Items:");

        if (uniqueItems.isEmpty()) {
            System.out.println("You haven't ordered any item yet.");
        } else {
            for (int i = 0; i < uniqueItems.size(); i++) {
                Item item = uniqueItems.get(i);
                System.out.println((i + 1) + ". Name: " + item.getName() + " | Price: Rs." + item.getPrice());
            }
        }
        return uniqueItems;
    }
    public void addReviewForItem() {
        List<Item> uniqueItems = displayUniqueItems();

        if (uniqueItems.isEmpty()) {
            System.out.println("No items available to review.");
            return;
        }

        System.out.println("Select an item to review by entering its index number or name:");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        Item selectedItem = null;

        try {
            int index = Integer.parseInt(userInput) - 1;
            if (index >= 0 && index < uniqueItems.size()) {
                selectedItem = uniqueItems.get(index);
            }
        } catch (NumberFormatException e) {
            for (Item item : uniqueItems) {
                if (item.getName().equalsIgnoreCase(userInput)) {
                    selectedItem = item;
                    break;
                }
            }
        }

        if (selectedItem == null) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        System.out.println("Enter rating for " + selectedItem.getName() + " (1 to 5):");
        int rating;
        while (true) {
            try {
                rating = Integer.parseInt(scanner.nextLine());
                if (rating >= 1 && rating <= 5) {
                    break;
                } else {
                    System.out.println("Rating should be between 1 and 5. Please enter again:");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a number between 1 and 5:");
            }
        }

        System.out.println("Enter a review (or press Enter to skip):");
        String reviewText = scanner.nextLine();

        Review review = reviewText.isEmpty() ? new Review(selectedItem, rating) : new Review(selectedItem, reviewText, rating);

        itemReviews.computeIfAbsent(selectedItem, k -> new ArrayList<>()).add(review);
        OrderSystem.saveItemList();
        OrderSystem.saveMenuToFile();
        System.out.println("Review added successfully for " + selectedItem.getName() + ".");
        System.out.println("Press Enter to go back to the Previous Menu.");
        scanner.nextLine();
        displayUserMenu();
    }
    private void saveCartToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))) {
            for (Map.Entry<Item, Integer> entry : Cart.entrySet()) {
                Item item = entry.getKey();
                int quantity = entry.getValue();
                // Save only name, price, and cart quantity
                writer.write(item.getName() + "," + item.getPrice() + "," + quantity);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving cart to file: " + e.getMessage());
        }
    }
    private Item findItemInMenu(String itemName) {
        Map<String, List<Item>> menu = OrderSystem.getMenu();

        for (List<Item> items : menu.values()) {
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
    public void restoreCartFromFile() {
        File file = new File(CART_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String itemName = parts[0];
                int quantityInCart = Integer.parseInt(parts[2]);

                Item item = findItemInMenu(itemName);
                if (item != null) {
                    Cart.put(item, quantityInCart);
                } else {
                    System.out.println("Item '" + itemName + "' not found in menu. Skipping...");
                }
            }
            System.out.println("Cart restored from file.");
        } catch (IOException e) {
            System.out.println("Error restoring cart from file: " + e.getMessage());
        }
    }

    public void viewOrderStatus() {
        System.out.println();
        System.out.println("Pending Orders:");

        boolean hasPendingOrders = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm:ss");

        for (Map.Entry<LocalDateTime, Order> entry : OrderHistory.entrySet()) {
            Order order = entry.getValue();
            if (!order.getStatus().equalsIgnoreCase("Delivered")) {
                hasPendingOrders = true;

                String formattedTime = entry.getKey().format(formatter);
                System.out.println("Order Time: " + formattedTime);
                System.out.println("Status: " + order.getStatus());

                int x = 0;
                for (Item i : order.getItemlist().keySet()) {
                    x++;
                    System.out.println(x + ". Item Name: " + i.getName() + " | Quantity: " + order.getItemlist().get(i) + " | Price: Rs." + order.getItemlist().get(i) * i.getPrice());
                }

                System.out.println("Total Bill: Rs." + order.getBill());
                System.out.println("-----------------------------------");
            }
        }

        if (!hasPendingOrders) {
            System.out.println("You have no pending orders.");
        }
        System.out.println("Press Enter to go back to Previous Menu");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        displayUserMenu();
    }
    private void cleanupCartFile() {
        File file = new File(CART_FILE);
        if (file.exists() && file.delete()) {
            System.out.println("Cart file deleted.");
        }
    }

    private void logout(){
        OrderSystem.saveAllData();
        cleanupCartFile();
        System.out.println();
        Scanner scan_lo = new Scanner(System.in);
        System.out.println("Logged Out. Press Enter to open Application");
        scan_lo.nextLine();
        try {
            OrderSystem.loadAllData();
            OrderSystem.login();
        } catch (javax.security.auth.login.LoginException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }

    public void displayUserMenu() {
        System.out.println("Customer Menu:");
        System.out.println("1. View Menu");
        System.out.println("   a. View all items");
        System.out.println("   b. Search for an item");
        System.out.println("   c. Filter items by category");
        System.out.println("   d. Sort items by price");
        System.out.println("2. Cart Operations");
        System.out.println("   a. Add items to cart");
        System.out.println("   b. Modify quantities in cart");
        System.out.println("   c. Remove items from cart");
        System.out.println("   d. View cart total");
        System.out.println("3. Checkout");
        System.out.println("4. Order Tracking");
        System.out.println("   a. View order status");
        System.out.println("   b. Cancel order");
        System.out.println("   c. View order history");
        System.out.println("5. Update Personal Details");
        System.out.println("6. Provide review for an item");
        System.out.println("7. LogOut");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: // View Menu
                viewMenu();
                break;

            case 2: // Cart Operations
                System.out.println("Cart Operations:");
                System.out.println("1. Add items to cart");
                System.out.println("2. Modify quantities in cart");
                System.out.println("3. Remove items from cart");
                System.out.println("4. View cart total");
                int cartChoice = scanner.nextInt();
                switch (cartChoice) {
                    case 1:
                        addToCart();
                        break;
                    case 2:
                        modifyItemQuantity();
                        break;
                    case 3:
                        removeItemFromCart();
                        break;
                    case 4:
                        System.out.println("Total Bill = Rs."+calculateBill());
                        System.out.println("Press Enter to return to Previous Menu.");
                        Scanner scan0 = new Scanner(System.in);
                        scan0.nextLine();
                        displayUserMenu();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                break;

            case 3: // Checkout
                placeOrder();
                break;

            case 4: // Order Tracking
                System.out.println("Order Tracking:");
                System.out.println("1. View order status");
                System.out.println("2. Cancel order");
                System.out.println("3. View order history");
                int trackingChoice = scanner.nextInt();
                switch (trackingChoice) {
                    case 1:
                        viewOrderStatus();
                        break;
                    case 2:
                        cancelOrder();
                        break;
                    case 3:
                        viewOrderHistory();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                break;
            case 5://change personal details & get VIP
                changeDetails();
                break;
            case 6: //add review for items
                addReviewForItem();
                break;
            case 7:
                logout();
                break;
            default:
                System.out.println("Invalid choice, please try again.");
                displayUserMenu();
        }
    }

}
