-- Dữ liệu giả lập, mật khẩu demo 123456. Chạy một lần sau TaoCSDL.sql.
USE QuanLyGiayDep;
GO
SET XACT_ABORT ON;
BEGIN TRY
BEGIN TRANSACTION;
IF EXISTS (SELECT 1 FROM dbo.NhanVien)
 OR EXISTS (SELECT 1 FROM dbo.TaiKhoan)
 OR EXISTS (SELECT 1 FROM dbo.DanhMuc)
 OR EXISTS (SELECT 1 FROM dbo.ThuongHieu)
 OR EXISTS (SELECT 1 FROM dbo.SanPham)
 OR EXISTS (SELECT 1 FROM dbo.ChiTietSanPham)
 OR EXISTS (SELECT 1 FROM dbo.KhachHang)
 OR EXISTS (SELECT 1 FROM dbo.HoaDon)
 OR EXISTS (SELECT 1 FROM dbo.ChiTietHoaDon)
    THROW 50002, N'Database đã có dữ liệu; không nạp mẫu lần nữa.', 1;
INSERT INTO dbo.NhanVien VALUES
('NV001',N'Nhân viên mẫu 1','0901000001',1),
('NV002',N'Nhân viên mẫu 2','0901000002',1),
('NV003',N'Nhân viên đã nghỉ',NULL,0),
('NV004',N'Nhân viên chưa có tài khoản','0901000004',1);
INSERT INTO dbo.TaiKhoan VALUES
('NV001','nv01','pbkdf2$600000$R2lheURlcERlbW9TYWx0MDE=$5w+tyZYmQ9J9Nks3qJIw+okqpK270mJXnasgnVDdmIo='),
('NV002','nv02','pbkdf2$600000$R2lheURlcERlbW9TYWx0MDE=$5w+tyZYmQ9J9Nks3qJIw+okqpK270mJXnasgnVDdmIo=');
INSERT INTO dbo.DanhMuc VALUES ('DM001',N'Giày thể thao'),('DM002',N'Sandal'),('DM003',N'Dép');
INSERT INTO dbo.ThuongHieu VALUES ('TH001',N'Biti''s'),('TH002',N'Ananas'),('TH003',N'Không thương hiệu');
INSERT INTO dbo.SanPham VALUES
('SP001','DM001','TH001',N'Giày thể thao mẫu A',1),
('SP002','DM001','TH002',N'Giày vải mẫu B',1),
('SP003','DM003','TH003',N'Dép quai ngang',1),
('SP004','DM002','TH001',N'Sandal mẫu C',0);
-- Tồn hiện tại đã trừ lượng bán trong HD001; tồn đầu CT001=10, CT003=8.
INSERT INTO dbo.ChiTietSanPham VALUES
('CT001','SP001',N'39',N'Trắng',500000,8,1),
('CT002','SP001',N'40',N'Đen',500000,10,1),
('CT003','SP002',N'40',N'Trắng',400000,7,1),
('CT004','SP003',N'41',N'Đen',150000,0,1),
('CT005','SP004',N'38',N'Nâu',300000,5,0);
INSERT INTO dbo.KhachHang VALUES ('KH001',N'Khách hàng mẫu','0902000001'),('KH002',N'Khách không có số điện thoại',NULL);
INSERT INTO dbo.HoaDon(maHD,maNhanVien,maKH,ngayLap,phuongThucThanhToan)
VALUES ('HD001','NV002','KH001',SYSDATETIME(),'TIENMAT');
INSERT INTO dbo.ChiTietHoaDon VALUES ('HD001','CT001',2,500000),('HD001','CT003',1,400000);
COMMIT TRANSACTION;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
THROW;
END CATCH;
GO
-- HD001 = 1.400.000 VND.
SELECT * FROM dbo.v_TongTienHoaDon;
