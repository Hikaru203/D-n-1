/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.DAO;

import com.phones.helper.JdbcHelper;
import com.phones.model.BaoHanh;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kioys
 */
public class BaoHanhDao {

        public List<BaoHanh> select() {
            String sql = "SELECT * FROM dbo.BaoHanh";
            return select(sql);
        }
        public List<BaoHanh> selectByKeyword(String keyword){
        String sql="SELECT * FROM dbo.BaoHanh where MaSuaChua LIKE ?";
        return select(sql, "%"+keyword+"%");
    }

        private List<BaoHanh> select(String sql, Object... args) {
            List<BaoHanh> list = new ArrayList<>();
            try {
                ResultSet rs = null;
                try {
                    rs = JdbcHelper.executeQuery(sql, args);
                    while (rs.next()) {
                        BaoHanh model = readFromResultSet(rs);
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

        private BaoHanh readFromResultSet(ResultSet rs) throws SQLException {
            BaoHanh model = new BaoHanh();
            model.setMaSuaChua(rs.getString("MaSuaChua"));
            model.setThoiGianBaoHanh(rs.getInt("thoiGianBaoHanh"));
            model.setNgayBatDau(rs.getDate("NgayBatDau"));
            return model;
        }
    }

