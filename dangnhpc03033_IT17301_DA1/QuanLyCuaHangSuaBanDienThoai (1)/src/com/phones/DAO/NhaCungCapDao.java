/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.NhaCungCap;
import com.phones.model.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class NhaCungCapDao {
    public void insert(NhaCungCap model) {
        String sql = "INSERT INTO NhaCungCap (MaNhaCungCap, TenNhaCungCap, DiaChi, Email, SoDienThoai) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaNhaCungCap(),
                model.getTenNhaCungCap(),
                model.getDiaChi(),
                model.getEmail(),
                model.getSoDienThoai());
                
    }
      public void delete(String MaNhaCungCap) {
        String sql = "DELETE FROM NhaCungCap WHERE MaNhaCungCap=?";
        JdbcHelper.executeUpdate(sql, MaNhaCungCap);
    }
      public void update(NhaCungCap model) {
        String sql = "UPDATE NhaCungCap SET TenNhaCungCap=?, DiaChi=?, Email=?, SoDienThoai=? WHERE MaNhaCungCap=?";
        JdbcHelper.executeUpdate(sql,
          
                model.getTenNhaCungCap(),
                model.getDiaChi(),
                model.getEmail(),
                model.getSoDienThoai(),
                model.getMaNhaCungCap());
        
    }
      public List<NhaCungCap> selectByKeyword(String keyword){
        String sql="SELECT * FROM NhaCungCap WHERE TenNhaCungCap LIKE ?";
        return select(sql, "%"+keyword+"%");
    }
    public List<NhaCungCap> select() {
        String sql = "select * from NhaCungCap";
        return select(sql);
    }
    public NhaCungCap findById(String MaNhaCungCap) {
        String sql = "select * from NhaCungCap where MaNhaCungCap=?";
        List<NhaCungCap> list = select(sql, MaNhaCungCap);
        return list.size() > 0 ? list.get(0) : null;
    }
    private List<NhaCungCap> select(String sql, Object...args){
        List<NhaCungCap> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    NhaCungCap model=readFromResultSet(rs);
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
    
    private NhaCungCap readFromResultSet(ResultSet rs) throws SQLException{
        NhaCungCap model=new NhaCungCap();
        model.setMaNhaCungCap(rs.getString("maNhaCungCap"));
        model.setTenNhaCungCap(rs.getString("tenNhaCungCap"));
        model.setDiaChi(rs.getString("DiaChi"));
        model.setEmail(rs.getString("Email"));
        model.setSoDienThoai(rs.getString("SoDienThoai"));
        return model;
    }
}
