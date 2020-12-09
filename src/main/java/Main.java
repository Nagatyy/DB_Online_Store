import javax.swing.*;

public class Main {
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Online Store");
        frame.setTitle("Online Store");
        frame.setSize(650, 650);

        frame.setContentPane(new LoginForm().getPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void updatePanel(JPanel panel){
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
