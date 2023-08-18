/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.DateHelper;
import com.phones.helper.JdbcHelper;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class BaoCaoDao {

    public List<Object[]> getDoanhThu() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "{call SP_DoanhThu}";
                rs = JdbcHelper.executeQuery(sql);
                int i=1;
                while (rs.next()) {
                    Object[] model = {
                        i++,
                        rs.getString("Ngay"),
                        rs.getString("Noi dung"),
                        rs.getBigDecimal("Tongtien",0),
                        rs.getString("Trạng thái")
                    };
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

    public List<Object[]> getLuong() {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            int i=1;
            try {
                String sql = "{call SP_LuongNV}";
                rs = JdbcHelper.executeQuery(sql);
                while (rs.next()) {
                    Object[] model = {
                        i++,
                        rs.getString("HoTen"),
                        rs.getBigDecimal("Luong",0),
                        rs.getString("ChucVu")
                    };
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
}
