package gui;

import dao.ChiTietDoiTra_DAO;
import dao.PhieuDoiTra_DAO;
import entity.ChiTietDoiTra;
import entity.PhieuDoiTra;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
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

// Lớp cũ dùng làm Danh sách đổi trả và xem chi tiết, không cho chỉnh sửa.
public class ChiTietDoiTra_GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final PhieuDoiTra_DAO dt_dao = new PhieuDoiTra_DAO();
    private final ChiTietDoiTra_DAO ct_dao = new ChiTietDoiTra_DAO();
    private JTextField txtTim;
    private DefaultTableModel modelDanhSach, modelChiTiet;
    private JTable bangDanhSach, bangChiTiet;
    private JLabel lblThongTin;
    private JButton btnXem;

    public ChiTietDoiTra_GUI() { this(""); }

    public ChiTietDoiTra_GUI(String maPDT) {
        setTitle("Danh sách đổi trả");
        setSize(1150, 740);
        setMinimumSize(new Dimension(900, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        JLabel tieuDe = new JLabel("DANH SÁCH ĐỔI TRẢ", SwingConstants.CENTER);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 25));
        add(tieuDe, BorderLayout.NORTH);

        JPanel thanh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thanh.add(new JLabel("Mã phiếu / hóa đơn gốc:"));
        txtTim = new JTextField(maPDT == null ? "" : maPDT, 22);
        JButton btnTim = new JButton("Tìm");
        JButton btnLamMoi = new JButton("Làm mới");
        btnXem = new JButton("Xem chi tiết");
        btnXem.setEnabled(false);
        JButton btnXml = new JButton("Xuất XML");
        thanh.add(txtTim);
        thanh.add(btnTim);
        thanh.add(btnLamMoi);
        thanh.add(btnXem);
        thanh.add(btnXml);

        modelDanhSach = new DefaultTableModel(new String[]{
                "Mã phiếu", "Hóa đơn gốc", "Hóa đơn mới", "Ngày lập", "Nhân viên", "Loại", "Lý do"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangDanhSach = new JTable(modelDanhSach);
        bangDanhSach.setRowHeight(29);
        bangDanhSach.setAutoCreateRowSorter(true);
        JPanel tren = new JPanel(new BorderLayout(4, 4));
        tren.add(thanh, BorderLayout.NORTH);
        tren.add(new JScrollPane(bangDanhSach), BorderLayout.CENTER);

        modelChiTiet = new DefaultTableModel(new String[]{
                "Mã CTSP", "Tên sản phẩm", "Size", "Màu", "Số lượng đổi/trả", "Nhập lại kho"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        bangChiTiet = new JTable(modelChiTiet);
        bangChiTiet.setRowHeight(28);
        JPanel duoi = new JPanel(new BorderLayout(5, 5));
        lblThongTin = new JLabel("Chọn phiếu rồi nhấn Xem chi tiết", SwingConstants.CENTER);
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
            lblThongTin.setText("Chọn phiếu rồi nhấn Xem chi tiết");
            napDanhSach();
        });
        bangDanhSach.getSelectionModel().addListSelectionListener(e ->
                btnXem.setEnabled(bangDanhSach.getSelectedRow() >= 0));
        btnXem.addActionListener(e -> {
            int r = bangDanhSach.getSelectedRow();
            if (r < 0) return;
            String ma = modelDanhSach.getValueAt(bangDanhSach.convertRowIndexToModel(r), 0).toString();
            try { docDuLieuDatabaseVaoTable(ma); }
            catch (SQLException ex) { thongBao(ex.getMessage()); }
        });
        bangDanhSach.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) btnXem.doClick();
            }
        });
        btnXml.addActionListener(e -> xuatXml());
        napDanhSach();
        if (maPDT != null && !maPDT.trim().isEmpty()) {
            try { docDuLieuDatabaseVaoTable(maPDT.trim()); }
            catch (SQLException ex) { thongBao(ex.getMessage()); }
        }
    }

    // Goi tu tab Main hoac khi quay lai tab de cap nhat danh sach.
    public void taiLaiDanhSach() {
        napDanhSach();
        modelChiTiet.setRowCount(0);
        lblThongTin.setText("Chọn một phiếu đổi/trả để xem chi tiết");
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
            for (PhieuDoiTra p : dt_dao.getAllTablePhieuDoiTra()) {
                String maMoi = p.getHoaDonMoi() == null ? "" : p.getHoaDonMoi().getMaHD();
                String timTrong = (p.getMaPDT() + " " + p.getHoaDonGoc().getMaHD() + " "
                        + maMoi + " " + p.getNgayLap()).toLowerCase();
                if (!timTrong.contains(tim)) continue;
                modelDanhSach.addRow(new Object[]{p.getMaPDT(), p.getHoaDonGoc().getMaHD(),
                        maMoi, p.getNgayLap(), p.getNhanVien().getMaNhanVien(),
                        p.getTrangThai(), p.getLyDo()});
            }
        } catch (SQLException ex) { thongBao("Lỗi tải danh sách: " + ex.getMessage()); }
    }

    public void docDuLieuDatabaseVaoTable(String maPDT) throws SQLException {
        PhieuDoiTra p = dt_dao.getPhieuTheoMaPDT(maPDT);
        if (p == null) throw new SQLException("Không tìm thấy phiếu đổi trả: " + maPDT);
        modelChiTiet.setRowCount(0);
        ArrayList<ChiTietDoiTra> ds = ct_dao.getChiTietTheoMaPDT(maPDT);
        for (ChiTietDoiTra ct : ds) {
            modelChiTiet.addRow(new Object[]{ct.getChiTietSanPham().getMaCTSP(),
                    ct.getChiTietSanPham().getSanPham().getTenSP(),
                    ct.getChiTietSanPham().getKichCo(), ct.getChiTietSanPham().getMauSac(),
                    ct.getSoLuong(), ct.getSoLuongNhapLai()});
        }
        lblThongTin.setText("Phiếu: " + maPDT + "   |   HD gốc: " + p.getHoaDonGoc().getMaHD()
                + "   |   HD mới: " + (p.getHoaDonMoi() == null ? "Không có" : p.getHoaDonMoi().getMaHD()));
    }

    private void ghi(XMLStreamWriter xml, String tag, Object v) throws Exception {
        xml.writeStartElement(tag);
        xml.writeCharacters(v == null ? "" : v.toString());
        xml.writeEndElement();
    }

    private void xuatXml() {
        if (modelDanhSach.getRowCount() == 0) { thongBao("Danh sách đang trống"); return; }
        JFileChooser chon = new JFileChooser();
        chon.setSelectedFile(new File("DanhSachDoiTra.xml"));
        if (chon.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File tep = chon.getSelectedFile();
        if (!tep.getName().toLowerCase().endsWith(".xml")) tep = new File(tep.getPath() + ".xml");
        if (tep.exists() && JOptionPane.showConfirmDialog(this, "Ghi đè " + tep.getName() + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        XMLStreamWriter xml = null;
        try (FileOutputStream os = new FileOutputStream(tep)) {
            xml = XMLOutputFactory.newFactory().createXMLStreamWriter(os, "UTF-8");
            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeStartElement("danhSachDoiTra");
            for (int i = 0; i < modelDanhSach.getRowCount(); i++) {
                PhieuDoiTra p = dt_dao.getPhieuTheoMaPDT(modelDanhSach.getValueAt(i, 0).toString());
                xml.writeStartElement("phieuDoiTra");
                ghi(xml, "maPDT", p.getMaPDT());
                ghi(xml, "hoaDonGoc", p.getHoaDonGoc().getMaHD());
                ghi(xml, "hoaDonMoi", p.getHoaDonMoi() == null ? "" : p.getHoaDonMoi().getMaHD());
                ghi(xml, "maNhanVien", p.getNhanVien().getMaNhanVien());
                ghi(xml, "ngayLap", p.getNgayLap());
                ghi(xml, "loai", p.getTrangThai());
                ghi(xml, "lyDo", p.getLyDo());
                xml.writeStartElement("chiTiet");
                for (ChiTietDoiTra ct : ct_dao.getChiTietTheoMaPDT(p.getMaPDT())) {
                    xml.writeStartElement("dong");
                    ghi(xml, "maCTSP", ct.getChiTietSanPham().getMaCTSP());
                    ghi(xml, "soLuong", ct.getSoLuong());
                    ghi(xml, "soLuongNhapLai", ct.getSoLuongNhapLai());
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
