/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.LinhKien;
import com.phones.model.SuaChua;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class LinhKienDao {
    public void insert(LinhKien model) {
        String sql = "INSERT INTO LinhKien( MaLinhKien, TenHang, SoLuong, GiaBan, Mota) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaLinhKien(),
                model.getTenHang(),
                model.getSoLuong(),
                model.getGiaBan(),
                model.getMoTa());
    }
        
    public void insertNhap(LinhKien model) {
        String sql = "INSERT INTO LinhKien( MaLinhKien, TenHang,SoLuong) VALUES (?, ?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaLinhKien(),
                model.getTenHang(),
                model.getSoLuong());
    }
    
    public void update(LinhKien model){
        String sql="UPDATE LinhKien SET MaLinhKien=?, TenHang=?, SoLuong=?, GiaBan=?, Mota=? WHERE MaLinhKien=?";
        JdbcHelper.executeUpdate(sql, 
                model.getMaLinhKien(), 
                model.getTenHang(),
                model.getSoLuong(),
                model.getGiaBan(),
                model.getMoTa(),
                model.getMaLinhKien());
    }
    
    public void update2(LinhKien model){
        String sql="UPDATE dbo.LinhKien SET SoLuong=SoLuong+? WHERE TenHang LIKE ?";
        JdbcHelper.executeUpdate(sql, 
                model.getSoLuong(),
                model.getTenHang());
    }
     public void delete(String MaLinhKien){
        String sql="DELETE FROM LinhKien WHERE MaLinhKien=?";
        JdbcHelper.executeUpdate(sql, MaLinhKien);
    }
      public List<LinhKien> selectByKeyword(String keyword){
        String sql="SELECT * FROM LinhKien WHERE TenHang LIKE ?";
        return select(sql, "%"+keyword+"%");
    }
     
    public List<LinhKien> select() {
        String sql = "select * from LinhKien";
        return select(sql);
    }
    public LinhKien findById(String MaKhachHanh) {
        String sql = "select * from LinhKien where MaLinhKien=?";
        List<LinhKien> list = select(sql, MaKhachHanh);
        return list.size() > 0 ? list.get(0) : null;
    }
    public LinhKien findByTen(String MaKhachHanh) {
        String sql = "select * from LinhKien where TenHang like ?";
        List<LinhKien> list = select(sql, MaKhachHanh);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<LinhKien> select(String sql, Object...args){
        List<LinhKien> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    LinhKien model=readFromResultSet(rs);
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
    
    private LinhKien readFromResultSet(ResultSet rs) throws SQLException{
        LinhKien model=new LinhKien();
        model.setMaLinhKien(rs.getString("maLinhKien"));
        model.setTenHang(rs.getString("tenHang"));
        model.setSoLuong(rs.getInt("soLuong"));
        model.setGiaBan(rs.getFloat("giaBan"));
        model.setMoTa(rs.getString("moTa"));
        return model;
    }
}
