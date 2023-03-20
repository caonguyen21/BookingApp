package com.example.adminbookingapp.Model;

import java.io.Serializable;

public class Booked implements Serializable {
    String hinh;
    String tenks;
    String diachi;
    String gia;
    String hinh2;
    String hinh3;
    String hinh4;
    String DiachiCT;
    String mota;
    String ngayden;
    String ngaydi;
    String tongtien;
    String slphongdon;
    String Sdtks;
    String tenkhachhang;
    String sdtkh;
    String tenkh;
    Boolean status;

    public Booked(String hinh, String tenks, String diachi, String gia, String hinh2, String hinh3, String hinh4, String DiachiCT, String mota, String ngayden, String ngaydi, String tongtien, String slphongdon, String Sdtks, String tenkhachhang, String sdtkh, String tenkh, Boolean status) {
        this.hinh = hinh;
        this.tenks = tenks;
        this.diachi = diachi;
        this.gia = gia;
        this.hinh2 = hinh2;
        this.hinh3 = hinh3;
        this.hinh4 = hinh4;
        this.DiachiCT = DiachiCT;
        this.mota = mota;
        this.ngayden = ngayden;
        this.ngaydi = ngaydi;
        this.tongtien = tongtien;
        this.slphongdon = slphongdon;
        this.Sdtks = Sdtks;
        this.tenkhachhang = tenkhachhang;
        this.sdtkh = sdtkh;
        this.tenkh = tenkh;
        this.status = status;
    }

    public Booked() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getSdtkh() {
        return sdtkh;
    }

    public void setSdtkh(String sdtkh) {
        this.sdtkh = sdtkh;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }

    public String getSdtks() {
        return Sdtks;
    }

    public void setSdtks(String sdtks) {
        Sdtks = sdtks;
    }

    public String getSlphongdon() {
        return slphongdon;
    }

    public void setSlphongdon(String slphongdon) {
        this.slphongdon = slphongdon;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

    public String getNgayden() {
        return ngayden;
    }

    public void setNgayden(String ngayden) {
        this.ngayden = ngayden;
    }

    public String getNgaydi() {
        return ngaydi;
    }

    public void setNgaydi(String ngaydi) {
        this.ngaydi = ngaydi;
    }

    public String getHinh2() {
        return hinh2;
    }

    public void setHinh2(String hinh2) {
        this.hinh2 = hinh2;
    }

    public String getHinh3() {
        return hinh3;
    }

    public void setHinh3(String hinh3) {
        this.hinh3 = hinh3;
    }

    public String getHinh4() {
        return hinh4;
    }

    public void setHinh4(String hinh4) {
        this.hinh4 = hinh4;
    }

    public String getDiachiCT() {
        return DiachiCT;
    }

    public void setDiachiCT(String diachiCT) {
        this.DiachiCT = diachiCT;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getTenks() {
        return tenks;
    }

    public void setTenks(String tenks) {
        this.tenks = tenks;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }
}
