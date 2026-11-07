package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SanPham_GUI extends JFrame {

    private JButton btnChonAnh;
    private JButton btnThuongHieu;
    private JButton btnDanhMuc;
    private JTextField txtmaSP;
    private JTextField txttenSP;
    private JTextField txtGiaBan;
    private JTextField txtSoLuong;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboThuongHieu;
    private JComboBox<String> cboDanhMuc;
    private JComboBox<String> cboKichCo;
    private JComboBox<String> cboMauSac;
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JTextField txtTimMaSP;
    private JTextField txtTimTenSP;
    private JComboBox<String> cboTimDanhMuc;
    private JComboBox<String> cboTimTrangThai;
    private DefaultTableModel model;
    private JTable table;

    public SanPham_GUI() {
        setTitle("Sản phẩm");
        setSize(1250, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        
        JPanel pTren = new JPanel();

        JLabel lblTieuDe = new JLabel("SẢN PHẨM");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);

        pTren.add(lblTieuDe);
        p.add(pTren, BorderLayout.NORTH);

        JPanel pGiua = new JPanel(new BorderLayout(0, 15));
        p.add(pGiua, BorderLayout.CENTER);


        JPanel pNhap = new JPanel(new BorderLayout(25, 0));
        pGiua.add(pNhap, BorderLayout.NORTH);

        JPanel pAnh = new JPanel(new BorderLayout(0, 10));
        pAnh.setPreferredSize(new Dimension(200, 250));

        JLabel lblAnh = new JLabel("Ảnh sản phẩm", SwingConstants.CENTER);
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.LIGHT_GRAY);
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pAnh.add(lblAnh, BorderLayout.CENTER);

        JPanel pChonAnh = new JPanel();
        btnChonAnh = new JButton("Chọn ảnh");
        pChonAnh.add(btnChonAnh);

        pAnh.add(pChonAnh, BorderLayout.SOUTH);
        pNhap.add(pAnh, BorderLayout.WEST);

        JPanel pBenPhai = new JPanel(new BorderLayout(0, 10));
        pNhap.add(pBenPhai, BorderLayout.CENTER);

        JPanel pQuanLy = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        btnThuongHieu = new JButton("Thương hiệu");
        btnDanhMuc = new JButton("Danh mục");
        pQuanLy.add(btnThuongHieu);
        pQuanLy.add(btnDanhMuc);
        pBenPhai.add(pQuanLy, BorderLayout.NORTH);

      


        
        JPanel pThongTin = new JPanel(new BorderLayout(0, 15));
        pThongTin.setBorder(
            BorderFactory.createTitledBorder("Thông tin")
        );
        pBenPhai.add(pThongTin, BorderLayout.CENTER);

        Box b = Box.createVerticalBox();
        Box b1 = Box.createHorizontalBox();
        Box b2 = Box.createHorizontalBox();
        Box b3 = Box.createHorizontalBox();
        Box b4 = Box.createHorizontalBox();

        JLabel lblmaSP = new JLabel("Mã sản phẩm: ");
        JLabel lbltenSP = new JLabel("Tên sản phẩm: ");
        JLabel lblThuongHieu = new JLabel("Thương hiệu: ");
        JLabel lblDanhMuc = new JLabel("Danh mục: ");
        JLabel lblGiaBan = new JLabel("Giá bán: ");
        JLabel lblKichCo = new JLabel("Kích cỡ: ");
        JLabel lblSoLuong = new JLabel("Tồn kho (đôi): ");
        JLabel lblMauSac = new JLabel("Màu sắc: ");
        JLabel lblTrangThai = new JLabel("Trạng thái: ");

        Dimension dim = new Dimension(105, 28);

        JLabel[] nhan = {
            lblmaSP, lbltenSP, lblThuongHieu, lblDanhMuc,
            lblGiaBan, lblKichCo, lblSoLuong, lblTrangThai
        };

        for (JLabel lbl : nhan) {
            lbl.setPreferredSize(dim);
        }

        txtmaSP = new JTextField(18);
        txttenSP = new JTextField(18);
        txtGiaBan = new JTextField(18);
        txtSoLuong = new JTextField(6);
        cboTrangThai = new JComboBox<>(new String[]{"Đang kinh doanh", "Ngừng kinh doanh"});

        cboThuongHieu = new JComboBox<>(new String[]{
            "-- Chọn thương hiệu --"
        });

        cboDanhMuc = new JComboBox<>(new String[]{
            "-- Chọn danh mục --"
        });

        cboKichCo = new JComboBox<>(new String[]{
            "-- Kích cỡ --",
            "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45"
        });

        cboMauSac = new JComboBox<>(new String[]{
            "-- Màu sắc --",
            "Đen", "Trắng", "Nâu", "Xám", "Xanh", "Đỏ", "Be"
        });

       
        b1.add(lblmaSP);
        b1.add(txtmaSP);
        b1.add(Box.createHorizontalStrut(20));
        b1.add(lbltenSP);
        b1.add(txttenSP);

       
        b2.add(lblThuongHieu);
        b2.add(cboThuongHieu);
        b2.add(Box.createHorizontalStrut(20));
        b2.add(lblDanhMuc);
        b2.add(cboDanhMuc);

     
        b3.add(lblGiaBan);
        b3.add(txtGiaBan);
        b3.add(Box.createHorizontalStrut(20));
        b3.add(lblKichCo);
        b3.add(cboKichCo);

 
        JPanel pSoLuong = new JPanel(new BorderLayout(10, 0));
        pSoLuong.add(txtSoLuong, BorderLayout.CENTER);

        Box bMauSac = Box.createHorizontalBox();
        bMauSac.add(lblMauSac);
        bMauSac.add(cboMauSac);

        pSoLuong.add(bMauSac, BorderLayout.EAST);

        b4.add(lblSoLuong);
        b4.add(pSoLuong);
        b4.add(Box.createHorizontalStrut(20));
        b4.add(lblTrangThai);
        b4.add(cboTrangThai);

        Dimension kichThuoc = new Dimension(Integer.MAX_VALUE, 28);
        b1.setMaximumSize(kichThuoc);
        b2.setMaximumSize(kichThuoc);
        b3.setMaximumSize(kichThuoc);
        b4.setMaximumSize(kichThuoc);

        b.add(b1);
        b.add(Box.createVerticalStrut(10));
        b.add(b2);
        b.add(Box.createVerticalStrut(10));
        b.add(b3);
        b.add(Box.createVerticalStrut(10));
        b.add(b4);
        b.add(Box.createVerticalGlue());

        pThongTin.add(b, BorderLayout.CENTER);

   
        JPanel pNut = new JPanel();

        btnThem = new JButton("Thêm");
        btnLuu = new JButton("Lưu");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xoá");
        btnLamMoi = new JButton("Làm mới");

        pNut.add(btnThem);
        pNut.add(btnLuu);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);

        pThongTin.add(pNut, BorderLayout.SOUTH);

        JPanel pDanhSach = new JPanel(new BorderLayout(0, 15));
        pGiua.add(pDanhSach, BorderLayout.CENTER);

        JPanel pTimKiem = new JPanel(new BorderLayout());
        pTimKiem.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
        pDanhSach.add(pTimKiem, BorderLayout.NORTH);

        Box bTim = Box.createHorizontalBox();

        txtTimMaSP = new JTextField(10);
        txtTimTenSP = new JTextField(10);

        cboTimDanhMuc = new JComboBox<>(new String[]{
            "-- Tất cả danh mục --"
        });

        cboTimTrangThai = new JComboBox<>(new String[]{
            "-- Tất cả trạng thái --",
            "Đang kinh doanh", "Ngừng kinh doanh"
        });

        bTim.add(new JLabel("Mã sản phẩm: "));
        bTim.add(txtTimMaSP);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Tên sản phẩm: "));
        bTim.add(txtTimTenSP);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Danh mục: "));
        bTim.add(cboTimDanhMuc);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Trạng thái: "));
        bTim.add(cboTimTrangThai);

        pTimKiem.add(bTim, BorderLayout.CENTER);

     
        String[] cot = {"Mã sản phẩm","Tên sản phẩm","Danh mục","Giá bán","Tồn kho (đôi)","Màu sắc","Thương hiệu","Kích cỡ","Trạng thái"};

        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setDefaultEditor(Object.class, null);

        JScrollPane srp = new JScrollPane(table);
        pDanhSach.add(srp, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new SanPham_GUI().setVisible(true);
    }
}
