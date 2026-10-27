package bus;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import util.PasswordUtil;
import util.SessionContext;

import java.sql.SQLException;
import java.util.List;

public class TaiKhoanBUS {
    private final TaiKhoanDAO dao;

    public TaiKhoanBUS() {
        this(new TaiKhoanDAO());
    }

    public TaiKhoanBUS(TaiKhoanDAO dao) {
        this.dao = dao;
    }

    public List<TaiKhoan> findAll(String keyword) throws SQLException {
        requireManager();
        return dao.findAll(keyword);
    }

    public List<TaiKhoanDAO.EmployeeOption> findActiveEmployees() throws SQLException {
        requireManager();
        return dao.findActiveEmployees();
    }

    public void create(TaiKhoan account, String plainPassword) throws SQLException {
        requireManager();
        validate(account, plainPassword, true);
        if (!dao.isActiveEmployeeAvailable(account.getMaNhanVien(), null)) {
            throw new IllegalArgumentException("Nhân viên không tồn tại, đã nghỉ việc hoặc đã có tài khoản");
        }
        account.setMatKhau(PasswordUtil.hash(plainPassword));
        dao.insert(account);
    }

    public void update(TaiKhoan account, String plainPassword) throws SQLException {
        requireManager();
        validate(account, plainPassword, false);
        if (!dao.isActiveEmployeeAvailable(account.getMaNhanVien(), account.getMaTK())) {
            throw new IllegalArgumentException("Chỉ được cấp tài khoản cho nhân viên đang làm việc");
        }
        boolean changePassword = plainPassword != null && !plainPassword.isBlank();
        if (changePassword)
            account.setMatKhau(PasswordUtil.hash(plainPassword));
        dao.update(account, changePassword);
    }

    public void delete(String id) throws SQLException {
        requireManager();
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Chưa chọn tài khoản");
        dao.delete(id);
    }

    private static void validate(TaiKhoan a, String password, boolean creating) {
        if (a == null)
            throw new IllegalArgumentException("Dữ liệu tài khoản không hợp lệ");
        check(a.getMaTK(), "Mã tài khoản", 12);
        check(a.getMaNhanVien(), "Mã nhân viên", 12);
        check(a.getTenDangNhap(), "Tên đăng nhập", 50);
        if (!"QUAN_LY".equals(a.getVaiTro()) && !"BAN_HANG".equals(a.getVaiTro())) {
            throw new IllegalArgumentException("Vai trò chỉ được là QUAN_LY hoặc BAN_HANG");
        }
        if (creating && (password == null || password.isBlank())) {
            throw new IllegalArgumentException("Mật khẩu không được trống khi tạo tài khoản");
        }
        if (password != null && !password.isBlank() && password.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
    }

    private static void check(String value, String label, int maxLength) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException(label + " không được trống");
        if (value.trim().length() > maxLength)
            throw new IllegalArgumentException(label + " không được quá " + maxLength + " ký tự");
    }

    private static void requireManager() {
        SessionContext.requireManager();
    }
}
