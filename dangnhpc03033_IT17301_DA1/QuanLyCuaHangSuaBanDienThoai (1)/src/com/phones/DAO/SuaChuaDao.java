/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.SuaChua;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class SuaChuaDao {

    public List<SuaChua> select() {
        String sql = "SELECT dbo.SuaChua.MaMay, dbo.SuaChua.MaNhanVienNhanMay, dbo.SuaChua.MaKyThuatVien, dbo.SuaChua.MaKhachHang, dbo.Hang.TenHang AS TenHangMay, dbo.May.TenMay, dbo.SuaChua.MoTaLoi, dbo.SuaChua.TinhTrangMay,dbo.SuaChua.MatKhauMay, dbo.SuaChua.NgayNhanMay, b.HoTen AS NhanVienNhanMay, a.HoTen AS KyThuatVien, dbo.SuaChua.PhiSuaChua, dbo.SuaChua.MoTa, dbo.SuaChua.NgayTraMayDuKien, dbo.SuaChua.TrangThai,dbo.SuaChua.NgayNhanMayThucTe, dbo.KhachHang.TenKhachHang, dbo.SuaChua.MaSuaChua  FROM dbo.SuaChua INNER JOIN dbo.KhachHang ON dbo.KhachHang.MaKhachHang = dbo.SuaChua.MaKhachHang INNER JOIN dbo.NhanVien AS a ON a.MaNhanVien = dbo.SuaChua.MaKyThuatVien INNER JOIN dbo.May ON dbo.May.MaMay = dbo.SuaChua.MaMay  inner JOIN dbo.NhanVien AS b ON b.MaNhanVien = dbo.SuaChua.MaNhanVienNhanMay inner JOIN dbo.Hang ON dbo.Hang.MaHang = dbo.May.MaHang";
        return select(sql);
    }
    public List<SuaChua> select1(String maSuaChua) {
        String sql = "SELECT SuaChua.MaSuaChua, LinhKien.TenHang AS 'TenLinhKien',SuaChua_LinhKien.MaLinhKien as 'MaLinhKien' FROM SuaChua INNER JOIN SuaChua_LinhKien ON SuaChua.MaSuaChua = SuaChua_LinhKien.MaSuaChua INNER JOIN LinhKien ON SuaChua_LinhKien.MaLinhKien = LinhKien.MaLinhKien where SuaChua.MaSuaChua=?";
        return select1(sql, maSuaChua);
    }
    public SuaChua findById(String maSuaChua) {
        String sql = "SELECT dbo.SuaChua.MaMay, dbo.SuaChua.MaNhanVienNhanMay, dbo.SuaChua.MaKyThuatVien, dbo.SuaChua.MaKhachHang, dbo.Hang.TenHang AS TenHangMay, dbo.May.TenMay, dbo.SuaChua.MoTaLoi, dbo.SuaChua.TinhTrangMay,dbo.SuaChua.MatKhauMay, dbo.SuaChua.NgayNhanMay, b.HoTen AS NhanVienNhanMay, a.HoTen AS KyThuatVien, dbo.SuaChua.PhiSuaChua, dbo.SuaChua.MoTa, dbo.SuaChua.NgayTraMayDuKien, dbo.SuaChua.TrangThai,dbo.SuaChua.NgayNhanMayThucTe, dbo.KhachHang.TenKhachHang, dbo.SuaChua.MaSuaChua  FROM dbo.SuaChua INNER JOIN dbo.KhachHang ON dbo.KhachHang.MaKhachHang = dbo.SuaChua.MaKhachHang INNER JOIN dbo.NhanVien AS a ON a.MaNhanVien = dbo.SuaChua.MaKyThuatVien INNER JOIN dbo.May ON dbo.May.MaMay = dbo.SuaChua.MaMay  inner JOIN dbo.NhanVien AS b ON b.MaNhanVien = dbo.SuaChua.MaNhanVienNhanMay inner JOIN dbo.Hang ON dbo.Hang.MaHang = dbo.May.MaHang WHERE SuaChua.MaSuaChua=?";
        List<SuaChua> list = select(sql, maSuaChua);
        return list.size() > 0 ? list.get(0) : null;
    }
    public SuaChua select2(String maSuaChua) {
        String sql = "SELECT SUM(LinhKien.GiaBan) as'Tong' FROM SuaChua INNER JOIN SuaChua_LinhKien ON SuaChua.MaSuaChua = SuaChua_LinhKien.MaSuaChua INNER JOIN LinhKien ON SuaChua_LinhKien.MaLinhKien = LinhKien.MaLinhKien where SuaChua.MaSuaChua=?";
        List<SuaChua> list = select2(sql, maSuaChua);
        return list.size() > 0 ? list.get(0) : null;
    }

    public void insert(SuaChua model) {
        String sql = "INSERT INTO SuaChua(MaSuaChua, MaMay, TinhTrangMay, MoTaLoi, MatKhauMay, MaKhachHang, NgayNhanMay, MaNhanVienNhanMay, MaKyThuatVien, NgayTraMayDuKien, TrangThai, NgayNhanMayThucTe) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaSuaChua(),
                model.getMaMay(),
                model.getTinhTrangMay(),
                model.getMoTaLoi(),
                model.getMatkhauMay(),
                model.getMaKhachHang(),
                model.getNgayNhanMay(),
                model.getMaNhanVienNhan(),
                model.getMaNhanVienSua(),
                model.getNgayTraMayDuKien(),
                model.getTrangThai(),
                model.getNgayTraMayThucTe());
    }
    public void insert1(SuaChua model) {
        String sql = "INSERT INTO SuaChua_LinhKien (MaSuaChua, MaLinhKien) VALUES (?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaSuaChua(),
                model.getMaLinhKien());
    }

    public void updateSL(SuaChua model) {
        String sql = "UPDATE dbo.LinhKien SET SoLuong=SoLuong-1 where MaLinhKien=?";
        JdbcHelper.executeUpdate(sql,
                model.getMaLinhKien());
    }
    public void updateSL2(SuaChua model) {
        String sql = "UPDATE dbo.LinhKien SET SoLuong=SoLuong+1 where MaLinhKien=?";
        JdbcHelper.executeUpdate(sql,
                model.getMaLinhKien());
    }
    public void updateSL1(SuaChua model) {
        String sql = "UPDATE SuaChua SET TrangThai = ? WHERE SuaChua.MaSuaChua =?";
        JdbcHelper.executeUpdate(sql,
                model.getTrangThai(),
                model.getMaSuaChua());
    }
    public void delete1(String MaSuaChua) {
        String sql = "DELETE FROM SuaChua_LinhKien WHERE MaLinhKien=?";
        JdbcHelper.executeUpdate(sql, MaSuaChua);
    }

    public void update(SuaChua model) {
        String sql = "UPDATE SuaChua SET MaMay =?, TinhTrangMay =?, MoTaLoi =?,MatKhauMay=?, MaKhachHang =?, NgayNhanMay =?, MaNhanVienNhanMay =?, MaKyThuatVien =?, PhiSuaChua =?, NgayTraMayDuKien =?, TrangThai =?, NgayNhanMayThucTe =?, MoTa =? WHERE MaSuaChua=?";
        JdbcHelper.executeUpdate(sql,
                model.getMaMay(),
                model.getTinhTrangMay(),
                model.getMoTaLoi(),
                model.getMatkhauMay(),
                model.getMaKhachHang(),
                model.getNgayNhanMay(),
                model.getMaNhanVienNhan(),
                model.getMaNhanVienSua(),
                model.getPhiSuaChua(),
                model.getNgayTraMayDuKien(),
                model.getTrangThai(),
                model.getNgayTraMayThucTe(),
                model.getMoTa(),
                model.getMaSuaChua());
    }
    public void update1(SuaChua model) {
        String sql = "UPDATE SuaChua SET NgayNhanMayThucTe =? WHERE MaSuaChua=?";
        JdbcHelper.executeUpdate(sql,
                model.getNgayTraMayThucTe(),
                model.getMaSuaChua());
    }


    public void delete(String MaSuaChua) {
        String sql = "DELETE FROM SuaChua WHERE MaSuaChua=?";
        JdbcHelper.executeUpdate(sql, MaSuaChua);
    }

    public List<SuaChua> selectByKeyword(String keyword) {
        String sql = "SELECT dbo.SuaChua.MaMay, dbo.SuaChua.MaNhanVienNhanMay, dbo.SuaChua.MaKyThuatVien, dbo.SuaChua.MaKhachHang, dbo.Hang.TenHang AS TenHangMay, dbo.May.TenMay, dbo.SuaChua.MoTaLoi, dbo.SuaChua.TinhTrangMay,dbo.SuaChua.MatKhauMay, dbo.SuaChua.NgayNhanMay, b.HoTen AS NhanVienNhanMay, a.HoTen AS KyThuatVien, dbo.SuaChua.PhiSuaChua, dbo.SuaChua.MoTa, dbo.SuaChua.NgayTraMayDuKien, dbo.SuaChua.TrangThai,dbo.SuaChua.NgayNhanMayThucTe, dbo.KhachHang.TenKhachHang, dbo.SuaChua.MaSuaChua  FROM dbo.SuaChua INNER JOIN dbo.KhachHang ON dbo.KhachHang.MaKhachHang = dbo.SuaChua.MaKhachHang INNER JOIN dbo.NhanVien AS a ON a.MaNhanVien = dbo.SuaChua.MaKyThuatVien INNER JOIN dbo.May ON dbo.May.MaMay = dbo.SuaChua.MaMay  inner JOIN dbo.NhanVien AS b ON b.MaNhanVien = dbo.SuaChua.MaNhanVienNhanMay inner JOIN dbo.Hang ON dbo.Hang.MaHang = dbo.May.MaHang WHERE SuaChua.MaMay LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    public List<SuaChua> selectByKeywordNVKT(String keyword) {
        String sql = "SELECT dbo.SuaChua.MaMay, dbo.SuaChua.MaNhanVienNhanMay, dbo.SuaChua.MaKyThuatVien, dbo.SuaChua.MaKhachHang, dbo.Hang.TenHang AS TenHangMay, dbo.May.TenMay, dbo.SuaChua.MoTaLoi, dbo.SuaChua.TinhTrangMay,dbo.SuaChua.MatKhauMay, dbo.SuaChua.NgayNhanMay, b.HoTen AS NhanVienNhanMay, a.HoTen AS KyThuatVien, dbo.SuaChua.PhiSuaChua, dbo.SuaChua.MoTa, dbo.SuaChua.NgayTraMayDuKien, dbo.SuaChua.TrangThai,dbo.SuaChua.NgayNhanMayThucTe, dbo.KhachHang.TenKhachHang, dbo.SuaChua.MaSuaChua  FROM dbo.SuaChua INNER JOIN dbo.KhachHang ON dbo.KhachHang.MaKhachHang = dbo.SuaChua.MaKhachHang INNER JOIN dbo.NhanVien AS a ON a.MaNhanVien = dbo.SuaChua.MaKyThuatVien INNER JOIN dbo.May ON dbo.May.MaMay = dbo.SuaChua.MaMay  inner JOIN dbo.NhanVien AS b ON b.MaNhanVien = dbo.SuaChua.MaNhanVienNhanMay inner JOIN dbo.Hang ON dbo.Hang.MaHang = dbo.May.MaHang WHERE SuaChua.MaKyThuatVien LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    private List<SuaChua> select(String sql, Object... args) {
        List<SuaChua> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SuaChua model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private List<SuaChua> select1(String sql, Object... args) {
        List<SuaChua> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SuaChua model = readFromResultSet1(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
    private List<SuaChua> select2(String sql, Object... args) {
        List<SuaChua> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SuaChua model = readFromResultSet2(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    private SuaChua readFromResultSet(ResultSet rs) throws SQLException {
        SuaChua model = new SuaChua();
        model.setMaSuaChua(rs.getString("MaSuaChua"));
        model.setTenMay(rs.getString("TenMay"));
        model.setTenHang(rs.getString("TenHangMay"));
        model.setMoTaLoi(rs.getString("moTaLoi"));
        model.setTinhTrangMay(rs.getString("tinhTrangMay"));
        model.setMatkhauMay(rs.getString("matKhauMay"));
        model.setNgayNhanMay(rs.getDate("ngayNhanMay"));
        model.setNhanVienNhanMay(rs.getString("NhanVienNhanMay"));
        model.setKyThuatVien(rs.getString("KyThuatVien"));
        model.setPhiSuaChua(rs.getFloat("phiSuaChua"));
        model.setMoTa(rs.getString("MoTa"));
        model.setNgayTraMayDuKien(rs.getDate("ngayTraMayDuKien"));
        model.setTrangThai(rs.getString("TrangThai"));
        model.setNgayTraMayThucTe(rs.getDate("NgayNhanMayThucTe"));
        model.setTenKhachHang(rs.getString("tenKhachHang"));
        model.setMaMay(rs.getString("MaMay"));
        model.setMaNhanVienNhan(rs.getString("MaNhanVienNhanMay"));
        model.setMaNhanVienSua(rs.getString("MaKyThuatVien"));
        model.setMaKhachHang(rs.getString("MaKhachHang"));
        return model;
    }

    private SuaChua readFromResultSet1(ResultSet rs) throws SQLException {
        SuaChua model = new SuaChua();
        model.setMaSuaChua(rs.getString("MaSuaChua"));
        model.setTenLinhKien(rs.getString("TenLinhKien"));
        model.setMaLinhKien(rs.getString("MaLinhKien"));
        return model;
    }
    private SuaChua readFromResultSet2(ResultSet rs) throws SQLException {
        SuaChua model = new SuaChua();
        model.setT(rs.getFloat("Tong"));
        return model;
    }
}
