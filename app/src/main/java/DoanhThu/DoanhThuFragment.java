package DoanhThu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.duan_1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.HoaDon;

public class DoanhThuFragment extends Fragment {
    Button bt_tongtien;
    TextView tvTongTien;
    FirebaseDatabase database;
    ArrayList<HoaDon> hoaDonList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doanhthu, container, false);

        bt_tongtien = v.findViewById(R.id.bt_tongtien);
        tvTongTien = v.findViewById(R.id.tv_tongtien);

        // Nút tính tổng tiền
        bt_tongtien.setOnClickListener(v1 -> {
            getAllHoaDonIDs(); // Lấy hóa đơn của tất cả người dùng
        });

        return v;
    }

    private void getAllHoaDonIDs() {
        // Tham chiếu đến "UserID" trong Firebase để lấy danh sách hóa đơn
        DatabaseReference userRef = database.getReference("HoaDon");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hoaDonList.clear(); // Đảm bảo danh sách được làm trống trước khi thêm mới

                // Duyệt qua tất cả các userID trong Firebase (có thể là một cấp cha chứa danh sách các hóa đơn)
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy ID người dùng
                    String userID = snapshot.getKey(); // Đây là userID của người dùng

                    // Bây giờ truy vấn lấy các hóa đơn của người dùng này
                    getHoaDonByUserID(userID);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DoanhThuFragment", "Lỗi khi lấy dữ liệu: " + databaseError.getMessage());
            }
        });
    }

    private void getHoaDonByUserID(String userID) {
        // Tham chiếu đến "HoaDon" của user
        DatabaseReference hoaDonRef = database.getReference("HoaDon").child(userID);

        hoaDonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Duyệt qua tất cả các hóa đơn của người dùng
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Chuyển dữ liệu fb thành 1 đối tượng
                    HoaDon hoaDon = snapshot.getValue(HoaDon.class);
                    // Kiểm tra nếu hóa đơn đã nhận hàng
                    if (hoaDon != null && hoaDon.isDaNhanHang()) {
                        hoaDonList.add(hoaDon); // Thêm vào danh sách các hóa đơn đã nhận hàng
                    }
                }
                tinhTong();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void tinhTong() {
        double finalTien = 0;

        // Duyệt qua các hóa đơn đã nhận hàng để tính tổng tiền
        for (HoaDon hoaDon : hoaDonList) {
            try {
                double tongTien = Double.parseDouble(hoaDon.getTongTien());
                finalTien += tongTien;
            } catch (NumberFormatException e) {

            }
        }

        // Hiển thị tổng doanh thu
        tvTongTien.setText(""+ finalTien);
    }
}
