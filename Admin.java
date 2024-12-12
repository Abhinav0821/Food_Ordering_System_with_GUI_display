import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Admin implements Serializable {
    private static final long serialVersionUID = 7420193723650627658L;
    String name;
    String staffId;
    String password;

    public Admin(String n, String sId, String pwd){
        this.name = n;
        this.staffId =sId;
        this.password = pwd;
    }
    public String getPassword(){
        return this.password;
    }
    public String getStaffId(){
        return this.staffId;
    }
    public void addNewItem() {
        Scanner scan_aani = new Scanner(System.in);
        Scanner scan_aani1 = new Scanner(System.in);
        Scanner scan_aani2 = new Scanner(System.in);
        Scanner scan_aani3 = new Scanner(System.in);
        Scanner scan_aani4 = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter name of the item: ");
        String name = scan_aani.nextLine();
        System.out.println("Enter category of the item: ");
        String category = scan_aani1.nextLine();
        System.out.println("Enter price of the item: ");
        double price = scan_aani2.nextDouble();
        System.out.println("Enter quantity of the item: ");
        int quantity = scan_aani3.nextInt();
        Item newItem = new Item(name, category,quantity, price);
        OrderSystem.addItemToMenu(category, newItem);
        System.out.println("Item added to the menu: " + name);
        System.out.println("Press Enter to return to Previous Menu");
        OrderSystem.saveMenuToFile();
        OrderSystem.saveItemList();
        scan_aani4.nextLine();
        displayUserMenu();
    }

    public void updateItemDetails() {
        Scanner scan_auid = new Scanner(System.in);
        System.out.println();
        if(!OrderSystem.getmenu().isEmpty()) {
            OrderSystem.viewmenu();
            System.out.println();
            Item i = null;
            System.out.println("Press 1 to search for item using index number.");
            System.out.println("Press 2 to search for item using name.");
            System.out.println("Press 3 to search for item using keyword");
            int c = scan_auid.nextInt();
            switch (c) {
                case 2:
                    Scanner scan_auid2 = new Scanner(System.in);
                    System.out.println("Enter name of the item to be updated: ");
                    String itemName = scan_auid2.nextLine();
                    i = OrderSystem.searchItemByName(itemName);
                    break;
                case 1:
                    Scanner scan_auid1 = new Scanner(System.in);
                    System.out.println("Enter index of the item to be updated: ");
                    int ind = scan_auid1.nextInt();
                    i = OrderSystem.getmenu().get(ind - 1);
                    break;
                case 3:
                    Scanner scan_auid3 = new Scanner(System.in);
                    System.out.println("Enter keyword(prefix of item name) of the item to be updated: ");
                    String k = scan_auid3.nextLine();
                    System.out.println("List of items related to the keyword: ");
                    for (Item a : OrderSystem.searchItemByPrefix(k)) {
                        int b = 1;
                        System.out.println(b + ". " + a.getName() + " " + a.getPrice() + " " + a.getCategory() + " " + a.getQuantity());
                        b++;
                    }
                    System.out.println("Enter the index of the item from above list: ");
                    int z = scan_auid3.nextInt();
                    i = OrderSystem.searchItemByPrefix(k).get(z-1);
                    break;
            }
            if (i != null) {
                System.out.println("Select detail of the item to be updated: ");
                System.out.println("1  Name ");
                System.out.println("2  Price");
                System.out.println("3  Quantity");
                int choice = scan_auid.nextInt();
                switch (choice) {
                    case 1:
                        Scanner scan_auid11 = new Scanner(System.in);
                        System.out.println("Enter new name of the item: ");
                        String name = scan_auid11.nextLine();
                        i.setName(name);
                        System.out.println("Item name has been changed.");
                        break;
                    case 2:
                        Scanner scan_auid12 = new Scanner(System.in);
                        System.out.println("Enter new price of the item: ");
                        double p = scan_auid12.nextDouble();
                        i.setPrice(p);
                        System.out.println("Item price has been changed");
                        break;
                    case 3:
                        Scanner scan_auid13 = new Scanner(System.in);
                        System.out.println("Enter new quantity of the item in stock: ");
                        int q = scan_auid13.nextInt();
                        i.setPrice(q);
                        System.out.println("Item quantity has been changed");
                        break;
                }
            } else {
                System.out.println("Item not found in the menu.");
            }
        }else{
            System.out.println("No item in the menu");
        }
        OrderSystem.saveMenuToFile();
        OrderSystem.saveItemList();
        System.out.println("Press Enter to return to Previous Menu");
        scan_auid.nextLine();
        displayUserMenu();
    }

    public void removeItem() {
        Scanner scan_ari = new Scanner(System.in);
        System.out.println();
        OrderSystem.viewmenu();
        if(!OrderSystem.getmenu().isEmpty()) {
            System.out.println("Enter the name of item to be removed:");
            String itemName = scan_ari.nextLine();
            Item item = OrderSystem.searchItemByName(itemName);
            if (item != null) {
                OrderSystem.removeItemFromMenu(item.getCategory(), item);  // Remove item globally
                System.out.println("Item removed: " + itemName);
                for (Order order : OrderSystem.getPendingOrders()) {
                    if (order.getItemlist().containsKey(item)) {
                        order.setStatus("Denied");
                    }
                    if (order.getMoP().equals("Cash/UPI on Delivery")) {
                        order.setRefundProcessed(true);
                    }
                }
            } else {
                System.out.println("Item not found in the menu.");
            }
        }else{
            System.out.println("No item in the menu");
        }
        System.out.println("Press Enter to return to Main Menu");
        OrderSystem.saveItemList();
        OrderSystem.saveMenuToFile();
        scan_ari.nextLine();
        displayUserMenu();
    }

    // Order Management
    public void viewPendingOrders() {
        Scanner scan_avpo = new Scanner(System.in);
        System.out.println();
        if(!(OrderSystem.getPendingOrders().isEmpty())) {
            List<Order> pendingOrders = OrderSystem.getPendingOrdersList();
            System.out.println("Pending Orders:");
            for (Order order : pendingOrders) {
                System.out.println(" - Order ID: " + order.getId() + " | Customer: " + order.getC().getName()+" | Items: "+order.displayItem()+" | Bill: Rs."+order.getBill() +" | Mode of Payment: "+order.getMoP().getS());
            }
            System.out.println("Do you want to search for a particular order? (y/n)");
            Scanner scan_avpo1 = new Scanner(System.in);
            String choice = scan_avpo1.nextLine();
            switch (choice) {
                case "y":
                    Order o = null;
                    System.out.println("Press 1 to search using order id.");
                    System.out.println("Press 2 to search using index number from above list.");
                    int c = scan_avpo1.nextInt();
                    switch (c) {
                        case 1:
                            Scanner scan_avpo2 = new Scanner(System.in);
                            System.out.println("Enter the order id:");
                            int oId = scan_avpo2.nextInt();
                            o = OrderSystem.getOrderById(oId);
                            break;
                        case 2:
                            Scanner scan_avpo3 = new Scanner(System.in);
                            System.out.println("Enter the index number:");
                            int ind = scan_avpo3.nextInt();
                            o = OrderSystem.getOrderByIndex(ind);
                            break;
                    }
                    System.out.println("Order ID: " + o.getId() + " | Customer Name : " + o.getC().getName() + " | Items Ordered : " + o.displayItem() + " | Bill : " + o.getBill());
                    if(!o.getspecialRequest().equals("")) {
                        System.out.println("The order has a special request from the customer: "+o.getspecialRequest());
                    }else{
                        System.out.println("There is no special request for this order.");
                    }
                    System.out.println("Do you want to change the status for this order? (y/n)");
                    Scanner scan_avpo11 = new Scanner(System.in);
                    String c11 = scan_avpo11.nextLine();
                    switch(c11){
                        case "n":
                            break;
                        case "y":
                            updateOrderStatus(o);
                            break;
                    }
                case "n":
                    break;

            }
        }else{
            System.out.println("No Pending Order");
        }
        System.out.println("Press Enter to return to Previous Menu.");
        scan_avpo.nextLine();
        displayUserMenu();
    }
    public void viewPendingOrders1() {
        System.out.println();
        if(!(OrderSystem.getPendingOrders().isEmpty())) {
            List<Order> pendingOrders = OrderSystem.getPendingOrdersList();
            System.out.println("Pending Orders:");
            for (Order order : pendingOrders) {
                System.out.println(" - Order ID: " + order.getId() + "| Customer: " + order.getC().getName()+"| Bill: Rs."+order.getBill() +"| Mode of Payment: "+order.getMoP().getS());
            }
        }else{
            System.out.println("No Pending Order");
        }
    }
    public void updateOrderStatus() {
        System.out.println();
        Scanner scan_auos = new Scanner(System.in);
        viewPendingOrders1();
        if(!OrderSystem.getPendingOrders().isEmpty()) {
            System.out.println("Enter id of the order: ");
            int orderId = scan_auos.nextInt();
            Order order = OrderSystem.getOrderById(orderId);
            System.out.println("Choose the status of order: ");
            int i = 1;
            for (String s : OrderSystem.getAllStatuses()) {
                System.out.println(i + "    " + s);
                i++;
            }
            int choice = scan_auos.nextInt();
            if (order != null) {
                order.setStatus(OrderSystem.getAllStatuses()[choice - 1]);
                System.out.println("Order status updated: " + orderId + " to " + OrderSystem.getAllStatuses()[choice - 1]);
                if (choice == 11 || choice == 10) {
                    OrderSystem.addToCompletedOrdersForToday(order);
                    OrderSystem.getPendingOrders().remove(order);
                    System.out.println("The order is completed.");
                }
            } else {
                System.out.println("Order not found.");
            }
        }else{
            System.out.println("No Incomplete Order.");
        }
        System.out.println("Press Enter to return to Previous Menu.");
        scan_auos.nextLine();
        displayUserMenu();
    }
    public void updateOrderStatus(Order o) {
        System.out.println();
        Scanner scan_auos = new Scanner(System.in);
        System.out.println("Choose the status for the order "+o.getId());
        int i=0;
        for(String s : OrderSystem.getAllStatuses()){
            i++;
            System.out.println((i+1)+". "+OrderSystem.getAllStatuses()[i]);
        }
        int ind = scan_auos.nextInt();
        o.setStatus(OrderSystem.getAllStatuses()[ind-1]);
        if (OrderSystem.getAllStatuses()[ind - 1].equals("Completed") || OrderSystem.getAllStatuses()[ind - 1].equals("Refund Completed") ||  (OrderSystem.getAllStatuses()[ind - 1].equals(" Denied") && o.getMoP().equals("Cash/UPI on Delivery"))) {
            OrderSystem.getPendingOrders().remove(o);
            OrderSystem.getCompletedOrdersForToday().add(o);
            System.out.println("The order is completed.");
        }Scanner scan_auos9 = new Scanner(System.in);
        System.out.println("Press Enter to return to Previous Menu.");
        scan_auos9.nextLine();
        displayUserMenu();
    }
    public void processRefund() {
        Scanner scan_apr = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter ID of the order for which refund has to be processed: ");
        int orderId = scan_apr.nextInt();
        Order order = OrderSystem.getOrderById(orderId);
        if (order != null) {
            order.setRefundProcessed(true);
            System.out.println("Refund processed for order: " + orderId);
        } else {
            System.out.println("Order not found.");
        }
    }
    private void logout(){
        OrderSystem.saveAllData();
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
    public void handleSpecialRequests() {
        System.out.println();
        Scanner scan_apr = new Scanner(System.in);
        viewPendingOrders1();
        System.out.println("Enter ID of the order for which the special request has to be handled: ");
        int orderId = scan_apr.nextInt();
        Order order = OrderSystem.getOrderById(orderId);
        if (order != null) {
            if(!order.getspecialRequest().equals("")) {
                System.out.println("Special request for the order " + orderId + ": " + order.getspecialRequest());
            }else{
                System.out.println("There is no request for the order " + orderId);
            }
        } else {
            System.out.println("Order not found.");
        }
        System.out.println("Press Enter to return to Previous Menu.");
        scan_apr.nextLine();
        displayUserMenu();
    }

    // Report Generation
    public void generateDailySalesReport() {
        double totalSales = 0;
        Map<Item, Integer> itemSalesCount = new HashMap<>();

        List<Order> completedOrders = OrderSystem.getCompletedOrdersForToday();
        int totalOrders = completedOrders.size();

        for (Order order : completedOrders) {
            totalSales += order.getBill();

            for (Item item : order.getItemlist().keySet()) {
                itemSalesCount.put(item, itemSalesCount.getOrDefault(item, 0) + 1);
            }
        }

        System.out.println("Daily Sales Report:");
        System.out.println("Total Sales: $" + totalSales);
        System.out.println("Total Orders: " + totalOrders);
        System.out.println("Most Popular Items:");

        itemSalesCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> {
                    System.out.println(" - " + entry.getKey().getName() + ": " + entry.getValue() + " orders");
                });

        System.out.println("Press Enter to return to Previous Menu");
        Scanner s = new Scanner(System.in);
        s.nextLine();
        displayUserMenu();
    }

    public void displayUserMenu() {
        System.out.println();
        System.out.println("Admin Menu:");
        System.out.println("1. Manage Menu");
        System.out.println("   a. Add new items");
        System.out.println("   b. Update existing items");
        System.out.println("   c. Remove items");
        System.out.println("2. Manage Orders");
        System.out.println("   a. View pending orders");
        System.out.println("   b. Update order status");
        System.out.println("   c. Process refunds");
        System.out.println("   d. Handle special requests");
        System.out.println("3. Generate daily sales report");
        System.out.println("4. LogOut");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: // Manage Menu
                System.out.println("Manage Menu:");
                System.out.println("1. Add new items");
                System.out.println("2. Update existing items");
                System.out.println("3. Remove items");
                int menuChoice = scanner.nextInt();
                switch (menuChoice) {
                    case 1:
                        addNewItem();
                        break;
                    case 2:
                        updateItemDetails();
                        break;
                    case 3:
                        removeItem();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                break;

            case 2: // Manage Orders
                System.out.println("Order Management:");
                System.out.println("1. View pending orders");
                System.out.println("2. Update order status");
                System.out.println("3. Process refunds");
                System.out.println("4. Handle special requests");
                int orderChoice = scanner.nextInt();
                switch (orderChoice) {
                    case 1:
                        viewPendingOrders();
                        break;
                    case 2:
                        updateOrderStatus();
                        break;
                    case 3:
                        processRefund();
                        break;
                    case 4:
                        handleSpecialRequests();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
                break;

            case 3: // Report Generation
                generateDailySalesReport();
                break;

            case 4:
                logout();
                break;

            default:
                System.out.println("Invalid choice, please try again.");
        }
    }

}
