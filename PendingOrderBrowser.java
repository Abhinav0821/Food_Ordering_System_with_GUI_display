import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PendingOrderBrowser {

    private static final String ALL_ORDERS_FILE = "allOrders.ser";

    public static void displayPendingOrders(Runnable callback) {
        JFrame frame = new JFrame("Pending Orders");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(240, 248, 255));
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("src/logo1.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledImage));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoPanel.add(logoLabel);

        mainPanel.add(logoPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Order ID");
        tableModel.addColumn("Order Time");
        tableModel.addColumn("Items");
        tableModel.addColumn("Bill");
        tableModel.addColumn("Status");

        List<Order> allOrders = loadOrdersFromFile();
        if (allOrders != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            allOrders.stream()
                    .filter(order -> !order.getStatus().equalsIgnoreCase("Delivered")
                            && !order.getStatus().equalsIgnoreCase("Refund Completed")
                            && !order.getStatus().equalsIgnoreCase("Cancelled"))
                    .forEach(order -> tableModel.addRow(new Object[]{
                            order.getId(),
                            order.getTime().format(formatter),
                            order.displayItem(),
                            order.getBill(),
                            order.getStatus()
                    }));
        } else {
            JOptionPane.showMessageDialog(frame, "No orders available or file could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(tableModel);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(173, 216, 230));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton backButton = new JButton("Back");
        JButton closeButton = new JButton("Close");

        backButton.addActionListener(e -> {
            frame.dispose();
            if (callback != null) {
                SwingUtilities.invokeLater(callback);
            }
        });

        closeButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(backButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static List<Order> loadOrdersFromFile() {
        File file = new File(ALL_ORDERS_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Order>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading orders from file: " + e.getMessage());
            return null;
        }
    }
}
