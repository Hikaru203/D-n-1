/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

import java.sql.Date;

/**
 *
 * @author kioys
 */
public class BaoHanh {
    String maSuaChua;
    int ThoiGianBaoHanh;
    Date NgayBatDau;

    public BaoHanh() {
    }

    public BaoHanh(String maSuaChua, int ThoiGianBaoHanh, Date NgayBatDau) {
        this.maSuaChua = maSuaChua;
        this.ThoiGianBaoHanh = ThoiGianBaoHanh;
        this.NgayBatDau = NgayBatDau;
    }

    public String getMaSuaChua() {
        return maSuaChua;
    }

    public void setMaSuaChua(String maSuaChua) {
        this.maSuaChua = maSuaChua;
    }

    public int getThoiGianBaoHanh() {
        return ThoiGianBaoHanh;
    }

    public void setThoiGianBaoHanh(int ThoiGianBaoHanh) {
        this.ThoiGianBaoHanh = ThoiGianBaoHanh;
    }

    public Date getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(Date NgayBatDau) {
        this.NgayBatDau = NgayBatDau;
    }
    
    
}
