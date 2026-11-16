package gui;

import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

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
        JButton btnXml = new JButton("Xuất XML");
        JButton btnIn = new JButton("In chi tiết");
        btnXem.setEnabled(false);
        thanhTim.add(txtTim);
        thanhTim.add(btnTim);
        thanhTim.add(btnLamMoi);
        thanhTim.add(btnXem);
        thanhTim.add(btnXml);
        thanhTim.add(btnIn);

        modelDanhSach = new DefaultTableModel(new String[]{
                "Mã HD", "Ngày lập", "Nhân viên", "Mã KH", "Thanh toán", "Tổng tiền"}, 0) {
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

        btnTim.addActionListener(e -> napDanhSach());
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
        btnXml.addActionListener(e -> xuatXml());
        btnIn.addActionListener(e -> {
            if (modelChiTiet.getRowCount() == 0) {
                thongBao("Chọn Xem chi tiết trước khi in");
                return;
            }
            try {
                bangChiTiet.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat(lblThongTin.getText()), null);
            } catch (Exception ex) { thongBao(ex.getMessage()); }
        });
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
                String maKH = hd.getKhachHang() == null ? "Khách lẻ" : hd.getKhachHang().getMaKH();
                String cotTim = (hd.getMaHD() + " " + maKH + " " + hd.getNgayLap()
                        + " " + hd.getNhanVien().getMaNhanVien()).toLowerCase();
                if (!cotTim.contains(tim)) continue;
                modelDanhSach.addRow(new Object[]{hd.getMaHD(), hd.getNgayLap(),
                        hd.getNhanVien().getMaNhanVien(), maKH, hd.getPhuongThucThanhToan(), hd.getTongTien()});
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

    private void ghi(XMLStreamWriter xml, String tag, Object value) throws Exception {
        xml.writeStartElement(tag);
        xml.writeCharacters(value == null ? "" : value.toString());
        xml.writeEndElement();
    }

    private void xuatXml() {
        if (modelDanhSach.getRowCount() == 0) { thongBao("Danh sách đang trống"); return; }
        JFileChooser chon = new JFileChooser();
        chon.setSelectedFile(new File("DanhSachHoaDon.xml"));
        if (chon.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File tep = chon.getSelectedFile();
        if (!tep.getName().toLowerCase().endsWith(".xml")) tep = new File(tep.getPath() + ".xml");
        if (tep.exists() && JOptionPane.showConfirmDialog(this, "Ghi đè " + tep.getName() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        XMLStreamWriter xml = null;
        try (FileOutputStream os = new FileOutputStream(tep)) {
            xml = XMLOutputFactory.newFactory().createXMLStreamWriter(os, "UTF-8");
            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeStartElement("danhSachHoaDon");
            for (int i = 0; i < modelDanhSach.getRowCount(); i++) {
                String maHD = modelDanhSach.getValueAt(i, 0).toString();
                HoaDon hd = hd_dao.getHoaDonTheoMaHD(maHD);
                xml.writeStartElement("hoaDon");
                ghi(xml, "maHD", hd.getMaHD());
                ghi(xml, "maNhanVien", hd.getNhanVien().getMaNhanVien());
                ghi(xml, "maKH", hd.getKhachHang() == null ? "" : hd.getKhachHang().getMaKH());
                ghi(xml, "ngayLap", hd.getNgayLap());
                ghi(xml, "phuongThucThanhToan", hd.getPhuongThucThanhToan());
                ghi(xml, "thue", hd.getThue());
                ghi(xml, "tongTien", hd.getTongTien());
                xml.writeStartElement("chiTiet");
                for (ChiTietHoaDon ct : ct_dao.getChiTietTheoMaHD(maHD)) {
                    xml.writeStartElement("dong");
                    ghi(xml, "maCTSP", ct.getChiTietSanPham().getMaCTSP());
                    ghi(xml, "soLuong", ct.getSoLuong());
                    ghi(xml, "donGia", ct.getDonGia());
                    xml.writeEndElement();
                }
                xml.writeEndElement();
                xml.writeEndElement();
            }
            xml.writeEndElement();
            xml.writeEndDocument();
            xml.flush();
            JOptionPane.showMessageDialog(this, "Đã xuất: " + tep.getAbsolutePath());
        } catch (Exception ex) { thongBao("Không xuất được XML: " + ex.getMessage()); }
        finally { if (xml != null) try { xml.close(); } catch (Exception ignored) { } }
    }
}
