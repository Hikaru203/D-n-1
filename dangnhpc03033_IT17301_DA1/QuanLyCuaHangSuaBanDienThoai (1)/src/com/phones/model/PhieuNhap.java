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
public class PhieuNhap {
    String maPhieuNhap;
    String tenNhaCungCap;
    Date ngayNhap;
    String moTa;
    int soLuong;
    String tenLinhKien;
    float giaThanh;
    String maNhaCungCap;
    String maLinhKien;

    public PhieuNhap() {
    }

    public PhieuNhap(String maPhieuNhap, String tenNhaCungCap, Date ngayNhap, String moTa, int soLuong, String tenLinhKien, String maNhaCungCap, String maLinhKien) {
        this.maPhieuNhap = maPhieuNhap;
        this.tenNhaCungCap = tenNhaCungCap;
        this.ngayNhap = ngayNhap;
        this.moTa = moTa;
        this.soLuong = soLuong;
        this.tenLinhKien = tenLinhKien;
        this.giaThanh = giaThanh;
        this.maNhaCungCap = maNhaCungCap;
        this.maLinhKien = maLinhKien;
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public Date getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Date ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTenLinhKien() {
        return tenLinhKien;
    }

    public void setTenLinhKien(String tenLinhKien) {
        this.tenLinhKien = tenLinhKien;
    }

    public float getGiaThanh() {
        return giaThanh;
    }

    public void setGiaThanh(float giaThanh) {
        this.giaThanh = giaThanh;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getMaLinhKien() {
        return maLinhKien;
    }

    public void setMaLinhKien(String maLinhKien) {
        this.maLinhKien = maLinhKien;
    }
}
