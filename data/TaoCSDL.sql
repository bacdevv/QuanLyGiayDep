-- Tạo database mới hoặc dùng database rỗng; không thay đổi database cũ có bảng.
USE master;
GO
IF DB_ID(N'QuanLyGiayDep') IS NULL
    EXEC(N'CREATE DATABASE QuanLyGiayDep');
GO
USE QuanLyGiayDep;
GO
SET XACT_ABORT ON;
BEGIN TRY
    BEGIN TRANSACTION;
    IF EXISTS (SELECT 1 FROM sys.tables WHERE is_ms_shipped = 0)
        THROW 50001, N'Database đã có bảng. Không chạy trên database cũ; hãy dùng database mới.', 1;
CREATE TABLE dbo.NhanVien (
    maNhanVien varchar(12) NOT NULL PRIMARY KEY,
    tenNhanVien nvarchar(100) NOT NULL,
    soDienThoai varchar(15) NULL,
    hoatDong bit NOT NULL DEFAULT 1
);

CREATE TABLE dbo.TaiKhoan (
    maNhanVien varchar(12) NOT NULL PRIMARY KEY,
    tenDangNhap varchar(50) NOT NULL UNIQUE,
    matKhauHash varchar(255) NOT NULL,
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNhanVien)
        REFERENCES dbo.NhanVien(maNhanVien)
);

CREATE TABLE dbo.DanhMuc (
    maDanhMuc varchar(12) NOT NULL PRIMARY KEY,
    tenDanhMuc nvarchar(100) NOT NULL UNIQUE
);

CREATE TABLE dbo.ThuongHieu (
    maThuongHieu varchar(12) NOT NULL PRIMARY KEY,
    tenThuongHieu nvarchar(100) NOT NULL UNIQUE
);

CREATE TABLE dbo.SanPham (
    maSP varchar(12) NOT NULL PRIMARY KEY,
    maDanhMuc varchar(12) NOT NULL,
    maThuongHieu varchar(12) NOT NULL,
    tenSP nvarchar(150) NOT NULL,
    hoatDong bit NOT NULL DEFAULT 1,
    CONSTRAINT FK_SanPham_DanhMuc FOREIGN KEY (maDanhMuc)
        REFERENCES dbo.DanhMuc(maDanhMuc),
    CONSTRAINT FK_SanPham_ThuongHieu FOREIGN KEY (maThuongHieu)
        REFERENCES dbo.ThuongHieu(maThuongHieu)
);

CREATE TABLE dbo.ChiTietSanPham (
    maCTSP varchar(12) NOT NULL PRIMARY KEY,
    maSP varchar(12) NOT NULL,
    kichCo nvarchar(10) NOT NULL,
    mauSac nvarchar(30) NOT NULL,
    giaBan decimal(18,0) NOT NULL,
    soLuongTon int NOT NULL DEFAULT 0,
    hoatDong bit NOT NULL DEFAULT 1,
    CONSTRAINT FK_ChiTietSanPham_SanPham FOREIGN KEY (maSP)
        REFERENCES dbo.SanPham(maSP),
    CONSTRAINT UQ_ChiTietSanPham_BienThe UNIQUE (maSP, kichCo, mauSac),
    CONSTRAINT CK_ChiTietSanPham_Gia CHECK (giaBan >= 0),
    CONSTRAINT CK_ChiTietSanPham_Ton CHECK (soLuongTon >= 0),
    CONSTRAINT CK_ChiTietSanPham_Size CHECK (LEN(LTRIM(RTRIM(kichCo))) > 0),
    CONSTRAINT CK_ChiTietSanPham_Mau CHECK (LEN(LTRIM(RTRIM(mauSac))) > 0)
);

CREATE TABLE dbo.KhachHang (
    maKH varchar(12) NOT NULL PRIMARY KEY,
    tenKH nvarchar(100) NOT NULL,
    soDienThoai varchar(15) NULL
);

CREATE TABLE dbo.HoaDon (
    maHD varchar(12) NOT NULL PRIMARY KEY,
    maNhanVien varchar(12) NOT NULL,
    maKH varchar(12) NULL,
    ngayLap datetime2(0) NOT NULL DEFAULT SYSDATETIME(),
    phuongThucThanhToan varchar(20) NOT NULL,
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien)
        REFERENCES dbo.NhanVien(maNhanVien),
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKH)
        REFERENCES dbo.KhachHang(maKH),
    CONSTRAINT CK_HoaDon_ThanhToan CHECK
        (phuongThucThanhToan IN ('TIENMAT', 'CHUYENKHOAN'))
);

CREATE TABLE dbo.ChiTietHoaDon (
    maHD varchar(12) NOT NULL,
    maCTSP varchar(12) NOT NULL,
    soLuong int NOT NULL,
    donGia decimal(18,0) NOT NULL,
    CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (maHD, maCTSP),
    CONSTRAINT FK_ChiTietHoaDon_HoaDon FOREIGN KEY (maHD)
        REFERENCES dbo.HoaDon(maHD),
    CONSTRAINT FK_ChiTietHoaDon_SanPham FOREIGN KEY (maCTSP)
        REFERENCES dbo.ChiTietSanPham(maCTSP),
    CONSTRAINT CK_ChiTietHoaDon_SoLuong CHECK (soLuong > 0),
    CONSTRAINT CK_ChiTietHoaDon_DonGia CHECK (donGia >= 0)
);


EXEC(N'CREATE VIEW dbo.v_TongTienHoaDon
AS
SELECT h.maHD,
       COALESCE(SUM(CAST(c.soLuong AS decimal(10,0)) * c.donGia), 0) AS tongTien
FROM dbo.HoaDon AS h
LEFT JOIN dbo.ChiTietHoaDon AS c ON c.maHD = h.maHD
GROUP BY h.maHD;');
COMMIT TRANSACTION;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
THROW;
END CATCH;
GO
