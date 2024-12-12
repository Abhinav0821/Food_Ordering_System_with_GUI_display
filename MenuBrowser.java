import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MenuBrowser {

    private static final String MENU_FILE = "itemsCat.ser";

    public static void displayMenu(Runnable callback) {
        JFrame frame = new JFrame("Menu");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.getContentPane().setBackground(new Color(240, 248, 255));

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(240, 248, 255));
        logoPanel.setPreferredSize(new Dimension(frame.getWidth(), 150));
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon logoIcon = new ImageIcon("src/logo.png");
        Image scaledImage = logoIcon.getImage().getScaledInstance(300, 150, Image.SCALE_SMOOTH);

        logoLabel.setIcon(new ImageIcon(scaledImage));
        logoPanel.add(logoLabel);
        frame.add(logoPanel, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Category");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Available");

        TreeMap<String, List<Item>> menu = loadMenuFromFile();
        if (menu != null) {
            for (Map.Entry<String, List<Item>> entry : menu.entrySet()) {
                String category = entry.getKey();
                for (Item item : entry.getValue()) {
                    tableModel.addRow(new Object[]{
                            category,
                            item.getName(),
                            item.getPrice(),
                            item.isAvailable() ? "Yes" : "No"
                    });
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Menu is empty or could not be loaded.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(173, 216, 230)); // Light blue header
        header.setForeground(Color.BLACK);

        // Create a renderer for left alignment
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Apply the left-alignment renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Back");
        JButton closeButton = new JButton("Close");

        backButton.addActionListener(e -> {
            frame.dispose();
            if (callback != null) {
                SwingUtilities.invokeLater(callback);
            }
        });

        closeButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(backButton);
        buttonPanel.add(closeButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static TreeMap<String, List<Item>> loadMenuFromFile() {
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (TreeMap<String, List<Item>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading menu from file: " + e.getMessage());
            return null;
        }
    }
}
