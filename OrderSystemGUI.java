import javax.swing.*;
import java.awt.*;

public class OrderSystemGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderSystemGUI::displayMenuGUI);
    }

    private static void displayMenuGUI() {
        JFrame frame = new JFrame("Order System GUI");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set the background color
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(240, 248, 255));
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setIcon(new ImageIcon("src/logo.png"));
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(e -> {
            frame.dispose();
            MenuBrowser.displayMenu(OrderSystemGUI::displayMenuGUI);
        });

        JButton ordersBtn = new JButton("View Pending Orders");
        ordersBtn.addActionListener(e -> {
            frame.dispose();
            PendingOrderBrowser.displayPendingOrders(OrderSystemGUI::displayMenuGUI);
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(menuButton);
        buttonPanel.add(ordersBtn);
        buttonPanel.add(closeButton);

        frame.add(logoPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
