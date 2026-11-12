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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import dao.HoaDon_DAO;
import dao.PhieuDoiTra_DAO;
import dao.PhieuDoiTra_DAO.HangDaMua;
import entity.ChiTietDoiTra;
import entity.ChiTietSanPham;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import dao.NhanVien_DAO;
import connectDB.ConnectDB;
import entity.PhieuDoiTra;

public class PhieuDoiTra_GUI extends JFrame
        implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaPDT, txtHoaDonGoc, txtHoaDonMoi, txtNgayLap;
    private JTextField txtSoDienThoai, txtKhachHang;
    private JTextArea txtLyDo;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboNhanVien;
    private ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
    private JTextField txtTienHangHoan, txtThueGoc, txtTienThueHoan, txtTongTienHoan;
    private JButton btnNapHoaDon, btnLuu, btnLamMoi, btnXemChiTiet;
    private JTable tableHangDaMua;
    private DefaultTableModel modelHangDaMua;
    private JTextField txtMaCTSP, txtSoLuong, txtSoLuongNhapLai;
    private JButton btnThem, btnSua, btnXoa;
    private JTable tableChiTietDoiTra;
    private DefaultTableModel modelChiTietDoiTra;

    private HoaDon_DAO hd_dao = new HoaDon_DAO();
    private PhieuDoiTra_DAO dt_dao = new PhieuDoiTra_DAO();
    private HoaDon hoaDonGoc;
    private KhachHang khachHangTheoSDT;
    private boolean daLuu;

    public PhieuDoiTra_GUI() {
        setTitle("Quản lý giày dép - Phiếu đổi trả");
        setSize(1500, 880);
        setMinimumSize(new java.awt.Dimension(1120, 680));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        buildUI();
        try {
            ConnectDB.getInstance().connect();
            docDuLieuNhanVien();
        } catch (SQLException e) {
            thongBao("Lỗi tải nhân viên: " + e.getMessage());
        }
        lamMoi();
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
        for (JTextField txt : new JTextField[] { txtMaPDT, txtHoaDonGoc,
                txtHoaDonMoi, txtNgayLap, txtSoDienThoai, txtKhachHang,
                txtTienHangHoan, txtThueGoc,
                txtTienThueHoan, txtTongTienHoan, txtMaCTSP,
                txtSoLuong, txtSoLuongNhapLai })
            txt.setFont(font);
        txtLyDo.setFont(font);
        cboTrangThai.setFont(font);
        cboNhanVien.setFont(font);
        for (JButton btn : new JButton[] { btnNapHoaDon, btnLuu,
                btnLamMoi, btnXemChiTiet, btnThem, btnSua, btnXoa })
            btn.setFont(font);
        for (JTable table : new JTable[] { tableHangDaMua, tableChiTietDoiTra }) {
            table.setFont(new Font("Arial", Font.PLAIN, 15));
            table.setRowHeight(32);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        }
        dinhDangNhan(getContentPane());
    }

    // Tam thoi chon nhan vien tu SQL cho den khi nhom tich hop dang nhap.
    private void docDuLieuNhanVien() {
        cboNhanVien.removeAllItems();
        dsNhanVien.clear();
        for (NhanVien nv : new NhanVien_DAO().gettalltbNhanVien()) {
            if (nv.isHoatDong()) {
                dsNhanVien.add(nv);
                cboNhanVien.addItem(nv.getTenNhanVien());
            }
        }
    }

    private NhanVien layNhanVien() {
        int viTri = cboNhanVien.getSelectedIndex();
        if (viTri < 0 || viTri >= dsNhanVien.size()) {
            throw new IllegalArgumentException("Chưa có nhân viên hoạt động để lập phiếu");
        }
        return dsNhanVien.get(viTri);
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 5));
        JLabel tieuDe = new JLabel("QUẢN LÝ GIÀY DÉP - PHIẾU ĐỔI TRẢ", SwingConstants.CENTER);
        tieuDe.setForeground(Color.BLUE);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        add(tieuDe, BorderLayout.NORTH);
        JPanel pnlChinh = new JPanel(new GridLayout(1, 2, 10, 0));
        add(pnlChinh, BorderLayout.CENTER);

        JPanel pnlPhieu = new JPanel(new BorderLayout(5, 5));
        pnlPhieu.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu đổi trả"));
        JPanel pnlThongTin = new JPanel(new GridLayout(9, 2, 5, 5));
        pnlThongTin.add(new JLabel("Mã phiếu đổi trả:"));
        pnlThongTin.add(txtMaPDT = new JTextField());
        pnlThongTin.add(new JLabel("Hóa đơn gốc:"));
        JPanel pnlGoc = new JPanel(new BorderLayout(5, 0));
        pnlGoc.add(txtHoaDonGoc = new JTextField(), BorderLayout.CENTER);
        pnlGoc.add(btnNapHoaDon = new JButton("Nạp"), BorderLayout.EAST);
        pnlThongTin.add(pnlGoc);
        pnlThongTin.add(new JLabel("Trạng thái:"));
        pnlThongTin.add(cboTrangThai = new JComboBox<>(
                new String[] { "Trả hàng", "Đổi size" }));
        pnlThongTin.add(new JLabel("Hóa đơn mới (nếu đổi):"));
        pnlThongTin.add(txtHoaDonMoi = new JTextField());
        pnlThongTin.add(new JLabel("Nhân viên:"));
        pnlThongTin.add(cboNhanVien = new JComboBox<>());
        pnlThongTin.add(new JLabel("Ngày lập:"));
        pnlThongTin.add(txtNgayLap = new JTextField());
        pnlThongTin.add(new JLabel("Số điện thoại KH:"));
        pnlThongTin.add(txtSoDienThoai = new JTextField());
        pnlThongTin.add(new JLabel("Khách hàng:"));
        pnlThongTin.add(txtKhachHang = new JTextField());
        pnlThongTin.add(new JLabel("Lý do:"));
        txtLyDo = new JTextArea(2, 20);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        pnlThongTin.add(new JScrollPane(txtLyDo));
        txtMaPDT.setEditable(false);
        txtNgayLap.setEditable(false);
        txtKhachHang.setEditable(false);
        pnlPhieu.add(pnlThongTin, BorderLayout.NORTH);

        JPanel pnlChiTiet = new JPanel(new BorderLayout(5, 5));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết đổi trả"));
        JPanel pnlNhap = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlNhap.add(new JLabel("Mã CTSP:"));
        pnlNhap.add(txtMaCTSP = new JTextField());
        pnlNhap.add(new JLabel("Số lượng trả:"));
        pnlNhap.add(txtSoLuong = new JTextField("1"));
        pnlNhap.add(new JLabel("Số lượng nhập lại kho:"));
        pnlNhap.add(txtSoLuongNhapLai = new JTextField("1"));
        txtMaCTSP.setEditable(false);
        pnlChiTiet.add(pnlNhap, BorderLayout.NORTH);
        String[] cotChiTiet = { "Mã CTSP", "Tên sản phẩm", "Kích cỡ",
                "Màu sắc", "Số lượng trả", "Số nhập lại" };
        modelChiTietDoiTra = new DefaultTableModel(cotChiTiet, 0);
        tableChiTietDoiTra = new JTable(modelChiTietDoiTra);
        tableChiTietDoiTra.setDefaultEditor(Object.class, null);
        pnlChiTiet.add(new JScrollPane(tableChiTietDoiTra), BorderLayout.CENTER);
        JPanel pnlNutChiTiet = new JPanel(new FlowLayout());
        pnlNutChiTiet.add(btnThem = new JButton("Thêm dòng"));
        pnlNutChiTiet.add(btnSua = new JButton("Sửa dòng"));
        pnlNutChiTiet.add(btnXoa = new JButton("Xóa dòng"));
        pnlChiTiet.add(pnlNutChiTiet, BorderLayout.SOUTH);
        pnlPhieu.add(pnlChiTiet, BorderLayout.CENTER);

        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        JPanel pnlTien = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlTien.add(new JLabel("Giá trị hàng trả chưa thuế (VND):"));
        pnlTien.add(txtTienHangHoan = new JTextField());
        pnlTien.add(new JLabel("Thuế hóa đơn gốc (%):"));
        pnlTien.add(txtThueGoc = new JTextField());
        pnlTien.add(new JLabel("Tiền thuế hoàn (VND):"));
        pnlTien.add(txtTienThueHoan = new JTextField());
        pnlTien.add(new JLabel("Tổng hoàn (0 nếu đổi size):"));
        pnlTien.add(txtTongTienHoan = new JTextField());
        txtTienHangHoan.setEditable(false);
        txtThueGoc.setEditable(false);
        txtTienThueHoan.setEditable(false);
        txtTongTienHoan.setEditable(false);
        pnlDuoi.add(pnlTien, BorderLayout.CENTER);
        JPanel pnlNut = new JPanel(new FlowLayout());
        pnlNut.add(btnLuu = new JButton("Lưu phiếu đổi trả"));
        pnlNut.add(btnLamMoi = new JButton("Làm mới"));
        pnlNut.add(btnXemChiTiet = new JButton("Xem chi tiết phiếu"));
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        pnlPhieu.add(pnlDuoi, BorderLayout.SOUTH);
        pnlChinh.add(pnlPhieu);

        JPanel pnlHangDaMua = new JPanel(new BorderLayout());
        pnlHangDaMua.setBorder(BorderFactory.createTitledBorder("Hàng trong hóa đơn gốc"));
        String[] cotMua = { "Mã CTSP", "Tên giày/dép", "Size", "Màu",
                "Đã mua", "Đã trả", "Còn được trả", "Đơn giá gốc" };
        modelHangDaMua = new DefaultTableModel(cotMua, 0);
        tableHangDaMua = new JTable(modelHangDaMua);
        tableHangDaMua.setDefaultEditor(Object.class, null);
        DinhDangTien.dinhDangCotTien(tableHangDaMua, 7);
        pnlHangDaMua.add(new JScrollPane(tableHangDaMua), BorderLayout.CENTER);
        pnlChinh.add(pnlHangDaMua);

        btnNapHoaDon.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLuu.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnXemChiTiet.addActionListener(this);
        tableHangDaMua.addMouseListener(this);
        tableChiTietDoiTra.addMouseListener(this);
        cboTrangThai.addActionListener(this);
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

    private void thongBao(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.ERROR_MESSAGE);
    }

    private void timKhachHangTheoSDT() {
        khachHangTheoSDT = null;
        txtKhachHang.setText("");
        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("0[0-9]{9}"))
            return;
        try {
            khachHangTheoSDT = hd_dao.getKhachHangTheoSoDienThoai(sdt);
            if (khachHangTheoSDT != null)
                txtKhachHang.setText(khachHangTheoSDT.getTenKH());
        } catch (SQLException e) {
            thongBao("Lỗi tra cứu khách hàng: " + e.getMessage());
        }
    }

    private void napHoaDon() throws SQLException {
        if (daLuu)
            throw new IllegalArgumentException("Phiếu đã lưu, hãy làm mới");
        String maHD = txtHoaDonGoc.getText().trim();
        if (maHD.isEmpty())
            throw new IllegalArgumentException("Nhập mã hóa đơn gốc");
        HoaDon hd = hd_dao.getHoaDonTheoMaHD(maHD);
        if (hd == null)
            throw new IllegalArgumentException("Không tìm thấy hóa đơn gốc");
        ArrayList<HangDaMua> ds = dt_dao.getHangDaMua(maHD);
        if (ds.isEmpty())
            throw new IllegalArgumentException("Hóa đơn không có chi tiết");
        if (hd.getKhachHang() == null)
            throw new IllegalArgumentException("Hóa đơn gốc chưa có khách hàng, cần xử lý dữ liệu cũ");
        hoaDonGoc = hd;
        modelHangDaMua.setRowCount(0);
        modelChiTietDoiTra.setRowCount(0);
        for (HangDaMua ct : ds) {
            modelHangDaMua.addRow(new Object[] {
                    ct.getMaCTSP(), ct.getTenSP(), ct.getKichCo(), ct.getMauSac(),
                    ct.getDaMua(), ct.getDaTra(), ct.getConLai(), ct.getDonGia()
            });
        }
        txtThueGoc.setText(hd.getThue().toPlainString());
        KhachHang kh = hd_dao.getKhachHangTheoMaKH(hd.getKhachHang().getMaKH());
        if (kh == null || kh.getSoDienThoai() == null
                || !kh.getSoDienThoai().matches("0[0-9]{9}"))
            throw new IllegalArgumentException("Khách hàng trên hóa đơn gốc không có SĐT hợp lệ");
        txtSoDienThoai.setText(kh.getSoDienThoai());
        txtKhachHang.setText(kh.getTenKH());
        tinhLaiTien();
        boolean quaHan = LocalDateTime.now().isAfter(hd.getNgayLap().plusDays(7));
        btnLuu.setEnabled(!quaHan);
        if (quaHan)
            thongBao("Đã quá 7 ngày kể từ lúc mua, không được đổi/trả");
    }

    private int laySoLuong(JTextField txt, String ten) {
        try {
            int so = Integer.parseInt(txt.getText().trim());
            if (so < 0 || (txt == txtSoLuong && so == 0))
                throw new NumberFormatException();
            return so;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ten + " phải là số nguyên hợp lệ");
        }
    }

    private int layDongHang(String maCTSP) {
        for (int i = 0; i < modelHangDaMua.getRowCount(); i++) {
            if (maCTSP.equals(modelHangDaMua.getValueAt(i, 0)))
                return i;
        }
        return -1;
    }

    private void themHoacSua(boolean sua) {
        if (daLuu)
            throw new IllegalArgumentException("Phiếu đã lưu, không thể sửa");
        if (hoaDonGoc == null)
            throw new IllegalArgumentException("Hãy nạp hóa đơn gốc trước");
        String maCTSP;
        int hang = tableHangDaMua.getSelectedRow();
        int dong = tableChiTietDoiTra.getSelectedRow();
        if (sua) {
            if (dong < 0)
                throw new IllegalArgumentException("Chọn dòng chi tiết cần sửa");
            dong = tableChiTietDoiTra.convertRowIndexToModel(dong);
            maCTSP = modelChiTietDoiTra.getValueAt(dong, 0).toString();
            hang = layDongHang(maCTSP);
        } else {
            if (hang < 0)
                throw new IllegalArgumentException("Chọn dòng hàng đã mua");
            hang = tableHangDaMua.convertRowIndexToModel(hang);
            maCTSP = modelHangDaMua.getValueAt(hang, 0).toString();
            for (int i = 0; i < modelChiTietDoiTra.getRowCount(); i++) {
                if (maCTSP.equals(modelChiTietDoiTra.getValueAt(i, 0))) {
                    throw new IllegalArgumentException("Đã thêm mã CTSP này, hãy sửa dòng");
                }
            }
        }
        int soLuong = laySoLuong(txtSoLuong, "Số lượng trả");
        int nhapLai = laySoLuong(txtSoLuongNhapLai, "Số nhập lại kho");
        if (nhapLai > soLuong)
            throw new IllegalArgumentException(
                    "Số nhập lại không được vượt số lượng trả");
        int conLai = (int) modelHangDaMua.getValueAt(hang, 6);
        if (soLuong > conLai)
            throw new IllegalArgumentException(
                    "Chỉ còn được trả " + conLai + " sản phẩm");
        Object[] values = {
                maCTSP, modelHangDaMua.getValueAt(hang, 1),
                modelHangDaMua.getValueAt(hang, 2), modelHangDaMua.getValueAt(hang, 3),
                soLuong, nhapLai
        };
        if (sua) {
            for (int i = 0; i < values.length; i++) {
                modelChiTietDoiTra.setValueAt(values[i], dong, i);
            }
        } else
            modelChiTietDoiTra.addRow(values);
        tinhLaiTien();
    }

    private void xoaDong() {
        if (daLuu)
            throw new IllegalArgumentException("Phiếu đã lưu, không thể xóa");
        int dong = tableChiTietDoiTra.getSelectedRow();
        if (dong < 0)
            throw new IllegalArgumentException("Chọn dòng cần xóa");
        modelChiTietDoiTra.removeRow(tableChiTietDoiTra.convertRowIndexToModel(dong));
        tinhLaiTien();
    }

    private void tinhLaiTien() {
        BigDecimal tienHang = BigDecimal.ZERO;
        for (int i = 0; i < modelChiTietDoiTra.getRowCount(); i++) {
            int hang = layDongHang(modelChiTietDoiTra.getValueAt(i, 0).toString());
            if (hang >= 0) {
                BigDecimal gia = (BigDecimal) modelHangDaMua.getValueAt(hang, 7);
                int so = (int) modelChiTietDoiTra.getValueAt(i, 4);
                tienHang = tienHang.add(gia.multiply(BigDecimal.valueOf(so)));
            }
        }
        tienHang = tienHang.setScale(2, RoundingMode.HALF_UP);
        BigDecimal thue = hoaDonGoc == null ? BigDecimal.ZERO : hoaDonGoc.getThue();
        BigDecimal tienThue = tienHang.multiply(thue).divide(
                new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        txtTienHangHoan.setText(DinhDangTien.hienThi(tienHang));
        txtTienThueHoan.setText("Đổi size".equals(cboTrangThai.getSelectedItem())
                ? "0"
                : DinhDangTien.hienThi(tienThue));
        txtTongTienHoan.setText("Đổi size".equals(cboTrangThai.getSelectedItem())
                ? "0"
                : DinhDangTien.hienThi(tienHang.add(tienThue)));
    }

    private void luuPhieu() throws SQLException {
        if (daLuu)
            throw new IllegalArgumentException("Phiếu đã lưu");
        if (hoaDonGoc == null)
            throw new IllegalArgumentException("Chưa nạp hóa đơn gốc");
        if (!hoaDonGoc.getMaHD().equals(txtHoaDonGoc.getText().trim())) {
            throw new IllegalArgumentException("Mã hóa đơn gốc đã đổi, hãy bấm Nạp lại");
        }
        if (modelChiTietDoiTra.getRowCount() == 0)
            throw new IllegalArgumentException("Chưa chọn sản phẩm đổi/trả");
        String lyDo = txtLyDo.getText().trim();
        if (lyDo.isEmpty() || lyDo.length() > 300) {
            throw new IllegalArgumentException("Lý do bắt buộc, tối đa 300 ký tự");
        }
        String sdt = txtSoDienThoai.getText().trim();
        if (!sdt.matches("0[0-9]{9}"))
            throw new IllegalArgumentException("Bắt buộc nhập SĐT khách hàng hợp lệ");
        KhachHang kh = hd_dao.getKhachHangTheoSoDienThoai(sdt);
        if (kh == null || hoaDonGoc.getKhachHang() == null
                || !kh.getMaKH().equals(hoaDonGoc.getKhachHang().getMaKH()))
            throw new IllegalArgumentException("SĐT không khớp khách hàng của hóa đơn gốc");

        boolean doi = "Đổi size".equals(cboTrangThai.getSelectedItem());
        String maHDMoi = txtHoaDonMoi.getText().trim();
        HoaDon hoaDonMoi = doi ? new HoaDon(maHDMoi) : null;
        PhieuDoiTra phieu = new PhieuDoiTra(null, hoaDonGoc, hoaDonMoi,
                layNhanVien(), LocalDateTime.now(), lyDo,
                (String) cboTrangThai.getSelectedItem());
        ArrayList<ChiTietDoiTra> ds = new ArrayList<>();
        for (int i = 0; i < modelChiTietDoiTra.getRowCount(); i++) {
            ds.add(new ChiTietDoiTra(null,
                    new ChiTietSanPham(modelChiTietDoiTra.getValueAt(i, 0).toString()),
                    (int) modelChiTietDoiTra.getValueAt(i, 4),
                    (int) modelChiTietDoiTra.getValueAt(i, 5)));
        }
        String maMoi = dt_dao.create(phieu, ds);
        txtMaPDT.setText(maMoi);
        daLuu = true;
        btnLuu.setEnabled(false);
        btnThem.setEnabled(false);
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        btnXemChiTiet.setEnabled(true);
        cboTrangThai.setEnabled(false);
        cboNhanVien.setEnabled(false);
        txtHoaDonGoc.setEditable(false);
        txtHoaDonMoi.setEditable(false);
        txtSoDienThoai.setEditable(false);
        txtLyDo.setEditable(false);
        JOptionPane.showMessageDialog(this, "Lưu phiếu thành công: " + maMoi);
        // Nap lai so luong da tra de bang phan anh ton tai moi.
        modelHangDaMua.setRowCount(0);
        for (HangDaMua ct : dt_dao.getHangDaMua(hoaDonGoc.getMaHD())) {
            modelHangDaMua.addRow(new Object[] { ct.getMaCTSP(), ct.getTenSP(),
                    ct.getKichCo(), ct.getMauSac(), ct.getDaMua(),
                    ct.getDaTra(), ct.getConLai(), ct.getDonGia() });
        }
    }

    private void lamMoi() {
        daLuu = false;
        hoaDonGoc = null;
        khachHangTheoSDT = null;
        txtMaPDT.setText("Tự sinh khi lưu");
        txtHoaDonGoc.setText("");
        txtHoaDonGoc.setEditable(true);
        txtHoaDonMoi.setText("");
        txtNgayLap.setText(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        txtSoDienThoai.setEditable(true);
        txtSoDienThoai.setText("");
        txtKhachHang.setText("");
        txtLyDo.setText("");
        txtLyDo.setEditable(true);
        cboTrangThai.setEnabled(true);
        cboNhanVien.setEnabled(true);
        cboTrangThai.setSelectedItem("Trả hàng");
        txtThueGoc.setText("0");
        txtMaCTSP.setText("");
        txtSoLuong.setText("1");
        txtSoLuongNhapLai.setText("1");
        modelHangDaMua.setRowCount(0);
        modelChiTietDoiTra.setRowCount(0);
        btnLuu.setEnabled(true);
        btnThem.setEnabled(true);
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
        btnXemChiTiet.setEnabled(false);
        tinhLaiTien();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        try {
            if (o == btnNapHoaDon)
                napHoaDon();
            else if (o == btnThem)
                themHoacSua(false);
            else if (o == btnSua)
                themHoacSua(true);
            else if (o == btnXoa)
                xoaDong();
            else if (o == btnLuu)
                luuPhieu();
            else if (o == btnLamMoi)
                lamMoi();
            else if (o == btnXemChiTiet) {
                new ChiTietDoiTra_GUI(txtMaPDT.getText()).setVisible(true);
            } else if (o == cboTrangThai) {
                boolean doi = "Đổi size".equals(cboTrangThai.getSelectedItem());
                txtHoaDonMoi.setEditable(doi && !daLuu);
                if (!doi && !daLuu)
                    txtHoaDonMoi.setText("");
                tinhLaiTien();
            }
        } catch (Exception ex) {
            thongBao(ex.getMessage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = e.getSource() == tableHangDaMua
                ? tableHangDaMua
                : tableChiTietDoiTra;
        int row = table.getSelectedRow();
        if (row < 0)
            return;
        row = table.convertRowIndexToModel(row);
        DefaultTableModel m = table == tableHangDaMua
                ? modelHangDaMua
                : modelChiTietDoiTra;
        txtMaCTSP.setText(m.getValueAt(row, 0).toString());
        txtSoLuong.setText(table == tableHangDaMua ? "1"
                : m.getValueAt(row, 4).toString());
        txtSoLuongNhapLai.setText(table == tableHangDaMua ? "1"
                : m.getValueAt(row, 5).toString());
    }

    public static void main(String[] args) {
        new PhieuDoiTra_GUI().setVisible(true);
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

}
