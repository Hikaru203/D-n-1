/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

/**
 *
 * @author kioys
 */
public class KhachHang {

    String MaKhachHanh;
    String TenKhachHang;
    String DiaChi;
    String Email;
    String SoDienThoai;

    public KhachHang(String MaKhachHanh, String TenKhachHang, String DiaChi, String Email, String SoDienThoai) {
        this.MaKhachHanh = MaKhachHanh;
        this.TenKhachHang = TenKhachHang;
        this.DiaChi = DiaChi;
        this.Email = Email;
        this.SoDienThoai = SoDienThoai;
    }

    public KhachHang() {
    }

    public String getMaKhachHanh() {
        return MaKhachHanh;
    }

    public void setMaKhachHanh(String MaKhachHanh) {
        this.MaKhachHanh = MaKhachHanh;
    }

    public String getTenKhachHang() {
        return TenKhachHang;
    }

    public void setTenKhachHang(String TenKhachHang) {
        this.TenKhachHang = TenKhachHang;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String SoDienThoai) {
        this.SoDienThoai = SoDienThoai;
    }

    @Override
    public String toString() {
        return TenKhachHang;
    }
    
    
    
    

}
