import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class CustomerView {
    private JButton addToCartButton;
    private JButton removeFromCartButton;
    private JButton addToCartButton1;
    private JButton removeFromCartButton1;
    private JButton addToCartButton3;
    private JButton removeFromCartButton2;
    private JButton addToCartButton2;
    private JButton removeFromCartButton3;
    private JButton logoutButton;
    private JPanel customerViewRootPanel;
    private JButton viewCartButton;
    private JLabel product1Label;
    private JLabel product2Label;
    private JLabel product3Label;
    private JLabel product4Label;
    private JLabel welcomeMessage;
    private JLabel p1qty;
    private JLabel p2qty;
    private JLabel p3qty;
    private JLabel p4qty;

    private int user_id;
    private int cart_id; // to store the cart_id corresponding to this user

    // to store all the product_ids
    ArrayList<Integer> allProductIDs;

    String DBURL = "jdbc:oracle:thin@coeoracle.aus.edu:1521:orcl";
    String DBUSER = "b00072311";
    String DBPASS = "b00072311";

    Connection con;
    Statement statement;
    PreparedStatement prepStatement;
    ResultSet rs;

    public CustomerView(int user_id) {
        this.user_id = user_id;
        allProductIDs = new ArrayList<>();

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
        return customerViewRootPanel;
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

            int i = 0;
            while (rs.next()) {
                String name = rs.getString("p_name");
                BigDecimal price = rs.getBigDecimal("price");
                int quantity = rs.getInt("quantity");

                allProductIDs.add(rs.getInt("product_id"));

                allLabels.get(i).setText("Name: " + name + "        Price: $" + price.toString() + "        Stock: " + quantity);
                i++;
            }


            // get the cart id of this user via the BELONGS_TO table
            prepStatement = con.prepareStatement("SELECT cart_id FROM BELONGS_TO WHERE user_id=?");
            prepStatement.setInt(1, user_id);
            rs = prepStatement.executeQuery();

            if (rs.next())
                cart_id = rs.getInt("cart_id");

            // now that we have the cart id, we can begin adding to this users cart
            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        // to add a product to cart, we need to first check if it is already in that cart
                        // if it is, we will simply update the quantity of product attribute++, if it is not
                        // then we will insert it

                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(0));
                        rs = prepStatement.executeQuery();

                        // if this product is already in the cart, then just update the qty attr
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity++;
                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(0));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();
                        }
                        // else, the product is not in the cart, so add it with a quantity of 1
                        else {
                            prepStatement = con.prepareStatement("INSERT INTO CONTAINS VALUES(?, ?, 1)");
                            prepStatement.setInt(1, cart_id);
                            prepStatement.setInt(2, allProductIDs.get(0));

                            if (prepStatement.executeUpdate() == 1) {
                                updateQuantityNumbers();
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });


            // action event for second add to cart button
            addToCartButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        // to add a product to cart, we need to first check if it is already in that cart
                        // if it is, we will simply update the quantity of product attribute++, if it is not
                        // then we will insert it

                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(1));
                        rs = prepStatement.executeQuery();

                        // if this product is already in the cart, then just update the qty attr
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity++;
                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(1));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }
                        // else, the product is not in the cart, so add it with a quantity of 1
                        else {
                            prepStatement = con.prepareStatement("INSERT INTO CONTAINS VALUES(?, ?, 1)");
                            prepStatement.setInt(1, cart_id);
                            prepStatement.setInt(2, allProductIDs.get(1));

                            if (prepStatement.executeUpdate() == 1) {
                                updateQuantityNumbers();
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
            // action event for third add to cart button
            addToCartButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        // to add a product to cart, we need to first check if it is already in that cart
                        // if it is, we will simply update the quantity of product attribute++, if it is not
                        // then we will insert it

                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(2));
                        rs = prepStatement.executeQuery();

                        // if this product is already in the cart, then just update the qty attr
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity++;
                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(2));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }
                        // else, the product is not in the cart, so add it with a quantity of 1
                        else {
                            prepStatement = con.prepareStatement("INSERT INTO CONTAINS VALUES(?, ?, 1)");
                            prepStatement.setInt(1, cart_id);
                            prepStatement.setInt(2, allProductIDs.get(2));

                            if (prepStatement.executeUpdate() == 1) {
                                updateQuantityNumbers();
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });


            // action event for fourth add to cart button
            addToCartButton3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {

                        // to add a product to cart, we need to first check if it is already in that cart
                        // if it is, we will simply update the quantity of product attribute++, if it is not
                        // then we will insert it

                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(3));
                        rs = prepStatement.executeQuery();

                        // if this product is already in the cart, then just update the qty attr
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity++;
                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(3));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }
                        // else, the product is not in the cart, so add it with a quantity of 1
                        else {
                            prepStatement = con.prepareStatement("INSERT INTO CONTAINS VALUES(?, ?, 1)");
                            prepStatement.setInt(1, cart_id);
                            prepStatement.setInt(2, allProductIDs.get(3));

                            if (prepStatement.executeUpdate() == 1) {
                                updateQuantityNumbers();
                            }

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });


            /* ------- now to add the action listener for remove from cart buttons ------- */

            removeFromCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // remove from cart works by removing the item from cart completely if the quantity is 0
                    // otherwise, it just decreased the quantity_of_product

                    try {
                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(0));
                        rs = prepStatement.executeQuery();

                        // if the item is in the cart, we simply decrement current quantity
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity--;

                            // if current quantity becomes 0, we delete the whole tuple
                            if (currentQuantity == 0) {
                                prepStatement = con.prepareStatement("DELETE FROM CONTAINS WHERE cart_id=? and product_id=?");
                                prepStatement.setInt(1, cart_id);
                                prepStatement.setInt(2, allProductIDs.get(0));
                                prepStatement.executeUpdate();
                                updateQuantityNumbers();
                                return;
                            }

                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(0));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }

                        // if the item is not in the cart
                        else {
                            JOptionPane.showMessageDialog(null, "This item is not in your cart!");

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            });

            removeFromCartButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // remove from cart works by removing the item from cart completely if the quantity is 0
                    // otherwise, it just decreased the quantity_of_product

                    try {
                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(1));
                        rs = prepStatement.executeQuery();

                        // if the item is in the cart, we simply decrement current quantity
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity--;

                            // if current quantity becomes 0, we delete the whole tuple
                            if (currentQuantity == 0) {
                                prepStatement = con.prepareStatement("DELETE FROM CONTAINS WHERE cart_id=? and product_id=?");
                                prepStatement.setInt(1, cart_id);
                                prepStatement.setInt(2, allProductIDs.get(1));
                                prepStatement.executeUpdate();
                                updateQuantityNumbers();
                                return;
                            }

                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(1));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }

                        // if the item is not in the cart
                        else {
                            JOptionPane.showMessageDialog(null, "This item is not in your cart!");

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            });

            removeFromCartButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // remove from cart works by removing the item from cart completely if the quantity is 0
                    // otherwise, it just decreased the quantity_of_product

                    try {
                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(2));
                        rs = prepStatement.executeQuery();

                        // if the item is in the cart, we simply decrement current quantity
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity--;

                            // if current quantity becomes 0, we delete the whole tuple
                            if (currentQuantity == 0) {
                                prepStatement = con.prepareStatement("DELETE FROM CONTAINS WHERE cart_id=? and product_id=?");
                                prepStatement.setInt(1, cart_id);
                                prepStatement.setInt(2, allProductIDs.get(2));
                                prepStatement.executeUpdate();
                                updateQuantityNumbers();
                                return;
                            }

                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(2));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }

                        // if the item is not in the cart
                        else {
                            JOptionPane.showMessageDialog(null, "This item is not in your cart!");

                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            });

            removeFromCartButton3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // remove from cart works by removing the item from cart completely if the quantity is 0
                    // otherwise, it just decreased the quantity_of_product

                    try {
                        prepStatement = con.prepareStatement("SELECT * FROM CONTAINS WHERE cart_id=? and product_id=?");
                        prepStatement.setInt(1, cart_id);
                        prepStatement.setInt(2, allProductIDs.get(3));
                        rs = prepStatement.executeQuery();

                        // if the item is in the cart, we simply decrement current quantity
                        if (rs.next()) {
                            int currentQuantity = rs.getInt("quantity_of_product");
                            currentQuantity--;

                            // if current quantity becomes 0, we delete the whole tuple
                            if (currentQuantity == 0) {
                                prepStatement = con.prepareStatement("DELETE FROM CONTAINS WHERE cart_id=? and product_id=?");
                                prepStatement.setInt(1, cart_id);
                                prepStatement.setInt(2, allProductIDs.get(3));
                                prepStatement.executeUpdate();
                                updateQuantityNumbers();
                                return;
                            }

                            prepStatement = con.prepareStatement("UPDATE CONTAINS SET quantity_of_product=?" +
                                    "WHERE cart_id=? and product_id=?");
                            prepStatement.setInt(1, currentQuantity);
                            prepStatement.setInt(2, cart_id);
                            prepStatement.setInt(3, allProductIDs.get(3));
                            prepStatement.executeUpdate();
                            updateQuantityNumbers();

                        }

                        // if the item is not in the cart
                        else {
                            JOptionPane.showMessageDialog(null, "This item is not in your cart!");
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }
            });

            updateQuantityNumbers();

            logoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.updatePanel(new LoginForm().getPanel());
                }
            });

            viewCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.updatePanel(new CartView(cart_id, user_id, false).getPanel());

                }
            });



        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void updateQuantityNumbers() throws Exception {
        // we need to get the count of each product
        prepStatement = con.prepareStatement("SELECT quantity_of_product FROM CONTAINS WHERE product_id=?");

        for (int i = 0; i < allProductIDs.size(); i++) {
            prepStatement.setInt(1, allProductIDs.get(i));
            rs = prepStatement.executeQuery();

            if (rs.next()) {
                int num = rs.getInt("quantity_of_product");

                if (allProductIDs.get(i) == 1)
                    p1qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 2)
                    p2qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 3)
                    p3qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 4)
                    p4qty.setText("Qty: " + num);
            } else {
                int num = 0;
                if (allProductIDs.get(i) == 1)
                    p1qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 2)
                    p2qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 3)
                    p3qty.setText("Qty: " + num);
                else if (allProductIDs.get(i) == 4)
                    p4qty.setText("Qty: " + num);

            }
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
        customerViewRootPanel = new JPanel();
        customerViewRootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 5, new Insets(0, 0, 0, 0), -1, -1));
        welcomeMessage = new JLabel();
        welcomeMessage.setText("Welcome to our store");
        customerViewRootPanel.add(welcomeMessage, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product1Label = new JLabel();
        product1Label.setText("Product 1");
        customerViewRootPanel.add(product1Label, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addToCartButton = new JButton();
        addToCartButton.setText("Add to Cart");
        customerViewRootPanel.add(addToCartButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeFromCartButton = new JButton();
        removeFromCartButton.setText("Remove from Cart");
        customerViewRootPanel.add(removeFromCartButton, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product2Label = new JLabel();
        product2Label.setText("Product 2");
        customerViewRootPanel.add(product2Label, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addToCartButton1 = new JButton();
        addToCartButton1.setText("Add to Cart");
        customerViewRootPanel.add(addToCartButton1, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeFromCartButton1 = new JButton();
        removeFromCartButton1.setText("Remove from Cart");
        customerViewRootPanel.add(removeFromCartButton1, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product3Label = new JLabel();
        product3Label.setText("Product 3");
        customerViewRootPanel.add(product3Label, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addToCartButton2 = new JButton();
        addToCartButton2.setText("Add to Cart");
        customerViewRootPanel.add(addToCartButton2, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeFromCartButton2 = new JButton();
        removeFromCartButton2.setText("Remove from Cart");
        customerViewRootPanel.add(removeFromCartButton2, new com.intellij.uiDesigner.core.GridConstraints(6, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        product4Label = new JLabel();
        product4Label.setText("Product 4");
        customerViewRootPanel.add(product4Label, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addToCartButton3 = new JButton();
        addToCartButton3.setText("Add to Cart");
        customerViewRootPanel.add(addToCartButton3, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeFromCartButton3 = new JButton();
        removeFromCartButton3.setText("Remove from Cart");
        customerViewRootPanel.add(removeFromCartButton3, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p1qty = new JLabel();
        p1qty.setText("Cart Qty: 0");
        customerViewRootPanel.add(p1qty, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p2qty = new JLabel();
        p2qty.setText("Cart Qty: 0");
        customerViewRootPanel.add(p2qty, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p3qty = new JLabel();
        p3qty.setText("Cart Qty: 0");
        customerViewRootPanel.add(p3qty, new com.intellij.uiDesigner.core.GridConstraints(6, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        p4qty = new JLabel();
        p4qty.setText("Cart Qty: 0");
        customerViewRootPanel.add(p4qty, new com.intellij.uiDesigner.core.GridConstraints(8, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Logout");
        customerViewRootPanel.add(logoutButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewCartButton = new JButton();
        viewCartButton.setText("View Cart");
        customerViewRootPanel.add(viewCartButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        customerViewRootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        customerViewRootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return customerViewRootPanel;
    }

}
