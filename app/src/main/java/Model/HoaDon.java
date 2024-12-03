package Model;

import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String userID;       // ID hóa đơn
    private String hoadonID;   // ID người mua
    private String tensp;
    private String diaChi;
    private String soDT;
    private String tongTien;
    private String size;
    private int soLuong;
    private boolean daNhanHang;
    private boolean daXacNhan; // Trạng thái xác nhận đơn hàng

    // Constructor không tham số (yêu cầu của Firebase)
    public HoaDon() {}

    // Constructor đầy đủ
    public HoaDon(String userID, String hoadonID, String tensp, String diaChi, String soDT, String tongTien, String size, int soLuong,boolean daNhanHang,boolean daXacNhan) {
        this.userID = userID;
        this.hoadonID = hoadonID;
        this.tensp = tensp;
        this.diaChi = diaChi;
        this.soDT = soDT;
        this.tongTien = tongTien;
        this.size = size;
        this.soLuong = soLuong;
        this.daNhanHang = daNhanHang;
        this.daXacNhan=daXacNhan;
    }


    // Getter và Setter
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getHoadonID() { return hoadonID; }
    public void setHoadonID(String hoadonID) { this.hoadonID = hoadonID; }
    public String getTensp() { return tensp; }
    public void setTensp(String tensp) { this.tensp = tensp; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }
    public String getTongTien() { return tongTien; }
    public void setTongTien(String tongTien) { this.tongTien = tongTien; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public boolean isDaNhanHang() {
        return daNhanHang;
    }

    public void setDaNhanHang(boolean daNhanHang) {
        this.daNhanHang = daNhanHang;
    }
    public boolean isXacNhan() {
        return daXacNhan;
    }

    public void setXacNhan(boolean confirmed) {
        daXacNhan = confirmed;
    }
}


