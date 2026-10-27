package gui;

import entity.TaiKhoan;
import util.SessionContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(TaiKhoan account) {
        setTitle("Quản lý giày dép");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        getContentPane().setBackground(new Color(245, 247, 250));
        add(createHeader(account), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Trang chủ", createDashboard(account));
        if (SessionContext.isManager()) {
            tabs.addTab("Thống kê", new ThongKeFrame());
            tabs.addTab("Tài khoản", new TaiKhoanFrame());
        }
        add(tabs, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private JPanel createHeader(TaiKhoan account) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(61, 65, 72));
        header.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        JLabel brand = new JLabel("GIÀY DÉP");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 25));
        header.add(brand, BorderLayout.WEST);

        JPanel user = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        user.setOpaque(false);
        JLabel name = new JLabel(account.getTenNhanVien() + "  |  " + account.getVaiTro());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton logout = navButton("Đăng xuất", new Color(225, 233, 241), new Color(45, 54, 64));
        logout.addActionListener(e -> {
            SessionContext.logout();
            dispose();
            new DangNhapFrame().setVisible(true);
        });
        user.add(name);
        user.add(logout);
        header.add(user, BorderLayout.EAST);
        return header;
    }

    private JPanel createDashboard(TaiKhoan account) {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 22, 22));

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        JLabel title = new JLabel("TRANG CHỦ", SwingConstants.CENTER);
        title.setForeground(new Color(70, 127, 177));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        content.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(0, 2, 14, 14));
        info.setOpaque(false);
        info.add(infoCard("NHÂN VIÊN ĐĂNG NHẬP", account.getTenNhanVien(), "Tên người đang sử dụng hệ thống"));
        info.add(infoCard("VAI TRÒ", account.getVaiTro(), roleDescription(account.getVaiTro())));
        info.add(infoCard("TRẠNG THÁI TÀI KHOẢN", "Đang hoạt động", "Tài khoản được phép sử dụng"));
        info.add(infoCard("QUYỀN TRUY CẬP", account.getVaiTro().equals("QUAN_LY")
                ? "Quản lý toàn hệ thống" : "Bán hàng và tra cứu", "Theo vai trò đăng nhập"));
        content.add(info, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);
        return root;
    }

    private JPanel infoCard(String label, String value, String description) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 226, 234)),
                BorderFactory.createEmptyBorder(20, 18, 20, 18)));
        JLabel caption = new JLabel(label);
        caption.setForeground(new Color(106, 119, 133));
        caption.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(new Color(38, 64, 87));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JLabel detail = new JLabel(description);
        detail.setForeground(new Color(125, 137, 149));
        detail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(caption, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(detail, BorderLayout.SOUTH);
        return card;
    }

    private static String roleDescription(String role) {
        return "QUAN_LY".equals(role) ? "Quản lý" : "Nhân viên bán hàng";
    }

    private static JButton navButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setFocusPainted(false);
        return button;
    }
}
