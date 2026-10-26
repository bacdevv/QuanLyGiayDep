-- Cơ sở dữ liệu quản lý cửa hàng giày dép | SQL Server 2017 trở lên.
-- Gồm 14 bảng, 18 khóa ngoại. Chỉ chạy khi khởi tạo cơ sở dữ liệu mới.
-- Không có tích điểm, voucher, giảm giá hoặc bảng liên kết NCC - sản phẩm.
-- Tiền bù trừ đổi trả dùng để ghi nhận giá trị hàng cũ trong hóa đơn mới.
IF DB_ID(N'QuanLyGiayDep') IS NULL CREATE DATABASE QuanLyGiayDep;
GO
USE QuanLyGiayDep;
GO
SET NOCOUNT ON;
GO

CREATE TABLE dbo.NHANVIEN (
 MaNhanVien varchar(12) NOT NULL PRIMARY KEY,
 TenNhanVien nvarchar(100) NOT NULL,
 NgaySinh date NULL,
 SoDienThoai varchar(15) NOT NULL,
 Email varchar(100) NULL,
 CCCD varchar(12) NULL,
 DiaChi nvarchar(255) NULL,
 GioiTinh nvarchar(10) NULL,
 ChucVu nvarchar(50) NOT NULL,
 CaLam nvarchar(30) NULL,
 TrangThai varchar(20) NOT NULL CONSTRAINT DF_NV_TT DEFAULT 'DANG_LAM',
 HinhAnh nvarchar(255) NULL,
 CONSTRAINT CK_NV_TT CHECK (TrangThai IN ('DANG_LAM','NGHI_VIEC')),
 CONSTRAINT CK_NV_GT CHECK (GioiTinh IS NULL OR GioiTinh IN (N'Nam',N'Nữ',N'Khác')),
 CONSTRAINT CK_NV_SDT CHECK (LEN(SoDienThoai) BETWEEN 9 AND 15)
);
GO
CREATE TABLE dbo.TAIKHOAN (
 MaTK varchar(12) NOT NULL PRIMARY KEY,
 MaNhanVien varchar(12) NOT NULL UNIQUE,
 TenDangNhap varchar(50) NOT NULL UNIQUE,
 MatKhau varchar(255) NOT NULL,
 VaiTro varchar(20) NOT NULL,
 HoatDong bit NOT NULL CONSTRAINT DF_TK_HD DEFAULT 1,
 NgayTao datetime2 NOT NULL CONSTRAINT DF_TK_NT DEFAULT SYSDATETIME(),
 CONSTRAINT FK_TK_NV FOREIGN KEY (MaNhanVien) REFERENCES dbo.NHANVIEN(MaNhanVien),
 CONSTRAINT CK_TK_VT CHECK (VaiTro IN ('QUAN_LY','BAN_HANG'))
);
GO
CREATE TABLE dbo.DANHMUC (
 MaDanhMuc varchar(12) NOT NULL PRIMARY KEY,
 TenDanhMuc nvarchar(100) NOT NULL UNIQUE,
 HoatDong bit NOT NULL CONSTRAINT DF_DM_HD DEFAULT 1
);
GO
CREATE TABLE dbo.THUONGHIEU (
 MaThuongHieu varchar(12) NOT NULL PRIMARY KEY,
 TenThuongHieu nvarchar(100) NOT NULL UNIQUE,
 HoatDong bit NOT NULL CONSTRAINT DF_TH_HD DEFAULT 1
);
GO
CREATE TABLE dbo.SANPHAM (
 MaSP varchar(12) NOT NULL PRIMARY KEY,
 MaDanhMuc varchar(12) NOT NULL,
 MaThuongHieu varchar(12) NOT NULL,
 TenSP nvarchar(150) NOT NULL,
 ChatLieu nvarchar(100) NULL,
 MoTa nvarchar(500) NULL,
 HinhAnh nvarchar(255) NULL,
 HoatDong bit NOT NULL CONSTRAINT DF_SP_HD DEFAULT 1,
 CONSTRAINT FK_SP_DM FOREIGN KEY (MaDanhMuc) REFERENCES dbo.DANHMUC(MaDanhMuc),
 CONSTRAINT FK_SP_TH FOREIGN KEY (MaThuongHieu) REFERENCES dbo.THUONGHIEU(MaThuongHieu)
);
GO
CREATE TABLE dbo.CHITIETSANPHAM (
 MaCTSP varchar(12) NOT NULL PRIMARY KEY,
 MaSP varchar(12) NOT NULL,
 SKU varchar(40) NOT NULL UNIQUE,
 KichCo nvarchar(10) NOT NULL,
 MauSac nvarchar(30) NOT NULL,
 GiaBan decimal(18,0) NOT NULL,
 SoLuongTon int NOT NULL CONSTRAINT DF_CTSP_TON DEFAULT 0,
 MucTonToiThieu int NOT NULL CONSTRAINT DF_CTSP_MUC DEFAULT 2,
 HoatDong bit NOT NULL CONSTRAINT DF_CTSP_HD DEFAULT 1,
 CONSTRAINT FK_CTSP_SP FOREIGN KEY (MaSP) REFERENCES dbo.SANPHAM(MaSP),
 CONSTRAINT UQ_CTSP_BIENTHE UNIQUE (MaSP,KichCo,MauSac),
 CONSTRAINT CK_CTSP_GIA CHECK (GiaBan>0),
 CONSTRAINT CK_CTSP_TON CHECK (SoLuongTon>=0 AND MucTonToiThieu>=0)
);
GO
CREATE TABLE dbo.NHACUNGCAP (
 MaNCC varchar(12) NOT NULL PRIMARY KEY,
 TenNCC nvarchar(150) NOT NULL,
 SoDienThoai varchar(15) NULL,
 Email varchar(100) NULL,
 DiaChi nvarchar(255) NULL,
 HoatDong bit NOT NULL CONSTRAINT DF_NCC_HD DEFAULT 1
);
GO
CREATE TABLE dbo.KHACHHANG (
 MaKH varchar(12) NOT NULL PRIMARY KEY,
 TenKH nvarchar(100) NOT NULL,
 NamSinh smallint NULL,
 GioiTinh nvarchar(10) NULL,
 SoDienThoai varchar(15) NOT NULL UNIQUE,
 DiaChi nvarchar(255) NULL,
 CONSTRAINT CK_KH_NAM CHECK (NamSinh IS NULL OR NamSinh BETWEEN 1900 AND 2100),
 CONSTRAINT CK_KH_SDT CHECK (LEN(SoDienThoai) BETWEEN 9 AND 15)
);
GO
CREATE TABLE dbo.HOADON (
 MaHD varchar(12) NOT NULL PRIMARY KEY,
 MaNhanVien varchar(12) NOT NULL,
 MaKH varchar(12) NULL,                     -- NULL: khách vãng lai
 NgayLap datetime2 NOT NULL CONSTRAINT DF_HD_NL DEFAULT SYSDATETIME(),
 TrangThai varchar(20) NOT NULL CONSTRAINT DF_HD_TT DEFAULT 'CHO_THANH_TOAN',
 PhuongThucThanhToan varchar(20) NULL,       -- Tiền mặt / chuyển khoản / quẹt thẻ / bù trừ
 TongThanhToan decimal(18,0) NOT NULL CONSTRAINT DF_HD_TONG DEFAULT 0,
 TienBuTruDoiTra decimal(18,0) NOT NULL CONSTRAINT DF_HD_BUTRU DEFAULT 0,
 TienKhachDua decimal(18,0) NULL,            -- Số tiền khách thực đưa khi trả tiền mặt
 NgayThanhToan datetime2 NULL,
 CONSTRAINT FK_HD_NV FOREIGN KEY(MaNhanVien) REFERENCES dbo.NHANVIEN(MaNhanVien),
 CONSTRAINT FK_HD_KH FOREIGN KEY(MaKH) REFERENCES dbo.KHACHHANG(MaKH),
 CONSTRAINT CK_HD_TT CHECK (TrangThai IN ('CHO_THANH_TOAN','DA_THANH_TOAN','HUY')),
 CONSTRAINT CK_HD_PT CHECK (PhuongThucThanhToan IS NULL OR PhuongThucThanhToan IN ('TIEN_MAT','CHUYEN_KHOAN','QUET_THE','BU_TRU')),
 CONSTRAINT CK_HD_TIEN CHECK (TongThanhToan>=0 AND TienBuTruDoiTra>=0 AND TienBuTruDoiTra<=TongThanhToan AND (TienKhachDua IS NULL OR TienKhachDua>=0)),
 CONSTRAINT CK_HD_CHOT CHECK (
   (TrangThai='DA_THANH_TOAN' AND NgayThanhToan IS NOT NULL AND PhuongThucThanhToan IS NOT NULL)
   OR (TrangThai IN ('CHO_THANH_TOAN','HUY') AND NgayThanhToan IS NULL)
 )
);
GO
CREATE TABLE dbo.CHITIETHOADON (
 MaCTHD varchar(12) NOT NULL PRIMARY KEY,
 MaHD varchar(12) NOT NULL,
 MaCTSP varchar(12) NOT NULL,
 SoLuong int NOT NULL,
 DonGia decimal(18,0) NOT NULL,
 ThanhTien AS CONVERT(decimal(18,0),SoLuong*DonGia) PERSISTED,
 CONSTRAINT FK_CTHD_HD FOREIGN KEY(MaHD) REFERENCES dbo.HOADON(MaHD),
 CONSTRAINT FK_CTHD_CTSP FOREIGN KEY(MaCTSP) REFERENCES dbo.CHITIETSANPHAM(MaCTSP),
 CONSTRAINT UQ_CTHD_DONG UNIQUE(MaHD,MaCTSP),
 CONSTRAINT CK_CTHD_TIEN CHECK(SoLuong>0 AND DonGia>0)
);
GO
CREATE TABLE dbo.PHIEUNHAP (
 MaPN varchar(12) NOT NULL PRIMARY KEY,
 MaNhanVien varchar(12) NOT NULL,
 MaNCC varchar(12) NOT NULL,
 MaNhanVienDuyet varchar(12) NULL,
 NgayLap datetime2 NOT NULL CONSTRAINT DF_PN_NL DEFAULT SYSDATETIME(),
 NgayDuyet datetime2 NULL,
 NgayHoanTat datetime2 NULL,
 TrangThai varchar(20) NOT NULL CONSTRAINT DF_PN_TT DEFAULT 'NHAP',
 TongTien decimal(18,0) NOT NULL CONSTRAINT DF_PN_TONG DEFAULT 0,
 GhiChu nvarchar(500) NULL,
 CONSTRAINT FK_PN_NV FOREIGN KEY(MaNhanVien) REFERENCES dbo.NHANVIEN(MaNhanVien),
 CONSTRAINT FK_PN_NV_DUYET FOREIGN KEY(MaNhanVienDuyet) REFERENCES dbo.NHANVIEN(MaNhanVien),
 CONSTRAINT FK_PN_NCC FOREIGN KEY(MaNCC) REFERENCES dbo.NHACUNGCAP(MaNCC),
 CONSTRAINT CK_PN_TT CHECK(TrangThai IN ('NHAP','CHO_DUYET','DA_DUYET','HOAN_TAT','HUY','TU_CHOI')),
 CONSTRAINT CK_PN_TONG CHECK(TongTien>=0),
 CONSTRAINT CK_PN_DUYET CHECK ((MaNhanVienDuyet IS NULL AND NgayDuyet IS NULL) OR (MaNhanVienDuyet IS NOT NULL AND NgayDuyet IS NOT NULL))
);
GO
CREATE TABLE dbo.CHITIETPHIEUNHAP (
 MaCTPN varchar(12) NOT NULL PRIMARY KEY,
 MaPN varchar(12) NOT NULL,
 MaCTSP varchar(12) NOT NULL,
 SoLuong int NOT NULL,
 DonGiaNhap decimal(18,0) NOT NULL,
 ThanhTien AS CONVERT(decimal(18,0),SoLuong*DonGiaNhap) PERSISTED,
 CONSTRAINT FK_CTPN_PN FOREIGN KEY(MaPN) REFERENCES dbo.PHIEUNHAP(MaPN),
 CONSTRAINT FK_CTPN_CTSP FOREIGN KEY(MaCTSP) REFERENCES dbo.CHITIETSANPHAM(MaCTSP),
 CONSTRAINT UQ_CTPN_DONG UNIQUE(MaPN,MaCTSP),
 CONSTRAINT CK_CTPN_DG CHECK(SoLuong>0 AND DonGiaNhap>0)
);
GO
CREATE TABLE dbo.PHIEUDOITRA (
 MaPDT varchar(12) NOT NULL PRIMARY KEY,
 MaHDCu varchar(12) NOT NULL,
 MaHDMoi varchar(12) NULL,
 MaNhanVien varchar(12) NOT NULL,
 LoaiXuLy varchar(10) NOT NULL,        -- Đổi hàng có hóa đơn mới; trả hàng không có hóa đơn mới
 NgayLap datetime2 NOT NULL CONSTRAINT DF_PDT_NL DEFAULT SYSDATETIME(),
 NgayHoanTat datetime2 NULL,
 LyDo nvarchar(255) NOT NULL,
 TrangThai varchar(20) NOT NULL CONSTRAINT DF_PDT_TT DEFAULT 'NHAP',
 TongGiaTriTra decimal(18,0) NOT NULL CONSTRAINT DF_PDT_TRA DEFAULT 0,
 TongGiaTriDoi decimal(18,0) NOT NULL CONSTRAINT DF_PDT_DOI DEFAULT 0,
 SoTienHoan decimal(18,0) NOT NULL CONSTRAINT DF_PDT_HOAN DEFAULT 0,
 SoTienThuThem decimal(18,0) NOT NULL CONSTRAINT DF_PDT_THU DEFAULT 0,
 PhuongThucHoanTien varchar(20) NULL,
 PhuongThucThuThem varchar(20) NULL,
 GhiChu nvarchar(500) NULL,
 CONSTRAINT FK_PDT_HD_CU FOREIGN KEY(MaHDCu) REFERENCES dbo.HOADON(MaHD),
 CONSTRAINT FK_PDT_HD_MOI FOREIGN KEY(MaHDMoi) REFERENCES dbo.HOADON(MaHD),
 CONSTRAINT FK_PDT_NV FOREIGN KEY(MaNhanVien) REFERENCES dbo.NHANVIEN(MaNhanVien),
 CONSTRAINT CK_PDT_LOAI CHECK(LoaiXuLy IN ('DOI','TRA')),
 CONSTRAINT CK_PDT_TT CHECK(TrangThai IN ('NHAP','HOAN_TAT','HUY')),
 CONSTRAINT CK_PDT_HOA_DON CHECK ((LoaiXuLy='DOI' AND MaHDMoi IS NOT NULL AND MaHDMoi<>MaHDCu) OR (LoaiXuLy='TRA' AND MaHDMoi IS NULL)),
 CONSTRAINT CK_PDT_TIEN CHECK(TongGiaTriTra>=0 AND TongGiaTriDoi>=0 AND SoTienHoan>=0 AND SoTienThuThem>=0 AND (SoTienHoan=0 OR SoTienThuThem=0)),
 CONSTRAINT CK_PDT_PT CHECK ((PhuongThucHoanTien IS NULL OR PhuongThucHoanTien IN ('TIEN_MAT','CHUYEN_KHOAN','QUET_THE')) AND (PhuongThucThuThem IS NULL OR PhuongThucThuThem IN ('TIEN_MAT','CHUYEN_KHOAN','QUET_THE'))),
 CONSTRAINT CK_PDT_HT CHECK ((TrangThai='HOAN_TAT' AND NgayHoanTat IS NOT NULL) OR (TrangThai<>'HOAN_TAT' AND NgayHoanTat IS NULL))
);
GO
-- Một hóa đơn mới không được gắn vào hai phiếu đổi trả.
CREATE UNIQUE INDEX UX_PDT_HDMOI ON dbo.PHIEUDOITRA(MaHDMoi) WHERE MaHDMoi IS NOT NULL;
GO
CREATE TABLE dbo.CHITIETDOITRA (
 MaCTDT varchar(12) NOT NULL PRIMARY KEY,
 MaPDT varchar(12) NOT NULL,
 MaCTSP varchar(12) NOT NULL,          -- Chỉ ghi hàng cũ; hàng mới thuộc chi tiết hóa đơn mới
 SoLuong int NOT NULL,
 DonGiaHoan decimal(18,0) NOT NULL,   -- Giá bán gốc của sản phẩm trả
 ThanhTienHoan AS CONVERT(decimal(18,0),SoLuong*DonGiaHoan) PERSISTED,
 TinhTrangHang varchar(20) NOT NULL,
 CONSTRAINT FK_CTDT_PDT FOREIGN KEY(MaPDT) REFERENCES dbo.PHIEUDOITRA(MaPDT),
 CONSTRAINT FK_CTDT_CTSP FOREIGN KEY(MaCTSP) REFERENCES dbo.CHITIETSANPHAM(MaCTSP),
 CONSTRAINT UQ_CTDT_DONG UNIQUE(MaPDT,MaCTSP),
 CONSTRAINT CK_CTDT_SL CHECK(SoLuong>0 AND DonGiaHoan>0),
 CONSTRAINT CK_CTDT_TT CHECK(TinhTrangHang IN ('BAN_LAI_DUOC','HANG_LOI'))
);
GO
CREATE INDEX IX_HD_NGAY ON dbo.HOADON(TrangThai,NgayThanhToan) INCLUDE(TongThanhToan);
CREATE INDEX IX_PDT_NGAY ON dbo.PHIEUDOITRA(TrangThai,NgayHoanTat) INCLUDE(TongGiaTriTra);
CREATE INDEX IX_CTHD_CTSP ON dbo.CHITIETHOADON(MaCTSP);
CREATE INDEX IX_CTDT_CTSP ON dbo.CHITIETDOITRA(MaCTSP);
CREATE INDEX IX_PDT_HD_CU ON dbo.PHIEUDOITRA(MaHDCu,TrangThai);
GO
-- Quy tắc nghiệp vụ: không xóa chứng từ đã hoàn tất; phải giữ lịch sử giao dịch.
-- Mật khẩu tài khoản lưu dạng BCrypt, không lưu mật khẩu gốc.
PRINT N'Đã tạo 14 bảng. Chạy tiếp DuLieuMau.sql.';
GO
