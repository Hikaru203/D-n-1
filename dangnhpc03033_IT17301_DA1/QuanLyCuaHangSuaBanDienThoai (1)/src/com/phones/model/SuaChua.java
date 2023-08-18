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
public class SuaChua {

    String MaSuaChua;
    String tenMay;
    String moTaLoi;
    String tinhTrangMay;
    String tenHang;
    String tenLinhKien;
    String matkhauMay;
    Date ngayNhanMay;
    String nhanVienNhanMay;
    String kyThuatVien;
    float phiSuaChua;
    float t;
    String moTa;
    Date ngayTraMayDuKien;
    String trangThai;
    Date ngayTraMayThucTe;
    String tenKhachHang;
    String maMay;
    String MaNhanVienNhan;
    String MaNhanVienSua;
    String MaLinhKien;
    String MaKhachHang;

    public SuaChua() {
    }

    public SuaChua(String MaSuaChua, String tenMay, String moTaLoi, String tinhTrangMay, String tenHang, String tenLinhKien, String matkhauMay, Date ngayNhanMay, String nhanVienNhanMay, String kyThuatVien, float phiSuaChua, float t, String moTa, Date ngayTraMayDuKien, String trangThai, Date ngayTraMayThucTe, String tenKhachHang, String maMay, String MaNhanVienNhan, String MaNhanVienSua, String MaLinhKien, String MaKhachHang) {
        this.MaSuaChua = MaSuaChua;
        this.tenMay = tenMay;
        this.moTaLoi = moTaLoi;
        this.tinhTrangMay = tinhTrangMay;
        this.tenHang = tenHang;
        this.tenLinhKien = tenLinhKien;
        this.matkhauMay = matkhauMay;
        this.ngayNhanMay = ngayNhanMay;
        this.nhanVienNhanMay = nhanVienNhanMay;
        this.kyThuatVien = kyThuatVien;
        this.phiSuaChua = phiSuaChua;
        this.t = t;
        this.moTa = moTa;
        this.ngayTraMayDuKien = ngayTraMayDuKien;
        this.trangThai = trangThai;
        this.ngayTraMayThucTe = ngayTraMayThucTe;
        this.tenKhachHang = tenKhachHang;
        this.maMay = maMay;
        this.MaNhanVienNhan = MaNhanVienNhan;
        this.MaNhanVienSua = MaNhanVienSua;
        this.MaLinhKien = MaLinhKien;
        this.MaKhachHang = MaKhachHang;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public String getMaSuaChua() {
        return MaSuaChua;
    }

    public void setMaSuaChua(String MaSuaChua) {
        this.MaSuaChua = MaSuaChua;
    }

    public String getTenMay() {
        return tenMay;
    }

    public void setTenMay(String tenMay) {
        this.tenMay = tenMay;
    }

    public String getMoTaLoi() {
        return moTaLoi;
    }

    public void setMoTaLoi(String moTaLoi) {
        this.moTaLoi = moTaLoi;
    }

    public String getTinhTrangMay() {
        return tinhTrangMay;
    }

    public void setTinhTrangMay(String tinhTrangMay) {
        this.tinhTrangMay = tinhTrangMay;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getTenLinhKien() {
        return tenLinhKien;
    }

    public void setTenLinhKien(String tenLinhKien) {
        this.tenLinhKien = tenLinhKien;
    }

    public String getMatkhauMay() {
        return matkhauMay;
    }

    public void setMatkhauMay(String matkhauMay) {
        this.matkhauMay = matkhauMay;
    }

    public Date getNgayNhanMay() {
        return ngayNhanMay;
    }

    public void setNgayNhanMay(Date ngayNhanMay) {
        this.ngayNhanMay = ngayNhanMay;
    }

    public String getNhanVienNhanMay() {
        return nhanVienNhanMay;
    }

    public void setNhanVienNhanMay(String nhanVienNhanMay) {
        this.nhanVienNhanMay = nhanVienNhanMay;
    }

    public String getKyThuatVien() {
        return kyThuatVien;
    }

    public void setKyThuatVien(String kyThuatVien) {
        this.kyThuatVien = kyThuatVien;
    }

    public float getPhiSuaChua() {
        return phiSuaChua;
    }

    public void setPhiSuaChua(float phiSuaChua) {
        this.phiSuaChua = phiSuaChua;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Date getNgayTraMayDuKien() {
        return ngayTraMayDuKien;
    }

    public void setNgayTraMayDuKien(Date ngayTraMayDuKien) {
        this.ngayTraMayDuKien = ngayTraMayDuKien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayTraMayThucTe() {
        return ngayTraMayThucTe;
    }

    public void setNgayTraMayThucTe(Date ngayTraMayThucTe) {
        this.ngayTraMayThucTe = ngayTraMayThucTe;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getMaMay() {
        return maMay;
    }

    public void setMaMay(String maMay) {
        this.maMay = maMay;
    }

    public String getMaNhanVienNhan() {
        return MaNhanVienNhan;
    }

    public void setMaNhanVienNhan(String MaNhanVienNhan) {
        this.MaNhanVienNhan = MaNhanVienNhan;
    }

    public String getMaNhanVienSua() {
        return MaNhanVienSua;
    }

    public void setMaNhanVienSua(String MaNhanVienSua) {
        this.MaNhanVienSua = MaNhanVienSua;
    }

    public String getMaLinhKien() {
        return MaLinhKien;
    }

    public void setMaLinhKien(String MaLinhKien) {
        this.MaLinhKien = MaLinhKien;
    }

    public String getMaKhachHang() {
        return MaKhachHang;
    }

    public void setMaKhachHang(String MaKhachHang) {
        this.MaKhachHang = MaKhachHang;
    }

    @Override
    public String toString() {
        return kyThuatVien;
    }
}
