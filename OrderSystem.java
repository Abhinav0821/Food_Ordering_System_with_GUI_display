import javax.security.auth.login.LoginException;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class OrderSystem {
    public static void setMenu(TreeMap<String, List<Item>> menu) {
        Menu = menu;
    }
    private static final String FILE_PATH = "pendingOrders.ser";
    private static TreeMap<String, List<Item>> Menu = new TreeMap<>();
    private static List<Item> menu = new ArrayList<>();
    private static List<Order> completedOrdersForToday = new ArrayList<>();

    public static Trie getItemTrie() {
        return itemTrie;
    }
    //add an order to today's completed orders if completed(Completed/Refund Completed) today
    public static void addToCompletedOrdersForToday(Order order) {
        if (order.getTime().toLocalDate().isEqual(LocalDate.now())) {
            completedOrdersForToday.add(order);
        }
    }
    public static String findCategoryByIndex(int i){
        String a ="";
        int x=1;
        if(i<getMenu().size()) {
            for (String k : getMenu().keySet()) {
                if(x != i){x++;}
                else if(x==i){a =k;}
            }
        } return a;
    }
    // Refresh method to reset and repopulate completed orders each day
    public static void refreshCompletedOrdersForToday() {
        completedOrdersForToday.clear();
        LocalDate today = LocalDate.now();

        for (Order order : allOrders) {
            if (order.getStatus().equals("Completed") &&
                    order.getTime().toLocalDate().isEqual(today)) {
                completedOrdersForToday.add(order);
            }
        }
    }

    private static Trie itemTrie = new Trie();
    public static List<Item> getmenu(){
        return menu;
    }
    public static List<Customer> getCustomers() {
        return customers;
    }

    public static List<Admin> getAdmins() {
        return admins;
    }

    private static List<Customer> customers = new ArrayList<>();
    private static List<Admin> admins = new ArrayList<>();
    private static final String CUSTOMER_FILE = "customers.ser";
    private static final String ADMIN_FILE = "admins.ser";
    private static final String ALL_ORDERS_FILE = "allOrders.ser";
    private static final String ITEMS_FILE = "items.ser";
    private static final String MENU_FILE = "itemsCat.ser";

    public static void loadUserData() {
        customers = loadFromFile(CUSTOMER_FILE);
        admins = loadFromFile(ADMIN_FILE);
    }
    public static void loadItemList(){
        menu = loadFromFile(ITEMS_FILE);
    }
    public static void saveItemList(){
        saveToFile(menu, ITEMS_FILE);
    }
    // load list from file with type safety
    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> loadFromFile(String fileName, Class<T> clazz) {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Object loadedObject = ois.readObject();

            if (loadedObject instanceof ArrayList<?>) {
                ArrayList<?> rawList = (ArrayList<?>) loadedObject;

                for (Object obj : rawList) {
                    if (!clazz.isInstance(obj)) {
                        throw new ClassCastException("List contains an element of incorrect type");
                    }
                }
                return (ArrayList<T>) rawList;
            } else {
                System.out.println();
                return new ArrayList<>();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return new ArrayList<>();
        }
    }
    public static Item findItemInMenu(String itemName) {
        Map<String, List<Item>> menu = OrderSystem.getMenu();

        // Iterate through categories and items to find the matching item
        for (List<Item> items : menu.values()) {
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }

    // Method to save the Menu TreeMap to a file
    public static void saveMenuToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MENU_FILE))) {
            oos.writeObject(Menu);
        } catch (IOException e) {
            System.out.println();
        }
    }

    //load the Menu TreeMap from a file
    @SuppressWarnings("unchecked")
    public static void loadMenuFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MENU_FILE))) {
            Menu = (TreeMap<String, List<Item>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
        }
    }
    // Save data to files
    public static void saveUserData() {
        saveToFile(customers, CUSTOMER_FILE);
        saveToFile(admins, ADMIN_FILE);
    }

    // Load allOrders from file
    private static ArrayList<Order> allOrders = new ArrayList<>();

    static {
        allOrders = loadFromFile(ALL_ORDERS_FILE, Order.class);
    }
    public static void viewmenu(){
        int x=1;
        for(Item i : menu){
            System.out.println(x+" "+i.getName()+" "+i.getPrice()+" "+i.getCategory()+" "+i.getQuantity());
            x++;
        }
    }
    public static void loadAllData() {
        loadUserData();          // Loads customers and admins
        loadItemList();          //loads items in the menu
        loadMenuFromFile();
        loadPendingOrders();     // Loads pending orders
        allOrders = loadFromFile(ALL_ORDERS_FILE, Order.class); // Loads all orders
    }
    public static void saveAllData() {
        saveItemList();
        saveMenuToFile();
        saveUserData();          // Saves customers and admins
        savePendingOrders();     // Saves pending orders
        saveAllOrders(); // Saves all orders
    }
    public static void saveAllOrders() {
        saveToFile(allOrders, ALL_ORDERS_FILE);
    }

    //load list from file
    private static <T> List<T> loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return new ArrayList<>();
        }
    }

    //save list to file
    private static <T> void saveToFile(List<T> list, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println();
        }
    }

    public static ArrayList<Order> getAllOrders() {
        return allOrders;
    }

    private static PriorityQueue<Order> pendingOrders = new PriorityQueue<>(new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getPriority().equals("VIP") && !o2.getPriority().equals("VIP")) return -1;
            if (!o1.getPriority().equals("VIP") && o2.getPriority().equals("VIP")) return 1;

            return o1.getTime().compareTo(o2.getTime());
        }
    });
    // Save the priority queue to file
    public static void savePendingOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(pendingOrders);
        } catch (IOException e) {
            System.out.println();
        }
    }
    // Load the priority queue from the file
    @SuppressWarnings("unchecked")
    public static void loadPendingOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            pendingOrders = (PriorityQueue<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
        }
    }
    // full menu organized by category
    public static TreeMap<String, List<Item>> getMenu() {
        return Menu;
    }
    public static List<Order> getCompletedOrdersForToday() {
        return completedOrdersForToday;
    }
    public static List<Item> searchItemByPrefix(String prefix) {
        return itemTrie.searchByPrefix(prefix);
    }
    // Adds an item to the Menu based on its category
    public static void addItemToMenu(String category, Item item) {
        Menu.putIfAbsent(category, new ArrayList<>());
        Menu.get(category).add(item);
        menu.add(item);
        itemTrie.insert(item.getName(), item);
    }
    public static void removeItemFromMenu(String category, Item item) {
        List<Item> items = Menu.get(category);
        if (items != null) {
            items.remove(item);
            if (items.isEmpty()) {
                Menu.remove(category);
            }
        }
    }
    public static Order getOrderByIndex(int ind){
        int i=0;
        for(Order o : pendingOrders){
            i = i+1;
            if(i == ind){
                return o;
            }
        }return null;
    }
    // Search for an item by name
    public static Item searchItemByName(String name) {
        for (List<Item> items : Menu.values()) {
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null;
    }
    public static PriorityQueue<Order> getPendingOrders() {
        return pendingOrders;
    }
    public static List<Order> getPendingOrdersList() {
        List<Order> ordersList = new ArrayList<>(pendingOrders);
        return ordersList;
    }

    // Filter and return a list of items in a category
    public static List<Item> filteredMenu(String filt) {
        return Menu.getOrDefault(filt, Collections.emptyList());
    }
    public static Order getOrderById(int orderId) {
        for (Order order : pendingOrders) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }
    // Display the menu categorized and alphabetically ordered
    public static void displayFullMenu() {
        System.out.println("Complete Menu:");
        for (Map.Entry<String, List<Item>> entry : Menu.entrySet()) {
            System.out.println("Category: " + entry.getKey());
            for (Item item : entry.getValue()) {
                System.out.println(" - " + item.getName() + " | Price: " + item.getPrice() + " | Available: " + (item.isAvailable() ? "Yes" : "No"));
            }
        }
    }
    private static final String[] ALL_STATUSES = {
            "Pending",
            "Being Processed",
            "Prepared",
            "Out for Delivery",
            "Delivered",
            "Cancelled",
            "Denied",
            "Returned",
            "Refund Initiated",
            "Refund Completed",
            "Completed",
            "Cancelled",
            "Processed"
    };
    public static String[] getAllStatuses() {
        return ALL_STATUSES;
    }
    //authenticate an admin
    public static boolean authenticateAdmin(String staffId, String password) {
        loadUserData(); // Load existing data from file
        for (Admin admin : admins) {
            if (admin.getStaffId().equals(staffId) && admin.getPassword().equals(password)) {
                System.out.println("Admin login successful for Staff ID: " + staffId);
                return true;
            }
        }
        System.out.println("Admin login failed for Staff ID: " + staffId);
        return false;
    }

    //authenticate a customer
    public static boolean authenticateCustomer(String roll, String password) {
        loadUserData();
        for (Customer customer : customers) {
            if (customer.getRollnum().equals(roll) && customer.getPassword().equals(password)) {
                System.out.println("Customer login successful for Roll Number: " + roll);
                return true;
            }
        }
        System.out.println("Customer login failed for Contact Number: " + roll);
        return false;
    }

    // Login method that validates user login attempts
    public static void login() throws LoginException {
        System.out.println();
        boolean success;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to ByteMe Order System");

        System.out.print("Do you already have an account? (yes/no): ");
        String hasAccount = scanner.nextLine().trim();

        if (hasAccount.equalsIgnoreCase("yes")) {

            System.out.print("Are you an Admin or Customer? ");
            String userType = scanner.nextLine().trim();

            boolean loginSuccess = false;
            for (int attempts = 1; attempts <= 5; attempts++) {
                System.out.print((userType.equalsIgnoreCase("Admin") ? "Staff ID" : "Roll Number") + ": ");
                String identifier = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
                try {
                    if (userType.equalsIgnoreCase("Admin")) {
                        success = authenticateAdmin(identifier, password);
                    } else if (userType.equalsIgnoreCase("Customer")) {
                        success = authenticateCustomer(identifier, password);
                    } else {
                        throw new LoginException("Invalid user type.");
                    }

                    if (!success) {
                        throw new LoginException("Invalid credentials for " + userType + ".");
                    }
                    loginSuccess = true;
                    if(userType.equals("Customer")){
                        Customer c = getCustomerByRoll(identifier);
                        c.displayUserMenu();
                    } else if (userType.equals("Admin")) {
                        Admin a = getAdminById((identifier));
                        a.displayUserMenu();
                    }
                    break;
                } catch (LoginException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Attempt " + attempts + " of 5 failed.");
                }
            }
            if (!loginSuccess) {
                System.out.print("Login failed after 5 attempts. Would you like to sign up instead? (yes/no): ");
                String signUpResponse = scanner.nextLine().trim();
                if (signUpResponse.equalsIgnoreCase("yes")) {
                    handleSignUp(scanner);
                    main(new String[]{});
                } else {
                    System.out.println("Exiting application. Please try again later.");
                }
            }

        } else if (hasAccount.equalsIgnoreCase("no")) {
            handleSignUp(scanner);
            System.out.println("Sign-up successful! Please proceed to login.");
            main(new String[]{});
        } else {
            System.out.println("Invalid input. Exiting application.");
        }

        scanner.close();

    }
    public static Customer getCustomerByRoll(String roll){
        for(Customer c: customers){
            if(c.getRollnum().equals(roll)){
                return c;
            }
        }return null;
    }
    public static Admin getAdminById(String id){
        for(Admin a: admins){
            if(a.getStaffId().equals(id)){
                return a;
            }
        }return null;
    }
    public static void main(String[] args) {

        loadAllData();
        System.out.println(OrderSystem.getMenu().size());
        try {
            login();
        } catch (javax.security.auth.login.LoginException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
    }
    private static void handleSignUp(Scanner scanner) {
        System.out.print("Are you signing up as an Admin or Customer? ");
        String userType = scanner.nextLine().trim();

        System.out.print((userType.equalsIgnoreCase("Admin") ? "Staff ID" : "Roll Number") + ": ");
        String identifier = scanner.nextLine().trim();
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        signUp(userType, identifier, name, password, scanner);
    }

    // Sign up method for new users with duplicate check
    public static void signUp(String userType, String identifier, String name, String password, Scanner scanner) {
        if (userType.equalsIgnoreCase("Customer")) {
            for (Customer customer : customers) {
                if (customer.getRollnum().equals(identifier)) {
                    System.out.println("This roll number is already signed up.");
                    handleDuplicateSignup();
                    return;
                }
            }
            System.out.print("Contact Number: ");
            String cn = scanner.nextLine().trim();
            customers.add(new Customer(name, identifier, cn, password, "Regular"));
        } else if (userType.equalsIgnoreCase("Admin")) {
            for (Admin admin : admins) {
                if (admin.getStaffId().equals(identifier)) {
                    System.out.println("This staff ID is already signed up.");
                    handleDuplicateSignup();
                    return;
                }
            }
            admins.add(new Admin(name, identifier, password));
        }

        saveUserData();
        System.out.println(userType + " signed up successfully!");
    }
    private static void handleDuplicateSignup() {
        System.out.println("Would you like to (1) Log in or (2) Try signing up again?");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1) {
            main(new String[]{});
        } else if (choice == 2) {
            handleSignUp(scanner);
        } else {
            System.out.println("Invalid choice. Returning to main menu.");
        }
    }


}
