/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.KhachHang;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class KhachHangDao {
    
    public void insert(KhachHang model) {
        String sql = "INSERT INTO KhachHang(MaKhachHang, TenKhachHang, DiaChi,Email,SoDienThoai) VALUES(?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaKhachHanh(),
                model.getTenKhachHang(),
                model.getDiaChi(),
                model.getEmail(),
                model.getSoDienThoai());
    }

    public void update(KhachHang model) {
        String sql = "UPDATE KhachHang SET TenKhachHang=?, Email=?, DiaChi=?,SoDienThoai=? WHERE MaKhachHang=?";
        JdbcHelper.executeUpdate(sql,
                model.getTenKhachHang(),
                model.getEmail(),
                model.getDiaChi(),
                model.getSoDienThoai(),
                model.getMaKhachHanh());
    }

    public void delete(String MaKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKhachHang=?";
        JdbcHelper.executeUpdate(sql, MaKH);
    }
    
    public List<KhachHang> selectByKeyword(String keyword){
        String sql="SELECT * FROM KhachHang WHERE TenKhachHang LIKE ?";
        return select(sql, "%"+keyword+"%");
    }
    
    public List<KhachHang> select() {
        String sql = "select * from KhachHang";
        return select(sql);
    }
    public KhachHang findById(String MaKhachHanh) {
        String sql = "select * from KhachHang where MaKhachHang=?";
        List<KhachHang> list = select(sql, MaKhachHanh);
        return list.size() > 0 ? list.get(0) : null;
    }
    private List<KhachHang> select(String sql, Object...args){
        List<KhachHang> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    KhachHang model=readFromResultSet(rs);
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
    
    private KhachHang readFromResultSet(ResultSet rs) throws SQLException{
        KhachHang model=new KhachHang();
        model.setMaKhachHanh(rs.getString("MaKhachHang"));
        model.setTenKhachHang(rs.getString("TenKhachHang"));
        model.setDiaChi(rs.getString("DiaChi"));
        model.setEmail(rs.getString("Email"));
        model.setSoDienThoai(rs.getString("SoDienThoai"));
        return model;
    }
}
