import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class CartView {
    private JPanel cartViewPanel;
    private JButton checkoutButton;
    private JLabel product1Label;
    private JLabel product2Label;
    private JLabel product3Label;
    private JLabel product4Label;
    private JLabel p1qty;
    private JLabel p2qty;
    private JLabel p3qty;
    private JLabel p4qty;
    private JLabel cartTotal;
    private JLabel p1price;
    private JLabel p2price;
    private JLabel p3price;
    private JLabel p4price;
    private JButton backButton;

    private int cart_id; // to store the cart_id corresponding to this user
    private int user_id;
    private boolean isAdmin;

    // to store all the product_ids
    String DBURL = "jdbc:oracle:thin@coeoracle.aus.edu:1521:orcl";
    String DBUSER = "b00072311";
    String DBPASS = "b00072311";

    Connection con;
    Statement statement;
    PreparedStatement prepStatement;
    ResultSet rs;

    public CartView(int cart_id, int user_id, boolean isAdmin) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.isAdmin = isAdmin;

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Connect to Oracle Database
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);

            prepStatement = con.prepareStatement("SELECT p_name, price, quantity, product_id FROM PRODUCTS");

            setupComponents();


        } catch (ClassNotFoundException | SQLException e) {
            JLabel label = new JLabel("SQL Error");
            e.printStackTrace();
            label.setFont(new Font("Arial", Font.BOLD, 18));
        }

    }

    public JPanel getPanel() {
        return cartViewPanel;
    }


    private void setupComponents() {

        try {
            // get all the products and display them
            rs = prepStatement.executeQuery();
            ArrayList<JLabel> allLabels = new ArrayList<>();
            allLabels.add(product1Label);
            allLabels.add(product2Label);
            allLabels.add(product3Label);
            allLabels.add(product4Label);

            ArrayList<JLabel> allPriceLabels = new ArrayList<>();
            allPriceLabels.add(p1price);
            allPriceLabels.add(p2price);
            allPriceLabels.add(p3price);
            allPriceLabels.add(p4price);


            int i = 0;
            while (rs.next()) {
                String name = rs.getString("p_name");
                BigDecimal price = rs.getBigDecimal("price");

                allLabels.get(i).setText("Name: " + name);
                allPriceLabels.get(i).setText("Price: $" + price);
                i++;
            }

            // we now need to update the qty values based on the cart_id

            prepStatement = con.prepareStatement("SELECT product_id, quantity_of_product FROM CONTAINS WHERE cart_id=?");
            prepStatement.setInt(1, cart_id);
            rs = prepStatement.executeQuery();

            while (rs.next()) {
                int current_product_id = rs.getInt("product_id");

                if (current_product_id == 1)
                    p1qty.setText("Quantity: " + rs.getInt("quantity_of_product"));
                else if (current_product_id == 2)
                    p2qty.setText("Quantity: " + rs.getInt("quantity_of_product"));
                else if (current_product_id == 3)
                    p3qty.setText("Quantity: " + rs.getInt("quantity_of_product"));
                else if (current_product_id == 4)
                    p4qty.setText("Quantity: " + rs.getInt("quantity_of_product"));
            }


            // function uses aggregate function SUM
            updateCartTotal();

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isAdmin)
                        Main.updatePanel(new AdminView(user_id).getPanel());
                    else
                        Main.updatePanel(new CustomerView(user_id).getPanel());

                }
            });

            checkoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        prepStatement = con.prepareStatement("DELETE FROM CONTAINS WHERE cart_id=?");
                        prepStatement.setInt(1, cart_id);

                        if (prepStatement.executeUpdate() == 1) {
                            JOptionPane.showMessageDialog(null, "You have checked out!");
                        }

                        // we now need to update the qty values to 0
                        p1qty.setText("Quantity: 0");
                        p2qty.setText("Quantity: 0");
                        p3qty.setText("Quantity: 0");
                        p4qty.setText("Quantity: 0");


                        // function uses aggregate function SUM
                        updateCartTotal();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCartTotal() {
        try {
            prepStatement = con.prepareStatement("SELECT SUM(c.quantity_of_product * p.price) as total FROM CONTAINS c, PRODUCTS p WHERE c.product_id = p.product_id and c.cart_id=?");
            prepStatement.setInt(1, cart_id);
            rs = prepStatement.executeQuery();

            cartTotal.setText("Cart Total: $" + 0);

            if (rs.next()) {
                int total = rs.getInt("total");
                cartTotal.setText("Cart Total: $" + total);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        cartViewPanel = new JPanel();
        cartViewPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 7, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Your Cart:");
        cartViewPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkoutButton = new JButton();
        checkoutButton.setText("Checkout");
        cartViewPanel.add(checkoutButton, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product1Label = new JLabel();
        product1Label.setText("Product Name");
        cartViewPanel.add(product1Label, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p1qty = new JLabel();
        p1qty.setText("Quantity: 0");
        cartViewPanel.add(p1qty, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p1price = new JLabel();
        p1price.setText("Price: ");
        cartViewPanel.add(p1price, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product2Label = new JLabel();
        product2Label.setText("Product Name");
        cartViewPanel.add(product2Label, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p2qty = new JLabel();
        p2qty.setText("Quantity: 0");
        cartViewPanel.add(p2qty, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p2price = new JLabel();
        p2price.setText("Price:");
        cartViewPanel.add(p2price, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product3Label = new JLabel();
        product3Label.setText("Product Name");
        cartViewPanel.add(product3Label, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p3qty = new JLabel();
        p3qty.setText("Quantity: 0");
        cartViewPanel.add(p3qty, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p3price = new JLabel();
        p3price.setText("Price:");
        cartViewPanel.add(p3price, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product4Label = new JLabel();
        product4Label.setText("Product Name");
        cartViewPanel.add(product4Label, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p4qty = new JLabel();
        p4qty.setText("Quantity: 0");
        cartViewPanel.add(p4qty, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p4price = new JLabel();
        p4price.setText("Price:");
        cartViewPanel.add(p4price, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cartTotal = new JLabel();
        cartTotal.setText("Cart Total: ");
        cartViewPanel.add(cartTotal, new com.intellij.uiDesigner.core.GridConstraints(5, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        cartViewPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        cartViewPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return cartViewPanel;
    }

}
