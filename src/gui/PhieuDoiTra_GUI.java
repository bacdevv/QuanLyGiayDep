package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class PhieuDoiTra_GUI extends JFrame {
    private JTextField txtMaPDT, txtHoaDonGoc, txtHoaDonMoi, txtNgayLap;
    private JTextArea txtLyDo;
    private JComboBox<String> cboNhanVien;
    private JTextField txtTienHangHoan, txtThueGoc, txtTienThueHoan, txtTongTienHoan;
    private JButton btnNapHoaDon, btnLuu, btnLamMoi;
    private JTable tableHangDaMua;
    private DefaultTableModel modelHangDaMua;
    private JTextField txtMaCTSP, txtSoLuong, txtSoLuongNhapLai;
    private JButton btnThem, btnSua, btnXoa;
    private JTable tableChiTietDoiTra;
    private DefaultTableModel modelChiTietDoiTra;

    private static final long serialVersionUID = 1L;

    public PhieuDoiTra_GUI() {
        setTitle("Quản lý giày dép - Phiếu đổi trả");
        setSize(1280, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 5));
        add(new JLabel("QUẢN LÝ GIÀY DÉP - PHIẾU ĐỔI TRẢ", SwingConstants.CENTER),
                BorderLayout.NORTH);

        JPanel pnlChinh = new JPanel(new GridLayout(1, 2, 10, 0));
        add(pnlChinh, BorderLayout.CENTER);
        JPanel pnlPhieu = new JPanel(new BorderLayout(5, 5));
        pnlPhieu.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu đổi trả"));
        JPanel pnlThongTin = new JPanel(new GridLayout(6, 2, 5, 5));
        pnlThongTin.add(new JLabel("Mã phiếu đổi trả:"));
        pnlThongTin.add(txtMaPDT = new JTextField());
        pnlThongTin.add(new JLabel("Hóa đơn gốc:"));
        JPanel pnlHoaDonGoc = new JPanel(new BorderLayout(5, 0));
        pnlHoaDonGoc.add(txtHoaDonGoc = new JTextField(), BorderLayout.CENTER);
        pnlHoaDonGoc.add(btnNapHoaDon = new JButton("Nạp"), BorderLayout.EAST);
        pnlThongTin.add(pnlHoaDonGoc);
        pnlThongTin.add(new JLabel("Hóa đơn mới (nếu đổi):"));
        pnlThongTin.add(txtHoaDonMoi = new JTextField());
        pnlThongTin.add(new JLabel("Nhân viên:"));
        pnlThongTin.add(cboNhanVien = new JComboBox<String>());
        pnlThongTin.add(new JLabel("Ngày lập:"));
        pnlThongTin.add(txtNgayLap = new JTextField());
        pnlThongTin.add(new JLabel("Lý do:"));
        txtLyDo = new JTextArea(2, 20);
        txtLyDo.setLineWrap(true);
        txtLyDo.setWrapStyleWord(true);
        pnlThongTin.add(new JScrollPane(txtLyDo));
        txtMaPDT.setEditable(false);
        txtNgayLap.setEditable(false);
        pnlPhieu.add(pnlThongTin, BorderLayout.NORTH);
        JPanel pnlChiTiet = new JPanel(new BorderLayout(5, 5));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết đổi trả"));
        JPanel pnlNhap = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlNhap.add(new JLabel("Mã biến thể:"));
        pnlNhap.add(txtMaCTSP = new JTextField());
        pnlNhap.add(new JLabel("Số lượng trả:"));
        pnlNhap.add(txtSoLuong = new JTextField());
        pnlNhap.add(new JLabel("Số lượng nhập lại kho:"));
        pnlNhap.add(txtSoLuongNhapLai = new JTextField());
        txtMaCTSP.setEditable(false);
        pnlChiTiet.add(pnlNhap, BorderLayout.NORTH);
        String[] cotChiTiet = "Mã CTSP;Tên sản phẩm;Kích cỡ;Màu sắc;Số lượng trả;Số nhập lại".split(";");
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
        pnlTien.add(new JLabel("Tiền hàng hoàn chưa thuế (VND):"));
        pnlTien.add(txtTienHangHoan = new JTextField());
        pnlTien.add(new JLabel("Thuế hóa đơn gốc (%):"));
        pnlTien.add(txtThueGoc = new JTextField());
        pnlTien.add(new JLabel("Tiền thuế hoàn (VND):"));
        pnlTien.add(txtTienThueHoan = new JTextField());
        pnlTien.add(new JLabel("Tổng tiền hoàn (VND):"));
        pnlTien.add(txtTongTienHoan = new JTextField());
        txtTienHangHoan.setEditable(false);
        txtThueGoc.setEditable(false);
        txtTienThueHoan.setEditable(false);
        txtTongTienHoan.setEditable(false);
        pnlDuoi.add(pnlTien, BorderLayout.CENTER);
        JPanel pnlNut = new JPanel(new FlowLayout());
        pnlNut.add(btnLuu = new JButton("Lưu phiếu đổi trả"));
        pnlNut.add(btnLamMoi = new JButton("Làm mới"));
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        pnlPhieu.add(pnlDuoi, BorderLayout.SOUTH);
        pnlChinh.add(pnlPhieu);

        JPanel pnlHangDaMua = new JPanel(new BorderLayout());
        pnlHangDaMua.setBorder(BorderFactory.createTitledBorder("Hàng trong hóa đơn gốc"));
        String[] colHeader = {"Mã biến thể", "Tên giày/dép", "Size", "Màu",
                "Đã mua", "Đã trả", "Còn được trả", "Đơn giá gốc"};
        modelHangDaMua = new DefaultTableModel(colHeader, 0);
        tableHangDaMua = new JTable(modelHangDaMua);
        tableHangDaMua.setDefaultEditor(Object.class, null);
        pnlHangDaMua.add(new JScrollPane(tableHangDaMua), BorderLayout.CENTER);
        pnlChinh.add(pnlHangDaMua);
    }

    public static void main(String[] args) {
        new PhieuDoiTra_GUI().setVisible(true);
    }
}
