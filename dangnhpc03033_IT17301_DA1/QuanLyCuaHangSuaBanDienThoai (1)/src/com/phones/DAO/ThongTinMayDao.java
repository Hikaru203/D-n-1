/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.ThongTinMay;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class ThongTinMayDao {

    public List<ThongTinMay> select() {
        String sql = "SELECT MaMay,TenMay,May.MaHang AS'MaHang',TenHang,RAM,ROM,Chip FROM dbo.May INNER JOIN dbo.Hang ON Hang.MaHang = May.MaHang";
        return select(sql);
    }


    public List<ThongTinMay> selectByCourse(String tenMay){
        String sql="SELECT MaMay,TenMay,May.MaHang AS'MaHang',TenHang,RAM,ROM,Chip FROM dbo.May INNER JOIN dbo.Hang ON Hang.MaHang = May.MaHang WHERE TenHang=?";
        return select(sql, tenMay);
    }
    public List<ThongTinMay> selectByKeyword(String keyword){
        String sql="SELECT MaMay,TenMay,May.MaHang AS'MaHang',TenHang,RAM,ROM,Chip FROM dbo.May INNER JOIN dbo.Hang ON Hang.MaHang = May.MaHang WHERE TenHang LIKE ?";
        return select(sql, "%"+keyword+"%");
    }

    public ThongTinMay findById(String maMay) {
        String sql = "SELECT MaMay,TenMay,May.MaHang AS'MaHang',TenHang,RAM,ROM,Chip FROM dbo.May INNER JOIN dbo.Hang ON Hang.MaHang = May.MaHang where MaMay=?";
        List<ThongTinMay> list = select(sql, maMay);
        return list.size() > 0 ? list.get(0) : null;
    }

    public ThongTinMay FindByMay(String tenMay){
        String sql="SELECT MaMay FROM May WHERE TenMay=?";
        List<ThongTinMay> list = select(sql, tenMay);
        return list.size() > 0 ? list.get(0) : null;
    }
    public void insert(ThongTinMay model){
        String sql="INSERT INTO dbo.May (MaMay,TenMay,MaHang,Ram,Rom,Chip)VALUES(?, ?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaMay(), 
                model.getTenMay(),
                model.getMaHang(),
                model.getRam(),
                model.getRom(),
                model.getChip());
    }
    public void update(ThongTinMay model) {
        String sql = "UPDATE May SET TenMay=?,MaHang=?,Ram=?,Rom=?,Chip=? WHERE MaMay=?";
        JdbcHelper.executeUpdate(sql,
                model.getTenMay(),
                model.getMaHang(),
                model.getRam(),
                model.getRom(),
                model.getChip(),
                model.getMaMay());
    }
    public void delete(String MaMay) {
        String sql = "DELETE FROM May WHERE MaMay=?";
        JdbcHelper.executeUpdate(sql, MaMay);
    }
    
    private List<ThongTinMay> select(String sql, Object... args) {
        List<ThongTinMay> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    ThongTinMay model=readFromResultSet(rs);
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

    private ThongTinMay readFromResultSet(ResultSet rs) throws SQLException {
        ThongTinMay model = new ThongTinMay();
        model.setMaMay(rs.getString("maMay"));
        model.setTenMay(rs.getString("TenMay"));
        model.setMaHang(rs.getString("MaHang"));
        model.setTenHang(rs.getString("tenHang"));
        model.setRam(rs.getInt("ram"));
        model.setRom(rs.getInt("rom"));
        model.setChip(rs.getString("Chip"));
        return model;
    }
}
