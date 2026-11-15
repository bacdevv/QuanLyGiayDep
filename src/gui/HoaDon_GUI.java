
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import dao.HoaDon_DAO;
import entity.ChiTietHoaDon;
import entity.ChiTietSanPham;
import dao.HoaDon_DAO.SanPhamBan;
import entity.HoaDon;
import entity.NhanVien;
import dao.NhanVien_DAO;
import connectDB.ConnectDB;
import entity.KhachHang;

public class HoaDon_GUI extends JFrame
        implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;
    // Thue GTGT co dinh cho hoa don moi.
    private static final BigDecimal THUE_SUAT = HoaDon_DAO.THUE_SUAT;

    private JTextField txtMaHD, txtNgayLap;
    private JTextField txtTienHang, txtThue;
    private JTextField txtTienThue, txtTongTien;
    private JTextField txtTimMaCTSP, txtTimTenSP;
    private JTextField txtMaCTSP, txtSoLuong, txtDonGia;

    private JTextField txtNhanVien;
    private NhanVien nhanVienDangNhap;
    private BigDecimal tongTienDangTinh = BigDecimal.ZERO;
    private JTextField txtKhachHang, txtSoDienThoai;
    private JButton btnThemKhachHang;
    private boolean dangThemKhachHang;
    private KhachHang khachHangDangChon;
    private JComboBox<String> cboThanhToan;

    private JButton btnTim, btnLamMoiSanPham;
    private JButton btnTaoHD, btnLamMoiHD, btnXuatHD;
    private JButton btnThem, btnSuaSoLuong, btnXoa;

    private JTable tableSanPham, tableChiTietHoaDon;
    private DefaultTableModel modelSanPham, modelChiTietHoaDon;

    private HoaDon_DAO hd_dao;
    private boolean daLuu = false;
    private java.util.function.Consumer<String> moChiTiet;

    public void setMoChiTiet(java.util.function.Consumer<String> moChiTiet) {
        this.moChiTiet = moChiTiet;
    }

    public HoaDon_GUI() {
        this(null);
    }

    public HoaDon_GUI(NhanVien nhanVien) {
        this.nhanVienDangNhap = nhanVien;
        hd_dao = new HoaDon_DAO();
        setTitle("Quản lý giày dép - Hóa đơn");
        setSize(1500, 880);
        setMinimumSize(new java.awt.Dimension(1120, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        buildUI();
        try {
            ConnectDB.getInstance().connect();
            docDuLieuSanPham();
        } catch (SQLException e) {
            thongBao("Lỗi tải dữ liệu: " + e.getMessage());
        }
        lamMoiHoaDon();
    }

    private void thongBao(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.ERROR_MESSAGE);
    }

    private void dinhDangNhan(java.awt.Container container) {
        for (java.awt.Component c : container.getComponents()) {
            if (c instanceof JLabel && c.getFont().getSize() < 25)
                c.setFont(new Font("Arial", Font.PLAIN, 16));
            if (c instanceof java.awt.Container)
                dinhDangNhan((java.awt.Container) c);
        }
    }

    private void dinhDangGiaoDien() {
        Font font = new Font("Arial", Font.PLAIN, 16);
        Font fontBang = new Font("Arial", Font.PLAIN, 15);
        for (JTextField txt : new JTextField[] { txtMaHD, txtNgayLap,
                txtSoDienThoai, txtKhachHang, txtMaCTSP, txtSoLuong, txtDonGia,
                txtTimMaCTSP, txtTimTenSP, txtTienHang, txtThue,
                txtTienThue, txtTongTien }) {
            txt.setFont(font);
        }
        cboThanhToan.setFont(font);
        txtNhanVien.setFont(font);
        for (JButton btn : new JButton[] { btnThemKhachHang, btnTim,
                btnLamMoiSanPham, btnThem, btnSuaSoLuong, btnXoa,
                btnTaoHD, btnLamMoiHD, btnXuatHD })
            btn.setFont(font);
        for (JTable table : new JTable[] { tableSanPham, tableChiTietHoaDon }) {
            table.setFont(fontBang);
            table.setRowHeight(32);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        }
        dinhDangNhan(getContentPane());
    }

    private NhanVien layNhanVien() {
        if (nhanVienDangNhap == null)
            throw new IllegalArgumentException("Cần đăng nhập trước khi lập phiếu");
        return nhanVienDangNhap;
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 5));

        JLabel lblTieuDe = new JLabel(
                "QUẢN LÝ GIÀY DÉP - HÓA ĐƠN BÁN HÀNG",
                SwingConstants.CENTER);
        lblTieuDe.setForeground(Color.BLUE);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        add(lblTieuDe, BorderLayout.NORTH);

        JPanel pnlChinh = new JPanel(
                new GridLayout(1, 2, 10, 0));
        add(pnlChinh, BorderLayout.CENTER);

        // ====== THONG TIN HOA DON ======
        JPanel pnlHoaDon = new JPanel(new BorderLayout(5, 5));
        pnlHoaDon.setBorder(
                BorderFactory.createTitledBorder(
                        "Thông tin hóa đơn"));

        JPanel pnlThongTin = new JPanel(
                new GridLayout(5, 2, 5, 5));

        pnlThongTin.add(new JLabel("Mã hóa đơn:"));
        pnlThongTin.add(txtMaHD = new JTextField());

        pnlThongTin.add(new JLabel("Ngày lập:"));
        pnlThongTin.add(txtNgayLap = new JTextField());

        pnlThongTin.add(new JLabel("Nhân viên:"));
        pnlThongTin.add(txtNhanVien = new JTextField(
                nhanVienDangNhap == null ? "Chưa đăng nhập" : nhanVienDangNhap.getTenNhanVien()));
        txtNhanVien.setEditable(false);

        pnlThongTin.add(new JLabel("Số điện thoại KH:"));
        JPanel pnlKhachHang = new JPanel(new BorderLayout(5, 0));
        pnlKhachHang.add(txtSoDienThoai = new JTextField(), BorderLayout.CENTER);
        pnlKhachHang.add(btnThemKhachHang = new JButton("Thêm KH"), BorderLayout.EAST);
        pnlThongTin.add(pnlKhachHang);
        pnlThongTin.add(new JLabel("Tên khách hàng:"));
        pnlThongTin.add(txtKhachHang = new JTextField());
        txtKhachHang.setEditable(false);
        btnThemKhachHang.setEnabled(false);

        txtMaHD.setEditable(false);
        txtNgayLap.setEditable(false);

        pnlHoaDon.add(pnlThongTin, BorderLayout.NORTH);

        // ====== CHI TIET HOA DON ======
        JPanel pnlChiTiet = new JPanel(
                new BorderLayout(5, 5));

        pnlChiTiet.setBorder(
                BorderFactory.createTitledBorder(
                        "Chi tiết hóa đơn"));

        JPanel pnlNhap = new JPanel(
                new GridLayout(3, 2, 5, 5));

        pnlNhap.add(new JLabel("Mã biến thể:"));
        pnlNhap.add(txtMaCTSP = new JTextField());

        pnlNhap.add(new JLabel("Số lượng:"));
        pnlNhap.add(txtSoLuong = new JTextField("1"));

        pnlNhap.add(new JLabel("Đơn giá chưa thuế (VND):"));
        pnlNhap.add(txtDonGia = new JTextField());

        txtMaCTSP.setEditable(false);
        txtDonGia.setEditable(false);

        pnlChiTiet.add(pnlNhap, BorderLayout.NORTH);

        String[] cotChiTiet = {
                "Mã CTSP", "Tên sản phẩm", "Kích cỡ",
                "Màu sắc", "Số lượng", "Đơn giá",
                "Thành tiền"
        };

        modelChiTietHoaDon = new DefaultTableModel(cotChiTiet, 0);

        tableChiTietHoaDon = new JTable(modelChiTietHoaDon);
        tableChiTietHoaDon.setDefaultEditor(
                Object.class, null);
        DinhDangTien.dinhDangCotTien(tableChiTietHoaDon, 5, 6);

        pnlChiTiet.add(
                new JScrollPane(tableChiTietHoaDon),
                BorderLayout.CENTER);

        JPanel pnlNutChiTiet = new JPanel(new FlowLayout());

        btnThem = new JButton("Thêm dòng");
        btnSuaSoLuong = new JButton("Sửa số lượng");
        btnXoa = new JButton("Xóa dòng");

        pnlNutChiTiet.add(btnThem);
        pnlNutChiTiet.add(btnSuaSoLuong);
        pnlNutChiTiet.add(btnXoa);

        pnlChiTiet.add(pnlNutChiTiet, BorderLayout.SOUTH);
        pnlHoaDon.add(pnlChiTiet, BorderLayout.CENTER);

        // ====== TONG TIEN ======
        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        JPanel pnlTien = new JPanel(
                new GridLayout(5, 2, 5, 5));

        pnlTien.add(new JLabel(
                "Tiền hàng chưa thuế (VND):"));
        pnlTien.add(txtTienHang = new JTextField());

        pnlTien.add(new JLabel("Thuế (%):"));
        pnlTien.add(txtThue = new JTextField(THUE_SUAT.toPlainString()));
        txtThue.setEditable(false);

        pnlTien.add(new JLabel("Tiền thuế (VND):"));
        pnlTien.add(txtTienThue = new JTextField());

        pnlTien.add(new JLabel(
                "Tổng thanh toán (VND):"));
        pnlTien.add(txtTongTien = new JTextField());

        pnlTien.add(new JLabel(
                "Phương thức thanh toán:"));

        cboThanhToan = new JComboBox<>(
                new String[] { "Tiền mặt", "Chuyển khoản" });
        pnlTien.add(cboThanhToan);

        txtTienHang.setEditable(false);
        txtTienThue.setEditable(false);
        txtTongTien.setEditable(false);

        pnlDuoi.add(pnlTien, BorderLayout.CENTER);

        JPanel pnlNut = new JPanel(new FlowLayout());

        btnTaoHD = new JButton("Tạo hóa đơn");
        btnLamMoiHD = new JButton("Làm mới");
        btnXuatHD = new JButton("Xem / In hóa đơn");

        pnlNut.add(btnTaoHD);
        pnlNut.add(btnLamMoiHD);
        pnlNut.add(btnXuatHD);

        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        pnlHoaDon.add(pnlDuoi, BorderLayout.SOUTH);

        pnlChinh.add(pnlHoaDon);

        // ====== DANH SACH SAN PHAM ======
        JPanel pnlSanPham = new JPanel(
                new BorderLayout(5, 5));

        pnlSanPham.setBorder(
                BorderFactory.createTitledBorder(
                        "Chọn sản phẩm"));

        JPanel pnlTim = new JPanel(
                new GridLayout(3, 2, 5, 5));

        pnlTim.add(new JLabel("Mã biến thể:"));
        pnlTim.add(txtTimMaCTSP = new JTextField());

        pnlTim.add(new JLabel("Tên giày/dép:"));
        pnlTim.add(txtTimTenSP = new JTextField());

        btnTim = new JButton("Tìm");
        btnLamMoiSanPham = new JButton("Làm mới danh sách");

        pnlTim.add(btnTim);
        pnlTim.add(btnLamMoiSanPham);

        pnlSanPham.add(pnlTim, BorderLayout.NORTH);

        String[] cotSanPham = {
                "Mã biến thể", "Tên giày/dép", "Size",
                "Màu", "Giá chưa thuế", "Tồn kho"
        };

        modelSanPham = new DefaultTableModel(cotSanPham, 0);

        tableSanPham = new JTable(modelSanPham);
        tableSanPham.setDefaultEditor(Object.class, null);
        DinhDangTien.dinhDangCotTien(tableSanPham, 4);

        pnlSanPham.add(new JScrollPane(tableSanPham),
                BorderLayout.CENTER);

        pnlChinh.add(pnlSanPham);

        // ====== DANG KY SU KIEN ======
        btnThemKhachHang.addActionListener(this);
        btnTim.addActionListener(this);
        btnLamMoiSanPham.addActionListener(this);
        btnThem.addActionListener(this);
        btnSuaSoLuong.addActionListener(this);
        btnXoa.addActionListener(this);
        btnTaoHD.addActionListener(this);
        btnLamMoiHD.addActionListener(this);
        btnXuatHD.addActionListener(this);

        tableSanPham.addMouseListener(this);
        tableChiTietHoaDon.addMouseListener(this);

        dinhDangGiaoDien();
        txtSoDienThoai.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                timKhachHangTheoSDT();
            }

            public void removeUpdate(DocumentEvent e) {
                timKhachHangTheoSDT();
            }

            public void changedUpdate(DocumentEvent e) {
                timKhachHangTheoSDT();
            }
        });

    }

    // ====== DOC DU LIEU CSDL ======

    // So dien thoai da co -> tu dong hien thi ten, chua co -> nhan Them KH.
    private void timKhachHangTheoSDT() {
        khachHangDangChon = null;
        dangThemKhachHang = false;
        txtKhachHang.setEditable(false);
        btnThemKhachHang.setText("Thêm KH");
        btnThemKhachHang.setEnabled(false);
        txtKhachHang.setText("");

        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("0[0-9]{9}"))
            return;
        try {
            khachHangDangChon = hd_dao.getKhachHangTheoSoDienThoai(sdt);
            if (khachHangDangChon != null) {
                txtKhachHang.setText(khachHangDangChon.getTenKH());
            } else {
                btnThemKhachHang.setEnabled(true);
                txtKhachHang.setToolTipText("Nhấn Thêm KH để nhập tên khách mới");
            }
        } catch (SQLException e) {
            thongBao("Không tra cứu được khách hàng: " + e.getMessage());
        }
    }

    private void themKhachHang() throws SQLException {
        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("0[0-9]{9}"))
            throw new IllegalArgumentException("SĐT phải có 10 chữ số và bắt đầu bằng 0");
        if (khachHangDangChon != null)
            throw new IllegalArgumentException("Khách hàng đã tồn tại");

        if (!dangThemKhachHang) {
            dangThemKhachHang = true;
            txtKhachHang.setEditable(true);
            txtKhachHang.requestFocusInWindow();
            btnThemKhachHang.setText("Lưu KH");
            return;
        }

        String ten = txtKhachHang.getText().trim();
        if (ten.isEmpty() || ten.length() > 100)
            throw new IllegalArgumentException("Tên khách hàng từ 1 đến 100 ký tự");
        khachHangDangChon = hd_dao.themKhachHang(sdt, ten);
        txtKhachHang.setText(khachHangDangChon.getTenKH());
        txtKhachHang.setEditable(false);
        dangThemKhachHang = false;
        btnThemKhachHang.setText("Thêm KH");
        btnThemKhachHang.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Đã thêm khách hàng "
                + khachHangDangChon.getMaKH());
    }

    private void docDuLieuSanPham() throws SQLException {
        modelSanPham.setRowCount(0);

        ArrayList<SanPhamBan> ds = hd_dao.getDanhSachSanPham(
                txtTimMaCTSP.getText(),
                txtTimTenSP.getText());

        for (SanPhamBan sp : ds) {
            modelSanPham.addRow(new Object[] {
                    sp.getMaCTSP(),
                    sp.getTenSP(),
                    sp.getKichCo(),
                    sp.getMauSac(),
                    sp.getGiaBan(),
                    sp.getSoLuongTon()
            });
        }
    }

    // ====== KIEM TRA SO LUONG ======

    private int laySoLuongNhap() {
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());

            if (soLuong <= 0) {
                throw new NumberFormatException();
            }

            return soLuong;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Số lượng phải là số nguyên dương");
        }
    }

    // ====== THEM SAN PHAM VAO HOA DON ======

    private void themDong() throws SQLException {
        if (daLuu) {
            throw new IllegalArgumentException(
                    "Hóa đơn đã lưu. Hãy tạo hóa đơn mới.");
        }

        int row = tableSanPham.getSelectedRow();

        if (row == -1) {
            throw new IllegalArgumentException(
                    "Vui lòng chọn sản phẩm");
        }

        row = tableSanPham.convertRowIndexToModel(row);

        String maCTSP = modelSanPham.getValueAt(row, 0).toString();
        String tenSP = modelSanPham.getValueAt(row, 1).toString();
        String kichCo = modelSanPham.getValueAt(row, 2).toString();
        String mauSac = modelSanPham.getValueAt(row, 3).toString();

        BigDecimal donGia = (BigDecimal) modelSanPham.getValueAt(row, 4);

        int soLuong = laySoLuongNhap();
        int tonKho = hd_dao.getSoLuongTon(maCTSP);

        // Neu ma da co, cong them so luong
        for (int i = 0; i < modelChiTietHoaDon.getRowCount(); i++) {

            String maCu = modelChiTietHoaDon.getValueAt(i, 0)
                    .toString();

            if (maCu.equalsIgnoreCase(maCTSP)) {
                int slCu = (int) modelChiTietHoaDon.getValueAt(i, 4);

                int slMoi = slCu + soLuong;

                if (slMoi > tonKho) {
                    throw new IllegalArgumentException(
                            "Không đủ tồn kho. Còn: " + tonKho);
                }

                DinhDangTien.kiemTraGioiHan(donGia.multiply(
                        BigDecimal.valueOf(slMoi)), "Thành tiền");
                modelChiTietHoaDon.setValueAt(slMoi, i, 4);
                modelChiTietHoaDon.setValueAt(
                        donGia.multiply(
                                BigDecimal.valueOf(slMoi)),
                        i, 6);

                tinhLaiTien();
                return;
            }
        }

        if (tonKho < soLuong) {
            throw new IllegalArgumentException(
                    "Không đủ tồn kho. Còn: " + tonKho);
        }

        BigDecimal thanhTien = donGia.multiply(
                BigDecimal.valueOf(soLuong));
        DinhDangTien.kiemTraGioiHan(thanhTien, "Thành tiền");

        modelChiTietHoaDon.addRow(new Object[] {
                maCTSP, tenSP, kichCo, mauSac,
                soLuong, donGia, thanhTien
        });

        tinhLaiTien();
    }

    // ====== SUA SO LUONG ======

    private void suaSoLuong() throws SQLException {
        if (daLuu) {
            throw new IllegalArgumentException(
                    "Không thể sửa hóa đơn đã lưu");
        }

        int row = tableChiTietHoaDon.getSelectedRow();

        if (row == -1) {
            throw new IllegalArgumentException(
                    "Chọn dòng cần sửa");
        }

        row = tableChiTietHoaDon.convertRowIndexToModel(row);

        String maCTSP = modelChiTietHoaDon.getValueAt(row, 0)
                .toString();

        int soLuong = laySoLuongNhap();
        int tonKho = hd_dao.getSoLuongTon(maCTSP);

        if (soLuong > tonKho) {
            throw new IllegalArgumentException(
                    "Không đủ tồn kho. Còn: " + tonKho);
        }

        BigDecimal donGia = (BigDecimal) modelChiTietHoaDon.getValueAt(row, 5);

        DinhDangTien.kiemTraGioiHan(donGia.multiply(
                BigDecimal.valueOf(soLuong)), "Thành tiền");
        modelChiTietHoaDon.setValueAt(soLuong, row, 4);
        modelChiTietHoaDon.setValueAt(
                donGia.multiply(
                        BigDecimal.valueOf(soLuong)),
                row, 6);

        tinhLaiTien();
    }

    // ====== XOA DONG ======

    private void xoaDong() {
        if (daLuu) {
            throw new IllegalArgumentException(
                    "Không thể xóa dòng của hóa đơn đã lưu");
        }

        int row = tableChiTietHoaDon.getSelectedRow();

        if (row == -1) {
            throw new IllegalArgumentException(
                    "Chọn dòng cần xóa");
        }

        row = tableChiTietHoaDon.convertRowIndexToModel(row);
        modelChiTietHoaDon.removeRow(row);

        tinhLaiTien();
    }

    // ====== TINH TIEN ======

    private void tinhLaiTien() {
        BigDecimal tienHang = BigDecimal.ZERO;

        for (int i = 0; i < modelChiTietHoaDon.getRowCount(); i++) {
            BigDecimal thanhTien = (BigDecimal) modelChiTietHoaDon.getValueAt(i, 6);
            tienHang = tienHang.add(thanhTien);
        }

        tienHang = tienHang.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tienThue = tienHang.multiply(THUE_SUAT).divide(
                new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal tongTien = tienHang.add(tienThue);

        tongTienDangTinh = tongTien;
        txtTienHang.setText(DinhDangTien.hienThi(tienHang));
        txtTienThue.setText(DinhDangTien.hienThi(tienThue));
        txtTongTien.setText(DinhDangTien.hienThi(tongTien));
        if (!daLuu)
            btnTaoHD.setEnabled(nhanVienDangNhap != null
                    && txtMaHD.getText().matches("HD[0-9]{6}")
                    && tongTien.compareTo(DinhDangTien.GIOI_HAN_SQL) <= 0);
    }

    // ====== TAO HOA DON ======

    private void taoHoaDon() throws SQLException {
        if (daLuu)
            throw new IllegalArgumentException("Hóa đơn đã lưu");
        if (modelChiTietHoaDon.getRowCount() == 0)
            throw new IllegalArgumentException("Hóa đơn chưa có sản phẩm");
        if (!txtMaHD.getText().matches("HD[0-9]{6}"))
            throw new IllegalArgumentException("Chưa cấp được mã hóa đơn");
        DinhDangTien.kiemTraGioiHan(tongTienDangTinh, "Tổng tiền");

        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("0[0-9]{9}"))
            throw new IllegalArgumentException("Bắt buộc nhập SĐT khách hàng hợp lệ");
        KhachHang kh = hd_dao.getKhachHangTheoSoDienThoai(sdt);
        if (kh == null)
            throw new IllegalArgumentException("Khách mới: nhấn Thêm KH và lưu trước khi thanh toán");
        khachHangDangChon = kh;

        BigDecimal thue = THUE_SUAT;
        BigDecimal tong = tongTienDangTinh;
        LocalDateTime ngayLap = LocalDateTime.now();
        HoaDon hd = new HoaDon(txtMaHD.getText().trim(), layNhanVien(),
                khachHangDangChon,
                ngayLap, (String) cboThanhToan.getSelectedItem(), thue, tong);

        ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
        for (int i = 0; i < modelChiTietHoaDon.getRowCount(); i++) {
            String maCTSP = modelChiTietHoaDon.getValueAt(i, 0).toString();
            int soLuong = (int) modelChiTietHoaDon.getValueAt(i, 4);
            BigDecimal gia = (BigDecimal) modelChiTietHoaDon.getValueAt(i, 5);
            DinhDangTien.kiemTraGioiHan(gia, "Đơn giá");
            ds.add(new ChiTietHoaDon(null, new ChiTietSanPham(maCTSP), soLuong, gia));
        }
        String maMoi = hd_dao.create(hd, ds);
        txtMaHD.setText(maMoi);
        txtNgayLap.setText(ngayLap.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        daLuu = true;
        btnTaoHD.setEnabled(false);
        btnThem.setEnabled(false);
        btnSuaSoLuong.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXuatHD.setEnabled(true);
        txtThue.setEditable(false);
        txtSoDienThoai.setEditable(false);
        btnThemKhachHang.setEnabled(false);
        cboThanhToan.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công: " + maMoi);
        docDuLieuSanPham();
    }

    private void lamMoiHoaDon() {
        daLuu = false;
        txtMaHD.setText("Đang cấp mã...");
        txtNgayLap.setText(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        modelChiTietHoaDon.setRowCount(0);
        txtMaCTSP.setText("");
        txtSoLuong.setText("1");
        txtDonGia.setText("");
        txtThue.setText(THUE_SUAT.toPlainString());
        txtSoDienThoai.setEditable(true);
        txtSoDienThoai.setText("");
        khachHangDangChon = null;
        txtKhachHang.setText("");
        txtKhachHang.setEditable(false);
        btnThemKhachHang.setEnabled(false);
        btnThemKhachHang.setText("Thêm KH");
        dangThemKhachHang = false;
        cboThanhToan.setSelectedIndex(0);
        btnTaoHD.setEnabled(true);
        btnThem.setEnabled(true);
        btnSuaSoLuong.setEnabled(true);
        btnXoa.setEnabled(true);
        btnXuatHD.setEnabled(false);
        txtThue.setEditable(false);
        cboThanhToan.setEnabled(true);
        try {
            txtMaHD.setText(hd_dao.taoMaHoaDonMoi());
        } catch (SQLException e) {
            txtMaHD.setText("Lỗi cấp mã");
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Không thể tạo hóa đơn", JOptionPane.ERROR_MESSAGE);
        }
        tinhLaiTien();
    }

    private void xuatHoaDon() {
        if (!daLuu)
            throw new IllegalArgumentException("Cần lưu hóa đơn trước");
        if (moChiTiet != null) moChiTiet.accept(txtMaHD.getText());
        else new ChiTietHoaDon_GUI(txtMaHD.getText()).setVisible(true);
    }

    // ====== XU LY SU KIEN BUTTON ======

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        try {
            if (o == btnThemKhachHang) {
                themKhachHang();
            } else if (o == btnTim) {
                docDuLieuSanPham();

            } else if (o == btnLamMoiSanPham) {
                txtTimMaCTSP.setText("");
                txtTimTenSP.setText("");
                docDuLieuSanPham();

            } else if (o == btnThem) {
                themDong();

            } else if (o == btnSuaSoLuong) {
                suaSoLuong();

            } else if (o == btnXoa) {
                xoaDong();

            } else if (o == btnTaoHD) {
                taoHoaDon();

            } else if (o == btnLamMoiHD) {
                lamMoiHoaDon();

            } else if (o == btnXuatHD) {
                xuatHoaDon();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Thông báo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====== XU LY CLICK TABLE ======

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tableSanPham) {
            int row = tableSanPham.getSelectedRow();

            if (row >= 0) {
                row = tableSanPham.convertRowIndexToModel(row);

                txtMaCTSP.setText(
                        modelSanPham.getValueAt(row, 0)
                                .toString());

                txtDonGia.setText(DinhDangTien.hienThi(
                        (BigDecimal) modelSanPham.getValueAt(row, 4)));

                txtSoLuong.setText("1");
            }

        } else if (e.getSource() == tableChiTietHoaDon) {
            int row = tableChiTietHoaDon.getSelectedRow();

            if (row >= 0) {
                row = tableChiTietHoaDon
                        .convertRowIndexToModel(row);

                txtMaCTSP.setText(
                        modelChiTietHoaDon.getValueAt(row, 0)
                                .toString());

                txtSoLuong.setText(
                        modelChiTietHoaDon.getValueAt(row, 4)
                                .toString());

                txtDonGia.setText(DinhDangTien.hienThi(
                        (BigDecimal) modelChiTietHoaDon.getValueAt(row, 5)));
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}
