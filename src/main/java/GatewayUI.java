import javax.swing.*;
import java.awt.*;

public class GatewayUI extends JFrame {
    private JLabel statusLabel;
    private JTextArea logArea;

    public GatewayUI() {
        super("Gateway UI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        statusLabel = new JLabel("Gateway is running");
        contentPane.add(statusLabel, BorderLayout.NORTH);

        logArea = new JTextArea(20, 60);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void Status(String status) {
        statusLabel.setText(status);
    }

    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        GatewayUI gatewayUI = new GatewayUI();
        gatewayUI.log("Gateway started.");
    }
}
