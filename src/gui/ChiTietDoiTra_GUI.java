package gui;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamSource;
import java.awt.Desktop;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import connectDB.ConnectDB;
import dao.ChiTietDoiTra_DAO;
import dao.PhieuDoiTra_DAO;
import entity.ChiTietDoiTra;
import entity.PhieuDoiTra;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
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

// Lớp cũ dùng làm Danh sách đổi trả và xem chi tiết, không cho chỉnh sửa.
public class ChiTietDoiTra_GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final java.time.format.DateTimeFormatter DINH_DANG_NGAY =
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        JButton btnIn = new JButton("In chi tiết");
        thanh.add(txtTim);
        thanh.add(btnTim);
        thanh.add(btnLamMoi);
        thanh.add(btnXem);
        thanh.add(btnIn);

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

        btnTim.addActionListener(e -> {
            // Tìm chỉ lọc danh sách, không yêu cầu chọn dòng trước.
            modelChiTiet.setRowCount(0);
            lblThongTin.setText("Chọn phiếu rồi nhấn Xem chi tiết");
            napDanhSach();
        });
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
        btnIn.addActionListener(e -> {
            int r = bangDanhSach.getSelectedRow();
            if (r < 0) { thongBao("Chọn phiếu đổi/trả để in"); return; }
            String ma = modelDanhSach.getValueAt(bangDanhSach.convertRowIndexToModel(r), 0).toString();
            try { inChiTiet(ma); }
            catch (Exception ex) { thongBao("Không in được phiếu: " + ex.getMessage()); }
        });
        try { ConnectDB.getInstance().connect(); }
        catch (SQLException ex) { thongBao("Lỗi kết nối SQL Server: " + ex.getMessage()); }
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
                String tenNV = p.getNhanVien().getTenNhanVien();
                String timTrong = (p.getMaPDT() + " " + p.getHoaDonGoc().getMaHD() + " "
                        + maMoi + " " + p.getNgayLap() + " " + tenNV
                        + " " + p.getNhanVien().getMaNhanVien()).toLowerCase();
                if (!timTrong.contains(tim)) continue;
                modelDanhSach.addRow(new Object[]{p.getMaPDT(), p.getHoaDonGoc().getMaHD(),
                        maMoi, p.getNgayLap().format(DINH_DANG_NGAY), tenNV,
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

    // JAXP DOM tạo XML, XSLT chuyển sang HTML để xem và in.
    private void themThe(Document doc, Element cha, String ten, Object giaTri) {
        Element the = doc.createElement(ten);
        the.setTextContent(giaTri == null ? "" : giaTri.toString());
        cha.appendChild(the);
    }

    private void inChiTiet(String maPDT) throws Exception {
        PhieuDoiTra p = dt_dao.getPhieuTheoMaPDT(maPDT);
        if (p == null) throw new SQLException("Không tìm thấy phiếu đổi/trả");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element goc = doc.createElement("phieuDoiTra");
        doc.appendChild(goc);
        themThe(doc, goc, "maPDT", p.getMaPDT());
        themThe(doc, goc, "hoaDonGoc", p.getHoaDonGoc().getMaHD());
        themThe(doc, goc, "hoaDonMoi", p.getHoaDonMoi() == null ? "" : p.getHoaDonMoi().getMaHD());
        themThe(doc, goc, "tenNhanVien", p.getNhanVien().getTenNhanVien());
        themThe(doc, goc, "ngayLap", p.getNgayLap().format(DINH_DANG_NGAY));
        themThe(doc, goc, "loai", p.getTrangThai());
        themThe(doc, goc, "lyDo", p.getLyDo());
        Element chiTiet = doc.createElement("chiTiet");
        goc.appendChild(chiTiet);
        for (ChiTietDoiTra ct : ct_dao.getChiTietTheoMaPDT(maPDT)) {
            Element dong = doc.createElement("dong");
            chiTiet.appendChild(dong);
            themThe(doc, dong, "maCTSP", ct.getChiTietSanPham().getMaCTSP());
            themThe(doc, dong, "tenSP", ct.getChiTietSanPham().getSanPham().getTenSP());
            themThe(doc, dong, "kichCo", ct.getChiTietSanPham().getKichCo());
            themThe(doc, dong, "mauSac", ct.getChiTietSanPham().getMauSac());
            themThe(doc, dong, "soLuong", ct.getSoLuong());
            themThe(doc, dong, "soLuongNhapLai", ct.getSoLuongNhapLai());
        }
        File thuMuc = new File(System.getProperty("java.io.tmpdir"), "QuanLyGiayDep_In");
        if (!thuMuc.exists() && !thuMuc.mkdirs())
            throw new java.io.IOException("Không tạo được thư mục in");
        File xml = new File(thuMuc, maPDT + ".xml");
        File html = new File(thuMuc, maPDT + ".html");
        Transformer ghiXml = TransformerFactory.newInstance().newTransformer();
        ghiXml.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        ghiXml.setOutputProperty(OutputKeys.INDENT, "yes");
        ghiXml.transform(new DOMSource(doc), new StreamResult(xml));
        File xsl = new File("data/PhieuDoiTra.xsl");
        if (!xsl.isFile()) throw new java.io.IOException("Thiếu data/PhieuDoiTra.xsl");
        Transformer chuyenHtml = TransformerFactory.newInstance()
                .newTransformer(new StreamSource(xsl));
        chuyenHtml.transform(new StreamSource(xml), new StreamResult(html));
        if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(html.toURI());
        else thongBao("Đã tạo HTML: " + html.getAbsolutePath());
    }
}
