package gui;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamSource;
import java.awt.Desktop;
import java.awt.Component;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import connectDB.ConnectDB;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

// Lớp cũ, giờ hiển thị danh sách hóa đơn và chi tiết chỉ đọc (không thêm lớp mới).
public class ChiTietHoaDon_GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    // Dinh dang so tien ngay trong giao dien (VD: 1.250.000,5).
    private static String hienThiTien(BigDecimal tien) {
        if (tien == null) return "";
        java.text.DecimalFormatSymbols symbols =
                new java.text.DecimalFormatSymbols(java.util.Locale.forLanguageTag("vi-VN"));
        java.text.DecimalFormat format = new java.text.DecimalFormat("#,##0.##", symbols);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(tien);
    }

    // Hien thi cot tien can le phai, khong doi gia tri BigDecimal trong bang.
    private static void dinhDangCotTien(JTable bang, int... cot) {
        javax.swing.table.DefaultTableCellRenderer renderer =
                new javax.swing.table.DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void setValue(Object value) {
                if (value instanceof BigDecimal)
                    setText(hienThiTien((BigDecimal) value));
                else super.setValue(value);
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i : cot)
            bang.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    private static final java.time.format.DateTimeFormatter DINH_DANG_NGAY =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final HoaDon_DAO hd_dao = new HoaDon_DAO();
    private final ChiTietHoaDon_DAO ct_dao = new ChiTietHoaDon_DAO();
    private JTextField txtTim;
    private JTable bangDanhSach, bangChiTiet;
    private DefaultTableModel modelDanhSach, modelChiTiet;
    private JLabel lblThongTin;
    private JButton btnXem;

    public ChiTietHoaDon_GUI() { this(""); }

    // Main.java hiện tại gọi constructor này, giữ nguyên chữ ký.
    public ChiTietHoaDon_GUI(String maHD) {
        setTitle("Danh sách hóa đơn");
        setSize(1200, 760);
        setMinimumSize(new Dimension(900, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        JLabel tieuDe = new JLabel("DANH SÁCH HÓA ĐƠN", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 25));
        add(tieuDe, BorderLayout.NORTH);

        JPanel thanhTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thanhTim.add(new JLabel("Mã HD / khách hàng / ngày:"));
        txtTim = new JTextField(maHD == null ? "" : maHD, 23);
        JButton btnTim = new JButton("Tìm");
        JButton btnLamMoi = new JButton("Làm mới");
        btnXem = new JButton("Xem chi tiết");
        JButton btnIn = new JButton("In chi tiết");
        btnXem.setEnabled(false);
        thanhTim.add(txtTim);
        thanhTim.add(btnTim);
        thanhTim.add(btnLamMoi);
        thanhTim.add(btnXem);
        thanhTim.add(btnIn);

        modelDanhSach = new DefaultTableModel(new String[]{
                "Mã HD", "Ngày lập", "Nhân viên", "Tên KH", "Thanh toán", "Tổng tiền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangDanhSach = new JTable(modelDanhSach);
        bangDanhSach.setRowHeight(28);
        bangDanhSach.setAutoCreateRowSorter(true);
        dinhDangCotTien(bangDanhSach, 5);
        JPanel tren = new JPanel(new BorderLayout(5, 5));
        tren.add(thanhTim, BorderLayout.NORTH);
        tren.add(new JScrollPane(bangDanhSach), BorderLayout.CENTER);

        modelChiTiet = new DefaultTableModel(new String[]{
                "Mã CTSP", "Tên sản phẩm", "Size", "Màu", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangChiTiet = new JTable(modelChiTiet);
        bangChiTiet.setRowHeight(28);
        dinhDangCotTien(bangChiTiet, 5, 6);
        lblThongTin = new JLabel("Chọn một hóa đơn rồi nhấn Xem chi tiết", SwingConstants.CENTER);
        JPanel duoi = new JPanel(new BorderLayout(5, 5));
        duoi.add(lblThongTin, BorderLayout.NORTH);
        duoi.add(new JScrollPane(bangChiTiet), BorderLayout.CENTER);

        JSplitPane chia = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tren, duoi);
        chia.setResizeWeight(0.55);
        chia.setDividerLocation(350);
        add(chia, BorderLayout.CENTER);

        btnTim.addActionListener(e -> {
            // Tìm chỉ lọc danh sách, không yêu cầu chọn dòng trước.
            modelChiTiet.setRowCount(0);
            lblThongTin.setText("Chọn một hóa đơn rồi nhấn Xem chi tiết");
            napDanhSach();
        });
        txtTim.addActionListener(e -> btnTim.doClick());
        btnLamMoi.addActionListener(e -> {
            txtTim.setText("");
            modelChiTiet.setRowCount(0);
            lblThongTin.setText("Chọn một hóa đơn rồi nhấn Xem chi tiết");
            napDanhSach();
        });
        btnXem.addActionListener(e -> {
            int r = bangDanhSach.getSelectedRow();
            if (r < 0) return;
            String ma = modelDanhSach.getValueAt(bangDanhSach.convertRowIndexToModel(r), 0).toString();
            try { docDuLieuDatabaseVaoTable(ma); }
            catch (SQLException ex) { thongBao(ex.getMessage()); }
        });
        bangDanhSach.getSelectionModel().addListSelectionListener(e ->
                btnXem.setEnabled(bangDanhSach.getSelectedRow() >= 0));
        bangDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) btnXem.doClick();
            }
        });
        btnIn.addActionListener(e -> {
            int r = bangDanhSach.getSelectedRow();
            if (r < 0) { thongBao("Chọn hóa đơn để in"); return; }
            String ma = modelDanhSach.getValueAt(bangDanhSach.convertRowIndexToModel(r), 0).toString();
            try { inHoaDon(ma, this); }
            catch (Exception ex) { thongBao("Không in được hóa đơn: " + ex.getMessage()); }
        });
        try { ConnectDB.getInstance().connect(); }
        catch (SQLException ex) { thongBao("Lỗi kết nối SQL Server: " + ex.getMessage()); }
        napDanhSach();
        if (maHD != null && !maHD.trim().isEmpty()) {
            try { docDuLieuDatabaseVaoTable(maHD.trim()); }
            catch (SQLException ex) { thongBao(ex.getMessage()); }
        }
    }

    // Goi tu tab Main hoac khi quay lai tab de cap nhat danh sach.
    public void taiLaiDanhSach() {
        napDanhSach();
        modelChiTiet.setRowCount(0);
        lblThongTin.setText("Chọn một hóa đơn để xem chi tiết");
    }

    // Khi nhan Xem / In tu man hinh lap phieu, chon chung tu trong tab danh sach.
    public void chonChungTu(String ma) {
        if (ma == null || ma.trim().isEmpty()) return;
        txtTim.setText(ma.trim());
        napDanhSach();
        for (int i = 0; i < modelDanhSach.getRowCount(); i++) {
            if (ma.trim().equalsIgnoreCase(modelDanhSach.getValueAt(i, 0).toString())) {
                int view = bangDanhSach.convertRowIndexToView(i);
                bangDanhSach.setRowSelectionInterval(view, view);
                bangDanhSach.scrollRectToVisible(bangDanhSach.getCellRect(view, 0, true));
                try { docDuLieuDatabaseVaoTable(ma.trim()); }
                catch (SQLException ex) { thongBao(ex.getMessage()); }
                return;
            }
        }
    }

    private void thongBao(String s) { JOptionPane.showMessageDialog(this, s); }

    private void napDanhSach() {
        modelDanhSach.setRowCount(0);
        String tim = txtTim.getText().trim().toLowerCase();
        try {
            for (HoaDon hd : hd_dao.getAllTableHoaDon()) {
                String maKH = hd.getKhachHang() == null ? "" : hd.getKhachHang().getMaKH();
                String tenKH = hd.getKhachHang() == null ? "Chưa liên kết"
                        : hd.getKhachHang().getTenKH();
                String sdt = hd.getKhachHang() == null ? "" : hd.getKhachHang().getSoDienThoai();
                String tenNV = hd.getNhanVien().getTenNhanVien();
                String ngay = hd.getNgayLap().format(DINH_DANG_NGAY);
                String cotTim = (hd.getMaHD() + " " + maKH + " " + tenKH + " " + sdt + " "
                        + ngay + " " + hd.getNhanVien().getMaNhanVien() + " " + tenNV).toLowerCase();
                if (!cotTim.contains(tim)) continue;
                modelDanhSach.addRow(new Object[]{hd.getMaHD(), ngay,
                        tenNV, tenKH, hd.getPhuongThucThanhToan(), hd.getTongTien()});
            }
        } catch (SQLException ex) { thongBao("Lỗi tải danh sách: " + ex.getMessage()); }
    }

    // Tên phương thức cũ vẫn giữ, tương thích Main.java.
    public void docDuLieuDatabaseVaoTable(String maHD) throws SQLException {
        HoaDon hd = hd_dao.getHoaDonTheoMaHD(maHD);
        if (hd == null) throw new SQLException("Không tìm thấy hóa đơn: " + maHD);
        modelChiTiet.setRowCount(0);
        for (ChiTietHoaDon ct : ct_dao.getChiTietTheoMaHD(maHD)) {
            modelChiTiet.addRow(new Object[]{ct.getChiTietSanPham().getMaCTSP(),
                    ct.getChiTietSanPham().getSanPham().getTenSP(),
                    ct.getChiTietSanPham().getKichCo(), ct.getChiTietSanPham().getMauSac(),
                    ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien()});
        }
        BigDecimal tong = hd.getTongTien();
        lblThongTin.setText("Hóa đơn: " + maHD + "   |   Tổng tiền: " + hienThiTien(tong));
    }

    // JAXP DOM tạo XML, sau đó JAXP + XSLT chuyển XML thành HTML để in.
    private static void themThe(Document doc, Element cha, String ten, Object giaTri) {
        Element the = doc.createElement(ten);
        the.setTextContent(giaTri == null ? "" : giaTri.toString());
        cha.appendChild(the);
    }

    // Dùng chung cho nút In chi tiết ở màn hình bán hàng và danh sách hóa đơn.
    public static void inHoaDon(String maHD, Component parent) throws Exception {
        HoaDon_DAO hdDao = new HoaDon_DAO();
        HoaDon hd = hdDao.getHoaDonTheoMaHD(maHD);
        if (hd == null) throw new SQLException("Không tìm thấy hóa đơn");
        ArrayList<ChiTietHoaDon> ds = new ChiTietHoaDon_DAO().getChiTietTheoMaHD(maHD);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element goc = doc.createElement("hoaDon");
        doc.appendChild(goc);
        themThe(doc, goc, "maHD", hd.getMaHD());
        themThe(doc, goc, "ngayLap", hd.getNgayLap().format(DINH_DANG_NGAY));
        themThe(doc, goc, "tenNhanVien", hd.getNhanVien().getTenNhanVien());
        themThe(doc, goc, "phuongThucThanhToan", hd.getPhuongThucThanhToan());
        themThe(doc, goc, "thue", hd.getThue());
        themThe(doc, goc, "tongTien", hd.getTongTien());
        if (hd.getKhachHang() == null)
            throw new SQLException("Hóa đơn chưa liên kết khách hàng; vui lòng xử lý dữ liệu cũ");
        entity.KhachHang kh = hdDao.getKhachHangTheoMaKH(hd.getKhachHang().getMaKH());
        if (kh == null || kh.getSoDienThoai() == null || kh.getSoDienThoai().trim().isEmpty())
            throw new SQLException("Khách hàng chưa có số điện thoại");
        themThe(doc, goc, "tenKH", kh.getTenKH());
        themThe(doc, goc, "soDienThoai", kh.getSoDienThoai());
        themThe(doc, goc, "tienHang", hd.tinhTienHang(ds));
        themThe(doc, goc, "tienThue", hd.tinhTienThue(ds));
        Element chiTiet = doc.createElement("chiTiet");
        goc.appendChild(chiTiet);
        for (ChiTietHoaDon ct : ds) {
            Element dong = doc.createElement("dong");
            chiTiet.appendChild(dong);
            themThe(doc, dong, "maCTSP", ct.getChiTietSanPham().getMaCTSP());
            themThe(doc, dong, "tenSP", ct.getChiTietSanPham().getSanPham().getTenSP());
            themThe(doc, dong, "kichCo", ct.getChiTietSanPham().getKichCo());
            themThe(doc, dong, "mauSac", ct.getChiTietSanPham().getMauSac());
            themThe(doc, dong, "soLuong", ct.getSoLuong());
            themThe(doc, dong, "donGia", ct.getDonGia());
            themThe(doc, dong, "thanhTien", ct.tinhThanhTien());
        }

        File thuMuc = new File(System.getProperty("java.io.tmpdir"), "QuanLyGiayDep_In");
        if (!thuMuc.exists() && !thuMuc.mkdirs())
            throw new java.io.IOException("Không tạo được thư mục in");
        File xml = new File(thuMuc, maHD + ".xml");
        File html = new File(thuMuc, maHD + ".html");
        Transformer ghiXml = TransformerFactory.newInstance().newTransformer();
        ghiXml.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        ghiXml.setOutputProperty(OutputKeys.INDENT, "yes");
        ghiXml.transform(new DOMSource(doc), new StreamResult(xml));

        File xsl = new File("data/HoaDon.xsl");
        if (!xsl.isFile()) throw new java.io.IOException("Thiếu data/HoaDon.xsl");
        Transformer chuyenHtml = TransformerFactory.newInstance()
                .newTransformer(new StreamSource(xsl));
        chuyenHtml.transform(new StreamSource(xml), new StreamResult(html));
        if (Desktop.isDesktopSupported())
            Desktop.getDesktop().browse(html.toURI());
        else
            JOptionPane.showMessageDialog(parent, "Đã tạo HTML: " + html.getAbsolutePath());
    }
}
