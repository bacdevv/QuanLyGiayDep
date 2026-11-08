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

public class HoaDon_GUI extends JFrame {
    private JTextField txtMaHD, txtNgayLap;
    private JTextField txtTienHang, txtThue, txtTienThue, txtTongTien;
    private JTextField txtTimMaCTSP, txtTimTenSP;
    private JComboBox<String> cboNhanVien, cboKhachHang, cboThanhToan;
    private JButton btnTim, btnLamMoiSanPham, btnTaoHD, btnLamMoiHD, btnXuatHD;
    private JTable tableSanPham;
    private DefaultTableModel modelSanPham;
    private JTextField txtMaCTSP, txtSoLuong, txtDonGia;
    private JButton btnThem, btnSuaSoLuong, btnXoa;
    private JTable tableChiTietHoaDon;
    private DefaultTableModel modelChiTietHoaDon;
    private JLabel lblHinhAnh;

    private static final long serialVersionUID = 1L;

    public HoaDon_GUI() {
        setTitle("Quản lý giày dép - Hóa đơn");
        setSize(1280, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 5));

        JLabel lblTieuDe = new JLabel("QUẢN LÝ GIÀY DÉP - HÓA ĐƠN BÁN HÀNG",
                SwingConstants.CENTER);
        add(lblTieuDe, BorderLayout.NORTH);
        JPanel pnlChinh = new JPanel(new GridLayout(1, 2, 10, 0));
        add(pnlChinh, BorderLayout.CENTER);

        JPanel pnlHoaDon = new JPanel(new BorderLayout(5, 5));
        pnlHoaDon.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        JPanel pnlThongTin = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlThongTin.add(new JLabel("Mã hóa đơn:"));
        pnlThongTin.add(txtMaHD = new JTextField());
        pnlThongTin.add(new JLabel("Ngày lập:"));
        pnlThongTin.add(txtNgayLap = new JTextField());
        pnlThongTin.add(new JLabel("Nhân viên:"));
        pnlThongTin.add(cboNhanVien = new JComboBox<String>());
        pnlThongTin.add(new JLabel("Khách hàng:"));
        pnlThongTin.add(cboKhachHang = new JComboBox<String>());
        cboKhachHang.addItem("Khách lẻ");
        txtMaHD.setEditable(false);
        txtNgayLap.setEditable(false);
        pnlHoaDon.add(pnlThongTin, BorderLayout.NORTH);

        JPanel pnlChiTiet = new JPanel(new BorderLayout(5, 5));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));
        JPanel pnlNhap = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlNhap.add(new JLabel("Mã biến thể:"));
        pnlNhap.add(txtMaCTSP = new JTextField());
        pnlNhap.add(new JLabel("Số lượng:"));
        pnlNhap.add(txtSoLuong = new JTextField());
        pnlNhap.add(new JLabel("Đơn giá chưa thuế (VND):"));
        pnlNhap.add(txtDonGia = new JTextField());
        txtMaCTSP.setEditable(false);
        txtDonGia.setEditable(false);
        pnlChiTiet.add(pnlNhap, BorderLayout.NORTH);
        String[] cotChiTiet = "Mã CTSP;Tên sản phẩm;Kích cỡ;Màu sắc;Số lượng;Đơn giá;Thành tiền".split(";");
        modelChiTietHoaDon = new DefaultTableModel(cotChiTiet, 0);
        tableChiTietHoaDon = new JTable(modelChiTietHoaDon);
        tableChiTietHoaDon.setDefaultEditor(Object.class, null);
        pnlChiTiet.add(new JScrollPane(tableChiTietHoaDon), BorderLayout.CENTER);
        JPanel pnlNutChiTiet = new JPanel(new FlowLayout());
        pnlNutChiTiet.add(btnThem = new JButton("Thêm dòng"));
        pnlNutChiTiet.add(btnSuaSoLuong = new JButton("Sửa số lượng"));
        pnlNutChiTiet.add(btnXoa = new JButton("Xóa dòng"));
        pnlChiTiet.add(pnlNutChiTiet, BorderLayout.SOUTH);
        pnlHoaDon.add(pnlChiTiet, BorderLayout.CENTER);

        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        JPanel pnlTien = new JPanel(new GridLayout(5, 2, 5, 5));
        pnlTien.add(new JLabel("Tiền hàng chưa thuế (VND):"));
        pnlTien.add(txtTienHang = new JTextField());
        pnlTien.add(new JLabel("Thuế (%):"));
        pnlTien.add(txtThue = new JTextField("0"));
        pnlTien.add(new JLabel("Tiền thuế (VND):"));
        pnlTien.add(txtTienThue = new JTextField());
        pnlTien.add(new JLabel("Tổng thanh toán (VND):"));
        pnlTien.add(txtTongTien = new JTextField());
        pnlTien.add(new JLabel("Phương thức thanh toán:"));
        pnlTien.add(cboThanhToan = new JComboBox<String>(
                new String[] { "Tiền mặt", "Chuyển khoản" }));
        txtTienHang.setEditable(false);
        txtTienThue.setEditable(false);
        txtTongTien.setEditable(false);
        pnlDuoi.add(pnlTien, BorderLayout.CENTER);
        JPanel pnlNut = new JPanel(new FlowLayout());
        pnlNut.add(btnTaoHD = new JButton("Tạo hóa đơn"));
        pnlNut.add(btnLamMoiHD = new JButton("Làm mới"));
        pnlNut.add(btnXuatHD = new JButton("Xuất hóa đơn"));
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        pnlHoaDon.add(pnlDuoi, BorderLayout.SOUTH);
        pnlChinh.add(pnlHoaDon);

        JPanel pnlSanPham = new JPanel(new BorderLayout(5, 5));
        pnlSanPham.setBorder(BorderFactory.createTitledBorder("Chọn sản phẩm"));
        JPanel pnlTim = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlTim.add(new JLabel("Mã biến thể:"));
        pnlTim.add(txtTimMaCTSP = new JTextField());
        pnlTim.add(new JLabel("Tên giày/dép:"));
        pnlTim.add(txtTimTenSP = new JTextField());
        pnlTim.add(btnTim = new JButton("Tìm"));
        pnlTim.add(btnLamMoiSanPham = new JButton("Làm mới danh sách"));
        pnlSanPham.add(pnlTim, BorderLayout.NORTH);
        String[] colHeader = { "Mã biến thể", "Tên giày/dép", "Size", "Màu",
                "Giá chưa thuế", "Tồn kho" };
        modelSanPham = new DefaultTableModel(colHeader, 0);
        tableSanPham = new JTable(modelSanPham);
        tableSanPham.setDefaultEditor(Object.class, null);
        pnlSanPham.add(new JScrollPane(tableSanPham), BorderLayout.CENTER);
        lblHinhAnh = new JLabel("Hình ảnh sản phẩm", SwingConstants.CENTER);
        lblHinhAnh.setPreferredSize(new java.awt.Dimension(0, 150));
        lblHinhAnh.setBorder(BorderFactory.createTitledBorder("Hình ảnh"));
        pnlSanPham.add(lblHinhAnh, BorderLayout.SOUTH);
        pnlChinh.add(pnlSanPham);
    }

    public static void main(String[] args) {
        new HoaDon_GUI().setVisible(true);
    }
}
