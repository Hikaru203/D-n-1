/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.phones.model;

/**
 *
 * @author kioys
 */
public class ThongTinMay {

    String maMay;
    String tenMay;
    String maHang;
    String tenHang;
    int ram;
    int rom;
    String chip;

    public ThongTinMay() {
    }

    public ThongTinMay(String maMay, String tenMay, String maHang, String tenHang, int ram, int rom, String chip) {
        this.maMay = maMay;
        this.tenMay = tenMay;
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.ram = ram;
        this.rom = rom;
        this.chip = chip;
    }

    

    public String getMaMay() {
        return maMay;
    }

    public void setMaMay(String maMay) {
        this.maMay = maMay;
    }

    public String getTenMay() {
        return tenMay;
    }

    public void setTenMay(String tenMay) {
        this.tenMay = tenMay;
    }

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getRom() {
        return rom;
    }

    public void setRom(int rom) {
        this.rom = rom;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public String getMaHang() {
        return maHang;
    }

    public void setMaHang(String maHang) {
        this.maHang = maHang;
    }
    
    

    @Override
    public String toString() {
        return tenMay;
    }
}
