/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

import java.util.Date;

/**
 *
 * @author kioys
 */
public class HoaDon {
    String maSuaChua;
    String maHoaDon;
    String nhanVienKyThuat;
    String tenKhachHang;
    double thanhTien;
    String nhanVienTiepNhan;
    Date thoiGianLap;
    String moTa;
    int thoiGianBaoHanh;
    Date ngayBatDau;
    double TongTien;
    
    public HoaDon() {
    }

    public HoaDon(String maSuaChua, String maHoaDon, String nhanVienKyThuat, String tenKhachHang, double thanhTien, String nhanVienTiepNhan, Date thoiGianLap, String moTa, int thoiGianBaoHanh, Date ngayBatDau) {
        this.maSuaChua = maSuaChua;
        this.maHoaDon = maHoaDon;
        this.nhanVienKyThuat = nhanVienKyThuat;
        this.tenKhachHang = tenKhachHang;
        this.thanhTien = thanhTien;
        this.nhanVienTiepNhan = nhanVienTiepNhan;
        this.thoiGianLap = thoiGianLap;
        this.moTa = moTa;
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.ngayBatDau = ngayBatDau;
    }
    
    
    public HoaDon(String maSuaChua, String maHoaDon, String nhanVienKyThuat, String tenKhachHang, double thanhTien, String nhanVienTiepNhan, Date thoiGianLap, String moTa, int thoiGianBaoHanh, Date ngayBatDau, double TongTien) {
        this.maSuaChua = maSuaChua;
        this.maHoaDon = maHoaDon;
        this.nhanVienKyThuat = nhanVienKyThuat;
        this.tenKhachHang = tenKhachHang;
        this.thanhTien = thanhTien;
        this.nhanVienTiepNhan = nhanVienTiepNhan;
        this.thoiGianLap = thoiGianLap;
        this.moTa = moTa;
        this.thoiGianBaoHanh = thoiGianBaoHanh;
        this.ngayBatDau = ngayBatDau;
        this.TongTien = TongTien;
    }
    
    

    public String getMaSuaChua() {
        return maSuaChua;
    }

    public void setMaSuaChua(String maSuaChua) {
        this.maSuaChua = maSuaChua;
    }


    public String getNhanVienKyThuat() {
        return nhanVienKyThuat;
    }

    public void setNhanVienKyThuat(String nhanVienKyThuat) {
        this.nhanVienKyThuat = nhanVienKyThuat;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getNhanVienTiepNhan() {
        return nhanVienTiepNhan;
    }

    public void setNhanVienTiepNhan(String nhanVienTiepNhan) {
        this.nhanVienTiepNhan = nhanVienTiepNhan;
    }

    public Date getThoiGianLap() {
        return thoiGianLap;
    }

    public void setThoiGianLap(Date thoiGianLap) {
        this.thoiGianLap = thoiGianLap;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }

    public void setThoiGianBaoHanh(int thoiGianBaoHanh) {
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public double getTongTien() {
        return TongTien;
    }

    public void setTongTien(double TongTien) {
        this.TongTien = TongTien;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    
    

    @Override
    public String toString() {
        return TongTien+"";
    }
}
