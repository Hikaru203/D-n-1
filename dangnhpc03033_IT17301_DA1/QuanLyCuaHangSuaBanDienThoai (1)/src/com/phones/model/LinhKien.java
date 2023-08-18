/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

/**
 *
 * @author kioys
 */
public class LinhKien {
    String maLinhKien;
    String tenHang;
    int soLuong;
    float giaBan;
    String moTa;

    public LinhKien(String maLinhKien, String tenHang, int soLuong, float giaBan, String moTa) {
        this.maLinhKien = maLinhKien;
        this.tenHang = tenHang;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.moTa = moTa;
    }

    public LinhKien() {
    }

    public String getMaLinhKien() {
        return maLinhKien;
    }

    public void setMaLinhKien(String maLinhKien) {
        this.maLinhKien = maLinhKien;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(float giaBan) {
        this.giaBan = giaBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenHang;
    }
    
    
    
}
