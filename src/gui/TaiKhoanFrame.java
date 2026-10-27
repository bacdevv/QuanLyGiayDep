package gui;

import bus.TaiKhoanBUS;
import dao.TaiKhoanDAO;
import entity.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaiKhoanFrame extends JPanel {
    private final TaiKhoanBUS bus = new TaiKhoanBUS();
    private final JTextField id = new JTextField();
    private final JComboBox<TaiKhoanDAO.EmployeeOption> employee = new JComboBox<>();
    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JComboBox<String> role = new JComboBox<>(new String[]{"QUAN_LY", "BAN_HANG"});
    private final JCheckBox active = new JCheckBox("Hoạt động", true);
    private final JTextField search = new JTextField();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã TK", "Nhân viên", "Tên đăng nhập", "Vai trò", "Hoạt động"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private List<TaiKhoan> accounts = List.of();

    public TaiKhoanFrame() {
        super(new BorderLayout());
        build();
        refresh();
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(14, 18, 18, 18));
        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setForeground(new Color(70, 127, 177));
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        root.add(title, BorderLayout.NORTH);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Tìm kiếm tài khoản"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        top.add(new JLabel("Tên đăng nhập / nhân viên:"), BorderLayout.WEST);
        top.add(search, BorderLayout.CENTER);
        JButton find = new JButton("Tìm");
        find.addActionListener(e -> refresh());
        top.add(find, BorderLayout.EAST);

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(new Color(211, 226, 239));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) fill(accounts.get(table.getSelectedRow()));
        });
        JPanel list = new JPanel(new BorderLayout(0, 8));
        list.setOpaque(false);
        list.add(top, BorderLayout.NORTH);
        list.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(2, 6, 10, 8));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin tài khoản"),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        form.add(labeled("Mã TK", id)); form.add(labeled("Nhân viên đang làm", employee));
        form.add(labeled("Tên đăng nhập", username)); form.add(labeled("Mật khẩu mới", password));
        form.add(labeled("Vai trò", role)); form.add(active);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Thêm"), update = new JButton("Sửa"), delete = new JButton("Xóa"), clear = new JButton("Làm mới");
        add.addActionListener(e -> save(true));
        update.addActionListener(e -> save(false));
        delete.addActionListener(e -> remove(table));
        clear.addActionListener(e -> { clearForm(); refresh(); });
        buttons.add(add); buttons.add(update); buttons.add(delete); buttons.add(clear);
        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER); south.add(buttons, BorderLayout.SOUTH);
        root.add(list, BorderLayout.CENTER);
        root.add(south, BorderLayout.SOUTH);
        setBackground(new Color(245, 247, 250));
        add(root);
    }

    private static JPanel labeled(String label, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(2, 2));
        p.add(new JLabel(label), BorderLayout.NORTH); p.add(input, BorderLayout.CENTER);
        return p;
    }

    private void save(boolean creating) {
        try {
            TaiKhoan account = readForm();
            if (creating) bus.create(account, new String(password.getPassword()));
            else bus.update(account, new String(password.getPassword()));
            JOptionPane.showMessageDialog(this, creating ? "Đã tạo tài khoản" : "Đã cập nhật tài khoản");
            clearForm(); refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể lưu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remove(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản cần xóa"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa tài khoản đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try { bus.delete(accounts.get(row).getMaTK()); clearForm(); refresh(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể xóa", JOptionPane.ERROR_MESSAGE); }
    }

    private TaiKhoan readForm() {
        TaiKhoan a = new TaiKhoan();
        TaiKhoanDAO.EmployeeOption selected = (TaiKhoanDAO.EmployeeOption) employee.getSelectedItem();
        a.setMaTK(id.getText().trim()); a.setMaNhanVien(selected == null ? "" : selected.id());
        a.setTenDangNhap(username.getText().trim()); a.setVaiTro((String) role.getSelectedItem());
        a.setHoatDong(active.isSelected());
        return a;
    }

    private void fill(TaiKhoan a) {
        id.setText(a.getMaTK());
        for (int i = 0; i < employee.getItemCount(); i++) {
            if (employee.getItemAt(i).id().equals(a.getMaNhanVien())) employee.setSelectedIndex(i);
        }
        username.setText(a.getTenDangNhap()); role.setSelectedItem(a.getVaiTro());
        active.setSelected(a.isHoatDong()); password.setText("");
    }

    private void clearForm() {
        id.setText(""); employee.setSelectedItem(null); username.setText(""); password.setText("");
        role.setSelectedIndex(0); active.setSelected(true);
    }

    private void refresh() {
        try {
            employee.removeAllItems();
            for (TaiKhoanDAO.EmployeeOption option : bus.findActiveEmployees()) employee.addItem(option);
            accounts = bus.findAll(search.getText());
            model.setRowCount(0);
            for (TaiKhoan a : accounts) model.addRow(new Object[]{a.getMaTK(), a.getTenNhanVien(),
                    a.getTenDangNhap(), a.getVaiTro(), a.isHoatDong() ? "Có" : "Không"});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể tải dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }
}
