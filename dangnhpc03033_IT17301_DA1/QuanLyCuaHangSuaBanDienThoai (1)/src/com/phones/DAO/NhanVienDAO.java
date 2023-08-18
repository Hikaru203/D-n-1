/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.NhanVien;
import com.phones.model.SuaChua;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class NhanVienDAO {
    
    public void insert(NhanVien model) {
        String sql = "INSERT INTO NhanVien (MaNhanVien, HoTen, TaiKhoan,MatKhau,Email,SoDienThoai,DiaChi,Luong,ChucVu,HinhAnh) VALUES(?,?,?,?,?,?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaNhanVien(),
                model.getHoTen(),
                model.getTaiKhoan(),
                model.getMatKhau(),
                model.getEmail(),
                model.getSoDienThoai(),
                model.getDiaChi(),
                model.getLuong(),
                model.getChucVu(),
                model.getHinhAnh());
    }

    public void update(NhanVien model) {
        String sql = "UPDATE NhanVien set HoTen=?, TaiKhoan=?,MatKhau=?,Email=?,SoDienThoai=?,DiaChi=?,Luong=?,ChucVu=?,HinhAnh=? WHERE MaNhanVien=?";
        JdbcHelper.executeUpdate(sql,
                model.getHoTen(),
                model.getTaiKhoan(),
                model.getMatKhau(),
                model.getEmail(),
                model.getSoDienThoai(),
                model.getDiaChi(),
                model.getLuong(),
                model.getChucVu(),
                model.getHinhAnh(),
                model.getMaNhanVien());
    }

    public void delete(String MaNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNhanVien=?";
        JdbcHelper.executeUpdate(sql, MaNV);
    }
    
    public List<NhanVien> select() {
        String sql = "select * from NhanVien";
        return select(sql);
    }
    public List<NhanVien> selectByKeyword(String keyword){
        String sql="SELECT * FROM NhanVien WHERE HoTen LIKE ?";
        return select(sql, "%"+keyword+"%");
    }
    public List<NhanVien> selectByCourse(String taiKhoan){
        String sql="SELECT MaNhanVien from NhanVien WHERE TaiKhoan=?";
        return select(sql, taiKhoan);
    }
    public NhanVien findByIdLogin(String taikhoan) {
        String sql = "select * from NhanVien where TaiKhoan=?";
        List<NhanVien> list = select(sql, taikhoan);
        return list.size() > 0 ? list.get(0) : null;
    }
    public NhanVien findById(String MaKhachHanh) {
        String sql = "select * from NhanVien where MaNhanVien=?";
        List<NhanVien> list = select(sql, MaKhachHanh);
        return list.size() > 0 ? list.get(0) : null;
    }
    
    private List<NhanVien> select(String sql, Object...args){
        List<NhanVien> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    NhanVien model=readFromResultSet(rs);
                    list.add(model);
                }
            } 
            finally{
                rs.getStatement().getConnection().close();
            }
        } 
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
    
    private NhanVien readFromResultSet(ResultSet rs) throws SQLException{
        NhanVien model=new NhanVien();
        model.setMaNhanVien(rs.getString("maNhanVien"));
        model.setHoTen(rs.getString("hoTen"));
        model.setTaiKhoan(rs.getString("taiKhoan"));
        model.setMatKhau(rs.getString("matKhau"));
        model.setEmail(rs.getString("email"));
        model.setSoDienThoai(rs.getString("soDienThoai"));
        model.setDiaChi(rs.getString("diaChi"));
        model.setLuong(rs.getFloat("luong"));
        model.setChucVu(rs.getString("chucVu"));
        model.setHinhAnh(rs.getString("hinhAnh"));
        return model;
    }
}
