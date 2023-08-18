/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.HoaDon;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class HoaDonDao {

    public List<HoaDon> select() {
        String sql = "SELECT HoaDon.MaSuaChua,TenKhachHang,a.HoTen AS'KyThuatVien',b.HoTen AS'NhanVienNhanMay',ThanhTien,NgayLap,MoTa,ThoiGianBaoHanh,NgayBatDau FROM dbo.SuaChua INNER JOIN dbo.HoaDon ON HoaDon.MaSuaChua = SuaChua.MaSuaChua INNER JOIN dbo.May ON May.MaMay = SuaChua.MaMay INNER JOIN dbo.KhachHang ON KhachHang.MaKhachHang = SuaChua.MaKhachHang INNER JOIN dbo.NhanVien a ON a.MaNhanVien = SuaChua.MaKyThuatVien INNER JOIN dbo.NhanVien b ON b.MaNhanVien = SuaChua.MaNhanVienNhanMay INNER JOIN dbo.BaoHanh ON BaoHanh.MaSuaChua = HoaDon.MaSuaChua";
        return select(sql);
    }

    public List<HoaDon> select1(String MaSuaChua) {
        String sql = "select TongChiPhi from TongTienDua(?)";
        return select1(sql, MaSuaChua);
    }
    
    public void insert(HoaDon model){
        String sql="INSERT INTO BaoHanh (MaSuaChua, ThoiGianBaoHanh, NgayBatDau) VALUES (?,?,?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaSuaChua(), 
                model.getThoiGianBaoHanh(),
                model.getNgayBatDau());
    }
    public void insert1(HoaDon model){
        String sql="INSERT INTO HoaDon (MaHoaDon, MaSuaChua, NgayLap, ThanhTien) VALUES (?,?,?,?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaHoaDon(),
                model.getMaSuaChua(),
                model.getThoiGianLap(),
                model.getThanhTien());
    }

    public List<HoaDon> selectByKeyword(String keyword) {
        String sql = "SELECT HoaDon.MaSuaChua,HoaDon.MaHoaDon,TenKhachHang,a.HoTen AS'KyThuatVien',b.HoTen AS'NhanVienNhanMay',ThanhTien,NgayLap,MoTa,ThoiGianBaoHanh,NgayBatDau FROM dbo.SuaChua INNER JOIN dbo.HoaDon ON HoaDon.MaSuaChua = SuaChua.MaSuaChua INNER JOIN dbo.May ON May.MaMay = SuaChua.MaMay INNER JOIN dbo.KhachHang ON KhachHang.MaKhachHang = SuaChua.MaKhachHang INNER JOIN dbo.NhanVien a ON a.MaNhanVien = SuaChua.MaKyThuatVien INNER JOIN dbo.NhanVien b ON b.MaNhanVien = SuaChua.MaNhanVienNhanMay INNER JOIN dbo.BaoHanh ON BaoHanh.MaSuaChua = HoaDon.MaSuaChua WHERE TenKhachHang LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    public List<HoaDon> selectByKeyword1(String keyword) {
        String sql = "SELECT HoaDon.MaSuaChua,HoaDon.MaHoaDon,TenKhachHang,a.HoTen AS'KyThuatVien',b.HoTen AS'NhanVienNhanMay',ThanhTien,NgayLap,MoTa,ThoiGianBaoHanh,NgayBatDau FROM dbo.SuaChua INNER JOIN dbo.HoaDon ON HoaDon.MaSuaChua = SuaChua.MaSuaChua INNER JOIN dbo.May ON May.MaMay = SuaChua.MaMay INNER JOIN dbo.KhachHang ON KhachHang.MaKhachHang = SuaChua.MaKhachHang INNER JOIN dbo.NhanVien a ON a.MaNhanVien = SuaChua.MaKyThuatVien INNER JOIN dbo.NhanVien b ON b.MaNhanVien = SuaChua.MaNhanVienNhanMay INNER JOIN dbo.BaoHanh ON BaoHanh.MaSuaChua = HoaDon.MaSuaChua WHERE HoaDon.MaSuaChua = ?";
        return select(sql, keyword);
    }

    public HoaDon findById(String maSuaChua) {
        String sql = "SELECT HoaDon.MaSuaChua,HoaDon.MaHoaDon,TenKhachHang,a.HoTen AS'KyThuatVien',b.HoTen AS'NhanVienNhanMay',ThanhTien,NgayLap,MoTa,ThoiGianBaoHanh,NgayBatDau FROM dbo.SuaChua INNER JOIN dbo.HoaDon ON HoaDon.MaSuaChua = SuaChua.MaSuaChua INNER JOIN dbo.May ON May.MaMay = SuaChua.MaMay INNER JOIN dbo.KhachHang ON KhachHang.MaKhachHang = SuaChua.MaKhachHang INNER JOIN dbo.NhanVien a ON a.MaNhanVien = SuaChua.MaKyThuatVien INNER JOIN dbo.NhanVien b ON b.MaNhanVien = SuaChua.MaNhanVienNhanMay INNER JOIN dbo.BaoHanh ON BaoHanh.MaSuaChua = HoaDon.MaSuaChua where SuaChua.MaSuaChua=?";
        List<HoaDon> list = select(sql, maSuaChua);
        return list.size() > 0 ? list.get(0) : null;
    }


    private List<HoaDon> select(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HoaDon model = readFromResultSet(rs);
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

    private List<HoaDon> select1(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HoaDon model = readFromResultSet1(rs);
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

    private HoaDon readFromResultSet(ResultSet rs) throws SQLException {
        HoaDon model = new HoaDon();
        model.setMaHoaDon(rs.getString("MaHoaDon"));
        model.setMaSuaChua(rs.getString("MaSuaChua"));
        model.setNhanVienKyThuat(rs.getString("KyThuatVien"));
        model.setNhanVienTiepNhan(rs.getString("NhanVienNhanMay"));
        model.setTenKhachHang(rs.getString("TenKhachHang"));
        model.setThanhTien(rs.getDouble("ThanhTien"));
        model.setThoiGianLap(rs.getDate("NgayLap"));
        model.setMoTa(rs.getString("MoTa"));
        model.setThoiGianBaoHanh(rs.getInt("ThoiGianBaoHanh"));
        model.setNgayBatDau(rs.getDate("NgayBatDau"));

        return model;
    }

    private HoaDon readFromResultSet1(ResultSet rs) throws SQLException {
        HoaDon model = new HoaDon();
        model.setTongTien(rs.getDouble("TongChiPhi"));
        return model;
    }
}
