package gui;

import connectDB.ConnectDB;
import entity.NhanVien;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Dimension;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

// Main la cua so chinh. Moi GUI cu duoc dua vao tab, khong can sua giao dien cua nhom.
public class Main extends JFrame {

    private final NhanVien nhanVien;
    private final JTabbedPane tabChinh = new JTabbedPane();
    private JTabbedPane tabBanHang, tabDoiTra;
    private ChiTietHoaDon_GUI danhSachHoaDonGUI;
    private ChiTietDoiTra_GUI danhSachDoiTraGUI;
    private final JLabel[] chiSo = new JLabel[5];
    private JLabel lblTrangThai;
    private DefaultTableModel modelSanPhamBanChay;
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
        tieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        dauTrang.add(tieuDe, BorderLayout.WEST);

        JPanel benPhai = new JPanel();
        JLabel lblNhanVien = new JLabel("Nhân viên: " + nhanVien.getTenNhanVien());
        lblNhanVien.setFont(new Font("Arial", Font.BOLD, 15));
        benPhai.add(lblNhanVien);
        JButton btnDangXuat = new JButton("Đăng xuất");
        btnDangXuat.setFont(new Font("Arial", Font.PLAIN, 14));
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

        tabChinh.setFont(new Font("Arial", Font.BOLD, 16));
        tabChinh.addTab("Thống kê", taoThongKe());
        tabChinh.addTab("Bán hàng", tabBanHang = taoTabBanHang());
        tabChinh.addTab("Đổi / Trả", tabDoiTra = taoTabDoiTra());
        tabChinh.addTab("Nhập hàng", taoTabNhapHang());
        tabChinh.addTab("Sản phẩm", taoTabSanPham());
        tabChinh.addChangeListener(e -> {
            if (tabChinh.getSelectedIndex() == 0) {
                napThongKe();
            }
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
        // Bảng xếp hạng trực tiếp trong tab Thống kê, không tạo class mới.
        modelSanPhamBanChay = new DefaultTableModel(
                new String[] { "Hạng", "Mã sản phẩm", "Tên sản phẩm", "Số lượng bán ròng" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable bangBanChay = new JTable(modelSanPhamBanChay);
        bangBanChay.setRowHeight(28);
        bangBanChay.setAutoCreateRowSorter(true);
        JPanel pnlBanChay = new JPanel(new BorderLayout());
        pnlBanChay.setBorder(BorderFactory.createTitledBorder("TOP 10 SẢN PHẨM BÁN CHẠY"));
        pnlBanChay.add(new JScrollPane(bangBanChay), BorderLayout.CENTER);
        pnlBanChay.setPreferredSize(new Dimension(0, 270));
        JPanel noiDung = new JPanel(new BorderLayout(0, 15));
        noiDung.add(bang, BorderLayout.CENTER);
        noiDung.add(pnlBanChay, BorderLayout.SOUTH);
        trang.add(noiDung, BorderLayout.CENTER);
        lblTrangThai = new JLabel("Số liệu lấy trực tiếp từ SQL Server");
        trang.add(lblTrangThai, BorderLayout.SOUTH);
        return trang;
    }

    private void napThongKe() {
        if (Boolean.getBoolean("app.preview")) {
            return;
        }
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
            // Gộp tất cả size/màu của cùng một sản phẩm. Hóa đơn đổi size
            // đã cộng vào doanh số, nên trừ các dòng đã đổi/trả để không đếm đôi.
            String sqlBanChay = "SELECT TOP (10) sp.maSP, sp.tenSP, "
                    + "SUM(x.soLuong) AS soLuongBanRong "
                    + "FROM ("
                    + " SELECT ctsp.maSP, CAST(cthd.soLuong AS BIGINT) AS soLuong "
                    + " FROM ChiTietHoaDon cthd "
                    + " JOIN ChiTietSanPham ctsp ON cthd.maCTSP = ctsp.maCTSP "
                    + " UNION ALL "
                    + " SELECT ctsp.maSP, -CAST(ctdt.soLuong AS BIGINT) AS soLuong "
                    + " FROM ChiTietDoiTra ctdt "
                    + " JOIN ChiTietSanPham ctsp ON ctdt.maCTSP = ctsp.maCTSP "
                    + ") x JOIN SanPham sp ON x.maSP = sp.maSP "
                    + "GROUP BY sp.maSP, sp.tenSP "
                    + "HAVING SUM(x.soLuong) > 0 "
                    + "ORDER BY soLuongBanRong DESC, sp.maSP";
            modelSanPhamBanChay.setRowCount(0);
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sqlBanChay)) {
                int hang = 1;
                while (rs.next()) {
                    modelSanPhamBanChay.addRow(new Object[] { hang++, rs.getString("maSP"),
                            rs.getString("tenSP"), rs.getLong("soLuongBanRong") });
                }
            }
        } catch (SQLException e) {
            for (JLabel label : chiSo) {
                label.setText("–");
            }
            modelSanPhamBanChay.setRowCount(0);
            lblTrangThai.setText("Chưa tải được thống kê từ SQL Server: " + e.getMessage());
        }
    }

    private JTabbedPane taoTabBanHang() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 15));
        themTrang(tabs, "Lập hóa đơn", () -> {
            HoaDon_GUI gui = new HoaDon_GUI(nhanVien);
            gui.setMoChiTiet(this::hienChiTietHoaDon);
            return gui;
        });
        // Tab nay la danh sach truc tiep, khong can nhap ma HD de mo.
        themTrang(tabs, "Danh sách hóa đơn", () -> {
            danhSachHoaDonGUI = new ChiTietHoaDon_GUI();
            return danhSachHoaDonGUI;
        });
        tabs.addChangeListener(e -> {
            napTab(tabs);
            if (tabs.getSelectedIndex() == 1 && danhSachHoaDonGUI != null)
                danhSachHoaDonGUI.taiLaiDanhSach();
        });
        return tabs;
    }

    private JTabbedPane taoTabDoiTra() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 15));
        themTrang(tabs, "Phiếu đổi/trả", () -> {
            PhieuDoiTra_GUI gui = new PhieuDoiTra_GUI(nhanVien);
            gui.setMoChiTiet(this::hienChiTietDoiTra);
            return gui;
        });
        // Tab nay la danh sach truc tiep, khong can nhap ma PDT de mo.
        themTrang(tabs, "Danh sách đổi trả", () -> {
            danhSachDoiTraGUI = new ChiTietDoiTra_GUI();
            return danhSachDoiTraGUI;
        });
        tabs.addChangeListener(e -> {
            napTab(tabs);
            if (tabs.getSelectedIndex() == 1 && danhSachDoiTraGUI != null)
                danhSachDoiTraGUI.taiLaiDanhSach();
        });
        return tabs;
    }

    public void hienChiTietHoaDon(String maHD) {
        tabChinh.setSelectedComponent(tabBanHang);
        tabBanHang.setSelectedIndex(1);
        if (danhSachHoaDonGUI != null)
            danhSachHoaDonGUI.chonChungTu(maHD);
    }

    public void hienChiTietDoiTra(String maPDT) {
        tabChinh.setSelectedComponent(tabDoiTra);
        tabDoiTra.setSelectedIndex(1);
        if (danhSachDoiTraGUI != null)
            danhSachDoiTraGUI.chonChungTu(maPDT);
    }

    private JTabbedPane taoTabNhapHang() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 15));
        themTrang(tabs, "Phiếu nhập", PhieuNhap_GUI::new);

        themTrang(tabs, "Nhà cung cấp", NhaCungCap_GUI::new);
        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    private JTabbedPane taoTabSanPham() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.PLAIN, 15));
        themTrang(tabs, "Sản phẩm", SanPham_GUI::new);

        tabs.addChangeListener(e -> napTab(tabs));
        return tabs;
    }

    // Tao tab rong truoc, chi khoi tao GUI khi nhan tab: tranh tai SQL dong loat.
    private void themTrang(JTabbedPane tabs, String ten, Supplier<JFrame> taoGUI) {
        JPanel trang = new JPanel(new BorderLayout());
        trang.putClientProperty("taoGUI", taoGUI);
        tabs.addTab(ten, trang);
    }

    @SuppressWarnings("unchecked")
    private void napTab(JTabbedPane tabs) {
        if (!(tabs.getSelectedComponent() instanceof JPanel)) {
            return;
        }
        JPanel trang = (JPanel) tabs.getSelectedComponent();
        Supplier<JFrame> taoGUI = (Supplier<JFrame>) trang.getClientProperty("taoGUI");
        if (taoGUI == null) {
            return;
        }
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
        for (JFrame gui : cacGiaoDien) {
            gui.dispose();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NhanVien_GUI::new);
    }
}
