/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.LinhKien;
import com.phones.model.PhieuNhap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 *
 * @author kioys
 */
public class PhieuNhapDao {
    public List<PhieuNhap> select() {
        String sql = "SELECT PhieuNhap.MaPhieuNhap,TenNhaCungCap,NgayNhap,Mota,SoLuong,TenLinhKien,GiaTien,PhieuNhap.MaNhaCungCap AS'MaNhaCungCap',MaLinhKien FROM dbo.PhieuNhap INNER JOIN dbo.NhaCungCap ON NhaCungCap.MaNhaCungCap = PhieuNhap.MaNhaCungCap INNER JOIN dbo.PhieuNhap_LinhKien ON PhieuNhap_LinhKien.MaPhieuNhap = PhieuNhap.MaPhieuNhap";
        return select(sql);
    }
    public List<PhieuNhap> select1(String maPhieuNhap) {
        String sql = "SELECT MaPhieuNhap, MaLinhKien, TenLinhKien, SoLuong, GiaTien FROM dbo.PhieuNhap_LinhKien WHERE MaPhieuNhap = ?";
        return select1(sql,maPhieuNhap);
    }
    
    public PhieuNhap findById(String maphieuNhap) {
        String sql = "SELECT PhieuNhap.MaPhieuNhap,TenNhaCungCap,NgayNhap,Mota,SoLuong,TenLinhKien,GiaTien,PhieuNhap.MaNhaCungCap AS'MaNhaCungCap',MaLinhKien FROM dbo.PhieuNhap INNER JOIN dbo.NhaCungCap ON NhaCungCap.MaNhaCungCap = PhieuNhap.MaNhaCungCap INNER JOIN dbo.PhieuNhap_LinhKien ON PhieuNhap_LinhKien.MaPhieuNhap = PhieuNhap.MaPhieuNhap where PhieuNhap.MaPhieuNhap=?";
        List<PhieuNhap> list = select(sql, maphieuNhap);
        return list.size() > 0 ? list.get(0) : null;
    }
    public List<PhieuNhap> selectByKeyword(){
        String sql="SELECT PhieuNhap.MaPhieuNhap,TenNhaCungCap,NgayNhap,Mota,PhieuNhap.MaNhaCungCap AS'MaNhaCungCap' FROM dbo.PhieuNhap INNER JOIN dbo.NhaCungCap ON NhaCungCap.MaNhaCungCap = PhieuNhap.MaNhaCungCap";
        return select(sql);
    }
    public void insert(PhieuNhap model){
        String sql="INSERT INTO dbo.PhieuNhap(MaPhieuNhap,MaNhaCungCap,NgayNhap,Mota)VALUES(?,?,?,?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaPhieuNhap(), 
                model.getMaNhaCungCap(),
                model.getNgayNhap(),
                model.getMoTa());
    }
    public void insert2(PhieuNhap model){
        String sql="INSERT INTO dbo.PhieuNhap_LinhKien(MaPhieuNhap,MaLinhKien,TenLinhKien,SoLuong,GiaTien)VALUES(?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql, 
                model.getMaPhieuNhap(), 
                model.getMaLinhKien(),
                model.getTenLinhKien(),
                model.getSoLuong(),
                model.getGiaThanh());
    }
    public void updateSL(PhieuNhap model) {
        String sql = "UPDATE dbo.LinhKien SET SoLuong=SoLuong+? where MaLinhKien=?";
        JdbcHelper.executeUpdate(sql,
                model.getSoLuong(),
                model.getMaLinhKien());
    }

    
    
    

    private List<PhieuNhap> select(String sql, Object...args){
        List<PhieuNhap> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    PhieuNhap model=readFromResultSet(rs);
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
    private List<PhieuNhap> select1(String sql, Object...args){
        List<PhieuNhap> list=new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    PhieuNhap model=readFromResultSet1(rs);
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
    
    private PhieuNhap readFromResultSet(ResultSet rs) throws SQLException{
        PhieuNhap model=new PhieuNhap();
        model.setMaPhieuNhap(rs.getString("MaPhieuNhap"));
        model.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
        model.setNgayNhap(rs.getDate("ngayNhap"));
        model.setMoTa(rs.getString("moTa"));
        model.setMaNhaCungCap(rs.getString("MaNhaCungCap"));
        return model;
    }
    private PhieuNhap readFromResultSet1(ResultSet rs) throws SQLException{
        PhieuNhap model=new PhieuNhap();
        model.setMaPhieuNhap(rs.getString("maPhieuNhap"));
        model.setSoLuong(rs.getInt("soLuong"));
        model.setTenLinhKien(rs.getString("tenLinhKien"));
        model.setGiaThanh(rs.getFloat("giaTien"));
        model.setMaLinhKien(rs.getString("MaLinhKien"));
        return model;
    }
}
