package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SanPham_GUI extends JFrame {

    private JButton btnChonAnh;
    private JButton btnNCC;
    private JTextField txtmaSP;
    private JTextField txttenSP;
    private JTextField txtGiaBan;
    private JTextField txtSoLuong;
    private JTextField txtTrangThai;
    private JComboBox cboNCC;
    private JComboBox cboLoaiSP;
    private JComboBox cboHanSuDung;
    private JComboBox cboDonVi;
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JTextField txtTimMaSP;
    private JTextField txtTimTenSP;
    private JComboBox cboTimLoaiSP;
    private JComboBox cboTimTrangThai;
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
        lblTieuDe.setForeground(new Color(90, 145, 180));

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

      
        JPanel pNCC = new JPanel(new BorderLayout());
        btnNCC = new JButton("Nhà cung cấp");

        pNCC.add(btnNCC, BorderLayout.EAST);
        pBenPhai.add(pNCC, BorderLayout.NORTH);

        
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
        JLabel lblNCC = new JLabel("Nhà cung cấp: ");
        JLabel lblLoaiSP = new JLabel("Loại sản phẩm: ");
        JLabel lblGiaBan = new JLabel("Giá bán: ");
        JLabel lblHanSuDung = new JLabel("Hạn sử dụng: ");
        JLabel lblSoLuong = new JLabel("Số lượng: ");
        JLabel lblDonVi = new JLabel("Đơn vị: ");
        JLabel lblTrangThai = new JLabel("Trạng thái: ");

        Dimension dim = new Dimension(105, 28);

        JLabel[] nhan = {
            lblmaSP, lbltenSP, lblNCC, lblLoaiSP,
            lblGiaBan, lblHanSuDung, lblSoLuong, lblTrangThai
        };

        for (JLabel lbl : nhan) {
            lbl.setPreferredSize(dim);
        }

        txtmaSP = new JTextField(18);
        txttenSP = new JTextField(18);
        txtGiaBan = new JTextField(18);
        txtSoLuong = new JTextField(6);
        txtTrangThai = new JTextField(18);
        txtTrangThai.setEditable(false);

        cboNCC = new JComboBox<>(new String[]{
            "-- Chọn nhà cung cấp --"
        });

        cboLoaiSP = new JComboBox<>(new String[]{
            "-- Chọn loại sản phẩm --"
        });

        cboHanSuDung = new JComboBox<>(new String[]{
            "-- Hạn sử dụng --",
            "1 tháng", "6 tháng", "9 tháng", "12 tháng"
        });

        cboDonVi = new JComboBox<>(new String[]{
            "-- Đơn vị --",
            "Cái", "Đôi", "Hộp", "Chai", "Lon", "Thùng"
        });

       
        b1.add(lblmaSP);
        b1.add(txtmaSP);
        b1.add(Box.createHorizontalStrut(20));
        b1.add(lbltenSP);
        b1.add(txttenSP);

       
        b2.add(lblNCC);
        b2.add(cboNCC);
        b2.add(Box.createHorizontalStrut(20));
        b2.add(lblLoaiSP);
        b2.add(cboLoaiSP);

     
        b3.add(lblGiaBan);
        b3.add(txtGiaBan);
        b3.add(Box.createHorizontalStrut(20));
        b3.add(lblHanSuDung);
        b3.add(cboHanSuDung);

 
        JPanel pSoLuong = new JPanel(new BorderLayout(10, 0));
        pSoLuong.add(txtSoLuong, BorderLayout.CENTER);

        Box bDonVi = Box.createHorizontalBox();
        bDonVi.add(lblDonVi);
        bDonVi.add(cboDonVi);

        pSoLuong.add(bDonVi, BorderLayout.EAST);

        b4.add(lblSoLuong);
        b4.add(pSoLuong);
        b4.add(Box.createHorizontalStrut(20));
        b4.add(lblTrangThai);
        b4.add(txtTrangThai);

        b.add(b1);
        b.add(Box.createVerticalStrut(10));
        b.add(b2);
        b.add(Box.createVerticalStrut(10));
        b.add(b3);
        b.add(Box.createVerticalStrut(10));
        b.add(b4);

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

        cboTimLoaiSP = new JComboBox<>(new String[]{
            "-- Tất cả loại sản phẩm --"
        });

        cboTimTrangThai = new JComboBox<>(new String[]{
            "-- Tất cả trạng thái --",
            "Còn hàng", "Hết hàng", "Sắp hết hàng"
        });

        bTim.add(new JLabel("Mã sản phẩm: "));
        bTim.add(txtTimMaSP);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Tên sản phẩm: "));
        bTim.add(txtTimTenSP);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Loại sản phẩm: "));
        bTim.add(cboTimLoaiSP);
        bTim.add(Box.createHorizontalStrut(15));

        bTim.add(new JLabel("Trạng thái: "));
        bTim.add(cboTimTrangThai);

        pTimKiem.add(bTim, BorderLayout.CENTER);

     
        String[] cot = {"Mã sản phẩm","Tên sản phẩm","Loại sản phẩm","Giá bán","Số lượng","Đơn vị","Nhà cung cấp","Hạn sử dụng","Trạng thái"};

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