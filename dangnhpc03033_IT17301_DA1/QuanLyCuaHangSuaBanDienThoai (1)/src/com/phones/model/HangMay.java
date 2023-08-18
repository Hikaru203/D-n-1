/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

/**
 *
 * @author kioys
 */
public class HangMay {

    String maHang;
    String tenHang;

    public HangMay() {
    }

    public HangMay(String maHang, String tenHang) {
        this.maHang = maHang;
        this.tenHang = tenHang;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    @Override
    public String toString() {
        return tenHang;
    }

}
