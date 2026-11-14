package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import entity.NhanVien;

// Main la cua so chinh. Moi GUI cu duoc dua vao tab, khong can sua giao dien cua nhom.
public class Main extends JFrame {
    private final NhanVien nhanVien;
    private final JTabbedPane tabChinh = new JTabbedPane();
    private JTabbedPane tabBanHang, tabDoiTra;
    private JPanel noiDungHoaDon, noiDungDoiTra;
    private JTextField txtMaHD, txtMaPDT;
    private final JLabel[] chiSo = new JLabel[5];
    private JLabel lblTrangThai;
    private final ArrayList<JFrame> cacGiaoDien = new ArrayList<>();

    public Main(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
        setTitle("Quản lý bán hàng giày dép");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        JPanel dauTrang = new JPanel(new BorderLayout());
        dauTrang.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel tieuDe = new JLabel("QUẢN LÝ GIÀY DÉP");
        tieuDe.setFont(new Font("Arial", Font.BOLD, 20));
        dauTrang.add(tieuDe, BorderLayout.WEST);

        JPanel benPhai = new JPanel();
        benPhai.add(new JLabel("Nhân viên: " + nhanVien.getTenNhanVien()));
        JButton btnDangXuat = new JButton("Đăng xuất");
        benPhai.add(btnDangXuat);
        btnDangXuat.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn muốn đăng xuất?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                new NhanVien_GUI();
            }
        });
        dauTrang.add(benPhai, BorderLayout.EAST);
        add(dauTrang, BorderLayout.NORTH);

        tabChinh.addTab("Thống kê", taoThongKe());
        tabChinh.addTab("Bán hàng", tabBanHang = taoTabBanHang());
        tabChinh.addTab("Đổi / Trả", tabDoiTra = taoTabDoiTra());
        tabChinh.addTab("Nhập hàng", taoTabNhapHang());
        tabChinh.addTab("Sản phẩm", taoTabSanPham());
        tabChinh.addTab("Nhân viên", taoTabNhanVien());
        tabChinh.addChangeListener(e -> {
            if (tabChinh.getSelectedIndex() == 0)
                napThongKe();
            if (tabChinh.getSelectedComponent() instanceof JTabbedPane) {
                napTab((JTabbedPane) tabChinh.getSelectedComponent());
            }
        });
        add(tabChinh, BorderLayout.CENTER);
        napThongKe();
    }

    private JPanel taoThongKe() {
        JPanel trang = new JPanel(new BorderLayout(15, 15));
        trang.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel tren = new JPanel(new BorderLayout());
        JLabel tieuDe = new JLabel("TỔNG QUAN CỬA HÀNG");
        tieuDe.setFont(new Font("Arial", Font.BOLD, 24));
        tren.add(tieuDe, BorderLayout.WEST);
        JButton btnTaiLai = new JButton("Làm mới thống kê");
        btnTaiLai.addActionListener(e -> napThongKe());
        tren.add(btnTaiLai, BorderLayout.EAST);
        trang.add(tren, BorderLayout.NORTH);

        String[] ten = { "Nhân viên hoạt động", "Số lượng tồn kho", "Đơn hàng đã bán",
                "Doanh thu", "Đơn đổi/trả" };
        JPanel bang = new JPanel(new GridLayout(2, 3, 15, 15));
        for (int i = 0; i < ten.length; i++) {
            JPanel o = new JPanel(new BorderLayout(4, 8));
            o.setBorder(BorderFactory.createTitledBorder(ten[i]));
            chiSo[i] = new JLabel("–", SwingConstants.CENTER);
            chiSo[i].setFont(new Font("Arial", Font.BOLD, 30));
            o.add(chiSo[i], BorderLayout.CENTER);
            bang.add(o);
        }
        trang.add(bang, BorderLayout.CENTER);
        lblTrangThai = new JLabel("Số liệu lấy trực tiếp từ SQL Server");
        trang.add(lblTrangThai, BorderLayout.SOUTH);
        return trang;
    }

    private void napThongKe() {
        if (Boolean.getBoolean("app.preview"))
            return;
        String[] sql = {
                "SELECT COUNT(*) FROM NhanVien WHERE hoatDong = 1",
                "SELECT COALESCE(SUM(soLuongTon),0) FROM ChiTietSanPham",
                "SELECT COUNT(*) FROM HoaDon",
                "SELECT COALESCE(SUM(tongTien),0) FROM HoaDon",
                "SELECT COUNT(*) FROM PhieuDoiTra" };
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            lblTrangThai.setText("Đã cập nhật thống kê");
            for (int i = 0; i < sql.length; i++) {
                try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql[i])) {
                    if (rs.next()) {
                        chiSo[i].setText(i == 3
                                ? NumberFormat.getNumberInstance(new Locale("vi", "VN"))
                                        .format(rs.getBigDecimal(1)) + " đ"
                                : rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            for (JLabel label : chiSo)
                label.setText("–");
            lblTrangThai.setText("Chưa kết nối SQL Server: " + e.getMessage());
        }
    }

    private JTabbedPane taoTabBanHang() {
        JTabbedPane tabs = new JTabbedPane();
        themTrang(tabs, "Lập hóa đơn", () -> {
            HoaDon_GUI gui = new HoaDon_GUI(nhanVien);
            gui.setMoChiTiet(this::hienChiTietHoaDon);
            return gui;
        });
        noiDungHoaDon = taoTrangChiTiet("Mã hóa đơn:", true);
        tabs.addTab("Chi tiết hóa đơn", noiDungHoaDon);
        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    private JTabbedPane taoTabDoiTra() {
        JTabbedPane tabs = new JTabbedPane();
        themTrang(tabs, "Phiếu đổi/trả", () -> {
            PhieuDoiTra_GUI gui = new PhieuDoiTra_GUI(nhanVien);
            gui.setMoChiTiet(this::hienChiTietDoiTra);
            return gui;
        });
        noiDungDoiTra = taoTrangChiTiet("Mã phiếu đổi/trả:", false);
        tabs.addTab("Chi tiết đổi/trả", noiDungDoiTra);
        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    private JPanel taoTrangChiTiet(String nhan, boolean hoaDon) {
        JPanel trang = new JPanel(new BorderLayout(5, 5));
        JPanel tim = new JPanel();
        JTextField txt = new JTextField(18);
        if (hoaDon)
            txtMaHD = txt;
        else
            txtMaPDT = txt;
        JButton btnXem = new JButton("Xem chi tiết");
        tim.add(new JLabel(nhan));
        tim.add(txt);
        tim.add(btnXem);
        trang.add(tim, BorderLayout.NORTH);
        btnXem.addActionListener(e -> {
            String ma = txt.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã chứng từ");
                return;
            }
            JPanel noiDung = hoaDon ? noiDungHoaDon : noiDungDoiTra;
            if (noiDung.getComponentCount() > 1)
                noiDung.remove(1);
            JFrame gui = hoaDon ? new ChiTietHoaDon_GUI(ma) : new ChiTietDoiTra_GUI(ma);
            noiDung.add(duaVaoTab(gui), BorderLayout.CENTER);
            noiDung.revalidate();
            noiDung.repaint();
        });
        return trang;
    }

    public void hienChiTietHoaDon(String maHD) {
        tabChinh.setSelectedComponent(tabBanHang);
        tabBanHang.setSelectedIndex(1);
        txtMaHD.setText(maHD);
        JButton btn = (JButton) ((JPanel) noiDungHoaDon.getComponent(0)).getComponent(2);
        btn.doClick();
    }

    public void hienChiTietDoiTra(String maPDT) {
        tabChinh.setSelectedComponent(tabDoiTra);
        tabDoiTra.setSelectedIndex(1);
        txtMaPDT.setText(maPDT);
        JButton btn = (JButton) ((JPanel) noiDungDoiTra.getComponent(0)).getComponent(2);
        btn.doClick();
    }

    private JTabbedPane taoTabNhapHang() {
        JTabbedPane tabs = new JTabbedPane();
        themTrang(tabs, "Phiếu nhập", PhieuNhap_GUI::new);
        themTrang(tabs, "Chi tiết phiếu nhập", ChiTietPhieuNhap_GUI::new);
        themTrang(tabs, "Nhà cung cấp", NhaCungCap_GUI::new);
        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    private JTabbedPane taoTabSanPham() {
        JTabbedPane tabs = new JTabbedPane();
        themTrang(tabs, "Sản phẩm", SanPham_GUI::new);
        themTrang(tabs, "Chi tiết sản phẩm", ChiTietSanPham_GUI::new);
        themTrang(tabs, "Thương hiệu", ThuongHieu_GUI::new);
        themTrang(tabs, "Danh mục", DanhMuc_GUI::new);
        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    private JPanel taoTabNhanVien() {
        JPanel trang = new JPanel(new BorderLayout(5, 5));
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "Mã nhân viên", "Tên nhân viên", "SĐT", "Hoạt động" }, 0);
        JTable bang = new JTable(model);
        JButton btnTai = new JButton("Tải danh sách nhân viên");
        btnTai.addActionListener(e -> {
            model.setRowCount(0);
            try {
                ConnectDB.getInstance().connect();
                try (Statement st = ConnectDB.getConnection().createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM NhanVien")) {
                    while (rs.next()) {
                        model.addRow(new Object[] { rs.getString("maNhanVien"),
                                rs.getString("tenNhanVien"), rs.getString("soDienThoai"),
                                rs.getBoolean("hoatDong") ? "Có" : "Không" });
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        trang.add(btnTai, BorderLayout.NORTH);
        trang.add(new JScrollPane(bang), BorderLayout.CENTER);
        return trang;
    }

    // Tao tab rong truoc, chi khoi tao GUI khi nhan tab: tranh tai SQL dong loat.
    private void themTrang(JTabbedPane tabs, String ten, Supplier<JFrame> taoGUI) {
        JPanel trang = new JPanel(new BorderLayout());
        trang.putClientProperty("taoGUI", taoGUI);
        tabs.addTab(ten, trang);
    }

    @SuppressWarnings("unchecked")
    private void napTab(JTabbedPane tabs) {
        if (!(tabs.getSelectedComponent() instanceof JPanel))
            return;
        JPanel trang = (JPanel) tabs.getSelectedComponent();
        Supplier<JFrame> taoGUI = (Supplier<JFrame>) trang.getClientProperty("taoGUI");
        if (taoGUI == null)
            return;
        trang.putClientProperty("taoGUI", null);
        try {
            trang.add(duaVaoTab(taoGUI.get()), BorderLayout.CENTER);
            trang.revalidate();
        } catch (RuntimeException e) {
            trang.add(new JLabel("Không thể mở màn hình: " + e.getMessage()), BorderLayout.CENTER);
        }
    }

    // Giữ nguyên JFrame từ code các thành viên, chỉ lấy giao diện của nó đặt vào
    // tab.
    private Container duaVaoTab(JFrame gui) {
        cacGiaoDien.add(gui);
        Container noiDung = gui.getContentPane();
        gui.setContentPane(new JPanel());
        return noiDung;
    }

    @Override
    public void dispose() {
        for (JFrame gui : cacGiaoDien)
            gui.dispose();
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NhanVien_GUI::new);
    }
}
