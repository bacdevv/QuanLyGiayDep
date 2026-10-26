# Quản lý giày dép

**Java 17 · Swing · SQL Server · VS Code · JDBC thuần · 3 lớp**  
**Mục tiêu:** phần mềm cho **nhân viên cửa hàng**, không có giao diện khách mua.  
**Trạng thái:** project khung. Chỉ `DBConnection` và `KiemTraKetNoi` đã có code.
**Lưu ý:**: Có thể code trên eclipse, xong thì dùng terminal và lệnh git push để push code lên cũng được

## 1. Phân công nhóm

| Thành viên                      | Phụ trách                                                                             | Class chính cần làm                                                                                                                                              | Hoàn thành khi                                                                       |
| ------------------------------- | ------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------ |
| **Trần Việt Bắc** (nhóm trưởng) | Thiết lập repo, kết nối DB, đăng nhập, phân quyền, màn hình chính, thống kê, tích hợp | `Main`, `DangNhapFrame`, `MainFrame`, `DangNhapBUS`, `TaiKhoanDAO`, `TaiKhoan`, `ThongKeFrame`, `ThongKeBUS`, `PasswordUtil`, `SessionContext`                   | Kết nối ổn, đăng nhập đúng quyền, menu theo vai trò, thống kê ngày/tháng/năm         |
| **Hoàng Thị Kim Ngần**          | Nhân viên, khách hàng, nhà cung cấp                                                   | `NhanVienFrame/BUS/DAO`, `KhachHangFrame/BUS/DAO`, `NhaCungCapDAO` và 3 `entity` tương ứng                                                                       | CRUD, tìm kiếm đơn giản/nâng cao, kiểm tra dữ liệu nhập và khóa ngoại                |
| **Nguyễn Phạm Đình Thi**        | Danh mục, thương hiệu, sản phẩm, size–màu, nhập hàng                                  | `SanPhamFrame/BUS/DAO`, `DanhMucDAO`, `ThuongHieuDAO`, `ChiTietSanPhamDAO`, `NhapHangFrame/BUS`, `PhieuNhapDAO`, `ChiTietPhieuNhapDAO` và `entity` liên quan     | CRUD biến thể, tìm kiếm; nhập nháp → chờ duyệt → hoàn tất, chỉ cộng tồn một lần      |
| **Hồng Phúc**                   | Bán hàng, hóa đơn, đổi/trả, in hóa đơn                                                | `HoaDonFrame/BUS/DAO`, `ChiTietHoaDonDAO`, `DoiTraFrame/BUS`, `PhieuDoiTraDAO`, `ChiTietDoiTraDAO`, `InvoicePrinter`, `TransactionManager` và `entity` liên quan | Thanh toán trừ tồn một lần; đổi/trả đúng hóa đơn, không trả quá số lượng; in hóa đơn |

**Quy ước:** Ai phụ trách nghiệp vụ thì tự hiện thực `GUI → BUS → DAO` và `entity` cần dùng. Bắc hỗ trợ merge/tích hợp, **không viết hộ chức năng**. Không sửa file của người khác trước khi trao đổi.

## 2. Các thư mục

```text
QuanLyGiayDep/
├── README.md             # Toàn bộ hướng dẫn nằm ở đây
├── src/
│   ├── app/              # Điểm chạy ứng dụng (chưa hiện thực)
│   ├── gui/              # Màn hình Swing
│   ├── bus/              # Kiểm tra và xử lý nghiệp vụ
│   ├── dao/              # Truy vấn dữ liệu bằng JDBC
│   ├── entity/           # Các lớp dữ liệu ứng với 14 bảng
│   └── util/             # Kết nối, phiên đăng nhập, in hóa đơn
├── sql/
│   ├── TaoCSDL.sql       # Tạo 14 bảng và ràng buộc
│   └── DuLieuMau.sql     # Dữ liệu liên kết giữa các bảng
├── erd/                  # Sơ đồ CSDL (Draw.io và DBML)
├── config/               # File cấu hình mẫu, không chứa mật khẩu thật
└── lib/                  # Tự thêm Microsoft JDBC Driver (.jar)
```

**3 lớp:** `GUI` nhận thao tác → `BUS` kiểm tra quy tắc → `DAO` dùng JDBC truy vấn SQL Server. `entity` chứa dữ liệu, **không truy vấn SQL trong GUI**.

## 3. Chuẩn bị VS Code và Java 17

1. Cài **JDK 17**, **VS Code**, **Git**, **SQL Server + SSMS**.
2. VS Code → Extensions → cài **Extension Pack for Java** (`vscjava.vscode-java-pack`) và **SQL Server (mssql)** (`ms-mssql.mssql`). Git đã có sẵn giao diện Source Control.
3. Mở đúng thư mục `QuanLyGiayDep` bằng **File → Open Folder**.
4. Kiểm tra terminal: `java -version`, `javac -version`, `git --version`. Yêu cầu Java **17**.
5. Nhấn `Ctrl+Shift+P` → **Java: Configure Java Runtime** → chọn **JDK 17**.
6. Không dùng Maven/Gradle; VS Code tự đọc `.vscode/settings.json` với `src`, `bin`, `lib/**/*.jar`.

## 4. Tạo database và chạy JDBC

1. Mở **SSMS** (hoặc extension SQL Server trong VS Code), đăng nhập SQL Server bằng SQL Authentication.
2. Chạy toàn bộ `sql/TaoCSDL.sql` trước; tiếp theo chạy **một lần** `sql/DuLieuMau.sql`.
3. Tải **Microsoft JDBC Driver for SQL Server** từ [Microsoft](https://learn.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server), giải nén và chép tệp `mssql-jdbc-13.6.0.jre11.jar` vào `lib/`. Bản **jre11 chạy được với Java 17**. Nếu có bản driver mới hơn, chọn đúng tệp `*.jre11.jar`.
4. Sao chép `config/db.properties.example` → `config/db.properties`; sửa `db.user`, `db.password`, `db.url` nếu cần.
5. Mở `src/util/KiemTraKetNoi.java` → bấm **Run Java**. Kết quả mong đợi: **Kết nối thành công**, **14 bảng**, **150 biến thể sản phẩm**.

**Lỗi nhanh:**

| Lỗi                          | Kiểm tra                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------- |
| `ClassNotFoundException`     | Thiếu file JDBC `*.jar` trong `lib/`, chọn **Java: Clean Java Language Server Workspace** rồi tải lại project |
| `Login failed for user 'sa'` | Sai mật khẩu; cần bật SQL Server Authentication (Mixed Mode)                                                  |
| `TCP/IP connection failed`   | Bật **TCP/IP**, kiểm tra port **1433**, dịch vụ SQL Server, firewall                                          |
| Thiếu `db.properties`        | Sao chép từ file `.example`; chỉ sửa bản riêng trên máy                                                       |
| `Invalid object name`        | Chưa chạy `TaoCSDL.sql` đúng database                                                                         |

`trustServerCertificate=true` chỉ phục vụ môi trường local; không dùng cấu hình này cho production. **Không commit mật khẩu**: `.gitignore` đã bỏ qua `config/db.properties` và JAR.

## 5. Git và GitHub cho 4 thành viên

**Bắc tạo 1 repository GitHub** tên `QuanLyGiayDep`, mời 3 bạn vào **Settings → Collaborators**, rồi đẩy project lần đầu (nếu chưa có Git):

```bash
git init
git add .
git commit -m "Khoi tao project"
git branch -M main
git remote add origin https://github.com/TAI_KHOAN/QuanLyGiayDep.git
git push -u origin main
```

**Ba bạn còn lại:** mở Terminal của VS Code tại thư mục muốn chứa project rồi clone:

```bash
git clone https://github.com/TAI_KHOAN/QuanLyGiayDep.git
cd QuanLyGiayDep
git switch -c ngan
```

Đổi `ngan` thành `thi`, `phuc` hoặc `bac` đúng người. Mỗi người **một branch riêng**, không code trực tiếp `main`.

**Mỗi ngày làm việc:**

```bash
git switch main
git pull origin main
git switch ngan
git merge main
# Viết code, sau đó:
git add .
git commit -m "Them chuc nang nhan vien"
git push -u origin ngan
```

Trên GitHub chọn **Compare & pull request → Create pull request**; **Bắc xem rồi Merge**. Sau khi merge, mọi người tiếp tục `git pull origin main`. Nếu gặp xung đột, cùng xem phần thay đổi rồi sửa; **không dùng `push --force`**.

**5 lệnh nhớ:** `clone` (lấy code) · `pull` (cập nhật) · `switch` (đổi nhánh) · `commit` (lưu thay đổi) · `push` (đẩy lên GitHub).

## 6. Trình tự làm và kiểm thử

| Bước | Việc cần xong                                                                  | Người kiểm tra  |
| ---- | ------------------------------------------------------------------------------ | --------------- |
| 1    | Setup Java 17, JDBC, SQL; cả 4 máy in ra 14 bảng                               | Bắc             |
| 2    | Chốt giao diện và class diagram từ `erd/` + 14 `entity`                        | Cả nhóm         |
| 3    | CRUD, tìm kiếm và màn hình của từng người                                      | Người phụ trách |
| 4    | Ghép đăng nhập → nhập hàng → bán hàng → đổi/trả → thống kê                     | Bắc và cả nhóm  |
| 5    | Thử lỗi: thiếu tồn, trùng SKU, khách vãng lai, trả 2 lần, thiếu quyền, ngắt DB | Cả nhóm         |
| 6    | Chụp màn hình, hoàn thiện 4 phần tài liệu, demo                                | Cả nhóm         |

**Quy tắc bắt buộc:**

- **Nhập hàng:** `NHAP → CHO_DUYET → DA_DUYET → HOAN_TAT`; nhân viên không tự duyệt; chỉ `HOAN_TAT` cộng tồn.
- **Bán hàng:** chỉ hóa đơn `DA_THANH_TOAN` mới trừ tồn; không để tồn âm; thanh toán dùng **transaction** và rollback khi lỗi.
- **Đổi/trả:** không vượt lượng đã bán trừ lượng đã trả; hàng lỗi không cộng tồn; `DOI` tạo hóa đơn mới, `TRA` không tạo hóa đơn mới; hoàn tiền/thu thêm đúng chênh lệch.
- **Phân quyền:** `QUAN_LY` quản lý nhân viên, phê duyệt nhập; `BAN_HANG` bán hàng, tạo yêu cầu. Quyền kiểm tra ở **BUS**, không chỉ ẩn nút GUI.
- **Dữ liệu:** hóa đơn đã chốt không xóa trực tiếp; với bản ghi đang được tham chiếu, cân nhắc **ngừng hoạt động** thay vì xóa.

## 7. Dữ liệu có sẵn

Tổng **641 bản ghi** trên 14 bảng, gồm **12 nhân viên, 150 biến thể, 80 hóa đơn, 25 phiếu nhập, 18 phiếu đổi/trả**. Có khách vãng lai, hóa đơn chờ thanh toán, phiếu chờ duyệt, hàng lỗi, đổi hàng chênh lệch giá.

**Lưu ý:** các giá trị demo đã phản ánh số tồn tại thời điểm cuối dữ liệu; **không chạy lại** các bước nhập/bán/đổi của giao dịch lịch sử để tránh cộng/trừ tồn lần hai. Script `DuLieuMau.sql` cố tình chặn chạy lại. Tài khoản demo chứa **hash BCrypt**, chức năng xác thực chưa được hiện thực.

**Tên file:** tiếng Việt không dấu, gọn, dùng PascalCase cho Java (`PhieuNhapDAO.java`), `README.md` là file Markdown **duy nhất**. Chú thích và hướng dẫn viết tiếng Việt có dấu.
