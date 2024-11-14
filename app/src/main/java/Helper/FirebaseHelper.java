package Helper;

import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import Model.SanPham;

public class FirebaseHelper {

    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();  // Truy cập đến Firebase Realtime Database
    }

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
    public void getAllSanPham(DataCallback callback) {
        mDatabase.child("sanpham").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SanPham> sanPhams = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SanPham sp = snapshot.getValue(SanPham.class);
                    sanPhams.add(sp); // Thêm sản phẩm vào danh sách
                }
                callback.onDataReceived(sanPhams); // Gọi callback với dữ liệu
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure();  // Nếu có lỗi trong quá trình lấy dữ liệu
            }
        });
    }

    // Interface callback khi lấy dữ liệu thành công hay thất bại
    public interface DataCallback {
        void onDataReceived(ArrayList<SanPham> sanPhams);
        void onFailure();
    }

    // Interface khi thêm sản phẩm thành công hay thất bại
    public interface OnCompleteListener {
        void onSuccess();
        void onFailure();
    }
}
