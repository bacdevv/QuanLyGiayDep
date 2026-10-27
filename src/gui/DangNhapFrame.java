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
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;
        addRow(form, c, 0, "Tên đăng nhập:", username);
        addRow(form, c, 1, "Mật khẩu:", password);
        JButton login = new JButton("Đăng nhập");
        login.addActionListener(e -> login());
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.EAST;
        form.add(login, c);
        add(form);
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
