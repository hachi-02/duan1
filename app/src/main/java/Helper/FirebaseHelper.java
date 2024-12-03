package Helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import Model.SanPham;
import Model.HoaDon;

public class FirebaseHelper {

    private DatabaseReference mDatabase;

    // Khởi tạo Firebase Database
    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // ===================== SẢN PHẨM =====================

    // Hàm lưu sản phẩm
    public void themSanPham(SanPham sanPham, OnCompleteListener onCompleteListener) {
        mDatabase.child("sanpham").child(sanPham.getIdsp()).setValue(sanPham)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteListener.onSuccess();
                    } else {
                        onCompleteListener.onFailure();
                    }
                });
    }

    // Lấy tất cả sản phẩm
    public void getAllSanPham(SanPhamCallback callback) {
        mDatabase.child("sanpham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SanPham> sanPhams = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sp = snapshot.getValue(SanPham.class);
                    if (sp != null) {
                        sanPhams.add(sp);
                    }
                }
                callback.onDataReceived(sanPhams);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure();
            }
        });
    }

    // ===================== HÓA ĐƠN =====================

    // Hàm lưu hóa đơn
    public void themHoaDon(HoaDon hoaDon, String userID, OnCompleteListener onCompleteListener) {
        // Lưu hóa đơn dưới người dùng tương ứng
        mDatabase.child("HoaDon").child(userID).child(hoaDon.getHoadonID()).setValue(hoaDon)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onCompleteListener.onSuccess();
                    } else {
                        onCompleteListener.onFailure();
                    }
                });
    }

    // Lấy tất cả hóa đơn của người dùng hiện tại
    public void getAllhoaDon(String userID, HoaDonCallback callback) {
        mDatabase.child("HoaDon").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HoaDon> hoaDons = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HoaDon hd = snapshot.getValue(HoaDon.class);
                    if (hd != null) {
                        hoaDons.add(hd);
                    }
                }
                callback.onDataReceived(hoaDons);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure();
            }
        });
    }


    // Hàm lấy tất cả hóa đơn từ tất cả người dùng
    public void getAllHoaDonToAdmin(HoaDonCallback callback) {
        mDatabase.child("HoaDon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HoaDon> hoaDons = new ArrayList<>();
                // Lặp qua các hóa đơn
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Duyệt qua các người dùng
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        HoaDon hd = userSnapshot.getValue(HoaDon.class);
                        if (hd != null) {
                            hoaDons.add(hd);
                        }
                    }
                }
                // Trả dữ liệu đã thu thập về callback
                callback.onDataReceived(hoaDons);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure();
            }
        });
    }





    // ===================== INTERFACE CALLBACK =====================

    // Callback cho sản phẩm
    public interface SanPhamCallback {
        void onDataReceived(ArrayList<SanPham> sanPhams);
        void onFailure();
    }

    // Callback cho hóa đơn
    public interface HoaDonCallback {
        void onDataReceived(ArrayList<HoaDon> hoaDons);
        void onFailure();
    }

    // Interface chung cho hoàn tất (thành công hoặc thất bại)
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure();
    }
}
