/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.HangMay;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class HangMayDao {
    public List<HangMay> select() {
        String sql = "select * from Hang";
        return select(sql);
    }
    public void insert(HangMay model){
        String sql="INSERT INTO Hang(MaHang, TenHang) VALUES(?, ?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaHang(), 
                model.getTenHang());
    }
    public HangMay findById(String MaKhachHanh) {
        String sql = "select * from Hang where MaHang=?";
        List<HangMay> list = select(sql, MaKhachHanh);
        return list.size() > 0 ? list.get(0) : null;
    }
    public List<HangMay> selectByKeyword(String keyword){
        String sql="select * from Hang where TenHang LIKE ?";
        return select(sql, "%"+keyword+"%");
    }
    public void update(HangMay model) {
        String sql = "UPDATE Hang SET TenHang=? WHERE MaHang=?";
        JdbcHelper.executeUpdate(sql,
                model.getTenHang(),
                model.getMaHang());
    }
    
    public void delete(String maHang) {
        String sql = "DELETE FROM Hang WHERE MaHang=?";
        JdbcHelper.executeUpdate(sql, maHang);
    }
    
    private List<HangMay> select(String sql, Object...args){
        List<HangMay> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    HangMay model=readFromResultSet(rs);
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
    
    private HangMay readFromResultSet(ResultSet rs) throws SQLException{
        HangMay model=new HangMay();
        model.setMaHang(rs.getString("maHang"));
        model.setTenHang(rs.getString("tenHang"));
        return model;
    }
}
