package gui;

import entity.TaiKhoan;
import util.SessionContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(TaiKhoan account) {
        setTitle("Quản lý giày dép");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(720, 420));
        JLabel welcome = new JLabel("Xin chào " + account.getTenNhanVien() + " (" + account.getVaiTro() + ")", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 20f));
        add(welcome, BorderLayout.CENTER);

        JMenuBar bar = new JMenuBar();
        JMenu system = new JMenu("Hệ thống");
        JMenuItem accounts = new JMenuItem("Tài khoản");
        JMenuItem statistics = new JMenuItem("Thống kê");
        JMenuItem logout = new JMenuItem("Đăng xuất");
        accounts.setEnabled(SessionContext.isManager());
        statistics.setEnabled(SessionContext.isManager());
        accounts.addActionListener(e -> new TaiKhoanFrame(this).setVisible(true));
        statistics.addActionListener(e -> new ThongKeFrame(this).setVisible(true));
        logout.addActionListener(e -> {
            SessionContext.logout();
            dispose();
            new DangNhapFrame().setVisible(true);
        });
        system.add(accounts);
        system.add(statistics);
        system.addSeparator();
        system.add(logout);
        bar.add(system);
        setJMenuBar(bar);
        setLocationRelativeTo(null);
    }
}
