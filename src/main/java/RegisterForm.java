import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;
import java.util.Random;


/*
only a customer can register using the register form
*/

public class RegisterForm {
    private JTextField textField1;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JPanel registerRootPanel;
    private JButton registerButton;
    private JButton alreadyHaveAnAccountButton;


    String DBURL = "jdbc:oracle:thin@coeoracle.aus.edu:1521:orcl";
    String DBUSER = "b00072311";
    String DBPASS = "b00072311";

    Connection con;
    Statement statement;
    PreparedStatement prepStatement;
    ResultSet rs;

    public RegisterForm() {
        setupComponents();

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Connect to Oracle Database
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);


            // needed to check if a username is taken
            prepStatement = con.prepareStatement("SELECT username FROM ACCOUNTS WHERE username=?");


        } catch (ClassNotFoundException | SQLException e) {
            JLabel label = new JLabel("SQL Error");
            e.printStackTrace();
            label.setFont(new Font("Arial", Font.BOLD, 18));
        }


    }

    public JPanel getPanel() {
        return registerRootPanel;
    }

    private void setupComponents() {
        alreadyHaveAnAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.updatePanel(new LoginForm().getPanel());
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField1.getText().trim().equals("") || textField2.getText().trim().equals("") || passwordField1.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter All Fields");
                    return;
                }

                try {
                    prepStatement.setString(1, textField1.getText().trim());
                    rs = prepStatement.executeQuery();
                    if (rs.next()) {
                        // then username is taken
                        JOptionPane.showMessageDialog(null, "username is taken");
                        textField1.setText("");
                        textField2.setText("");
                        passwordField1.setText("");
                        return;
                    }
                    // now that we know the username is not taken we can register the user
                    // 1. generate a random customer id
                    Random rand = new Random();
                    int new_id = rand.nextInt(100000);
                    prepStatement = con.prepareStatement("INSERT INTO ACCOUNTS VALUES(?, ?, ?)");
                    prepStatement.setString(1, textField1.getText().trim());
                    prepStatement.setInt(2, new_id);
                    prepStatement.setString(3, passwordField1.getText().trim());
                    // 2. perfrom the insertion into ACCOUNTS
                    prepStatement.executeUpdate();

                    // 3. perform the insertion into CUSTOMERS
                    prepStatement = con.prepareStatement("INSERT INTO CUSTOMERS VALUES(?, ?, ?)");
                    prepStatement.setInt(1, new_id);
                    prepStatement.setString(2, textField2.getText().trim());
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date currentTime = calendar.getTime();
                    prepStatement.setDate(3, new Date(currentTime.getTime()));
                    prepStatement.executeUpdate();

                    // now that a user is created, a cart needs to also be created for them
                    int new_cart_id = rand.nextInt(100000);
                    prepStatement = con.prepareStatement("INSERT INTO CART VALUES(?, ?, ?)");
                    prepStatement.setInt(1, new_cart_id);
                    prepStatement.setInt(2, 0);
                    prepStatement.setInt(3, 0);
                    prepStatement.executeUpdate();

                    // we must also insert into BELONGS_TO
                    prepStatement = con.prepareStatement("INSERT INTO BELONGS_TO VALUES(?, ?)");
                    prepStatement.setInt(1, new_cart_id);
                    prepStatement.setInt(2, new_id);


                    if (prepStatement.executeUpdate() == 1) {
                        JOptionPane.showMessageDialog(null, "Account Created Successfully");
                    } else {
                        JOptionPane.showMessageDialog(null, "Account Creation Failed");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


            }
        });
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
        registerRootPanel = new JPanel();
        registerRootPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Register");
        registerRootPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Username");
        registerRootPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField1 = new JTextField();
        registerRootPanel.add(textField1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Email");
        registerRootPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        registerRootPanel.add(textField2, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Password");
        registerRootPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField1 = new JPasswordField();
        registerRootPanel.add(passwordField1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        registerButton = new JButton();
        registerButton.setText("Register");
        registerRootPanel.add(registerButton, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        alreadyHaveAnAccountButton = new JButton();
        alreadyHaveAnAccountButton.setText("Already have an account? Login");
        registerRootPanel.add(alreadyHaveAnAccountButton, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        registerRootPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        registerRootPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(8, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return registerRootPanel;
    }

}
