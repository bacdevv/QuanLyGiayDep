package gui;

import bus.DangNhapBUS;
import entity.TaiKhoan;

import javax.swing.*;
import java.awt.*;

public class DangNhapFrame extends JFrame {
    private final JTextField username = new JTextField(20);
    private final JPasswordField password = new JPasswordField(20);
    private final DangNhapBUS bus = new DangNhapBUS();

    public DangNhapFrame() {
        setTitle("Quản lý giày dép - Đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(242, 245, 249));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                BorderFactory.createEmptyBorder(28, 38, 28, 38)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        JLabel logo = new JLabel("GIÀY DÉP", SwingConstants.CENTER);
        logo.setForeground(new Color(31, 78, 121));
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.fill = GridBagConstraints.HORIZONTAL;
        card.add(logo, c);
        JLabel subtitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        subtitle.setForeground(new Color(92, 109, 128));
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        c.gridy = 1;
        card.add(subtitle, c);
        c.gridwidth = 1; c.fill = GridBagConstraints.NONE;
        addRow(card, c, 2, "Tên đăng nhập:", username);
        addRow(card, c, 3, "Mật khẩu:", password);
        JButton login = new JButton("Đăng nhập");
        login.setPreferredSize(new Dimension(150, 34));
        login.setBackground(new Color(49, 112, 173));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        login.addActionListener(e -> login());
        c.gridx = 0; c.gridy = 4; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        card.add(login, c);
        add(card);
        getRootPane().setDefaultButton(login);
        pack();
        setLocationRelativeTo(null);
    }

    private static void addRow(JPanel panel, GridBagConstraints c, int row, String label, JComponent input) {
        c.gridx = 0; c.gridy = row;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        panel.add(input, c);
    }

    private void login() {
        try {
            TaiKhoan account = bus.login(username.getText(), new String(password.getPassword()));
            dispose();
            new MainFrame(account).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }
}
