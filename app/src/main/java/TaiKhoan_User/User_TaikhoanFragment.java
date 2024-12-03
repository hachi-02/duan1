package TaiKhoan_User;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class User_TaikhoanFragment extends Fragment {
    TextView tv_hoten, tv_email;  // tv_email thay vì EditText
    EditText et_hoten, et_sdt, et_diachi;
    Button bt_luu, bt_doimk;
    ImageView img;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taikhoan_user, container, false);
        tv_hoten = v.findViewById(R.id.tv_hoten_u);
        tv_email = v.findViewById(R.id.tv_email_u);  // TextView cho email
        et_hoten = v.findViewById(R.id.et_hoten_u);
        et_diachi = v.findViewById(R.id.et_diachi_u);
        et_sdt = v.findViewById(R.id.et_sdt_u);
        bt_doimk = v.findViewById(R.id.bt_doimk_u);
        bt_luu = v.findViewById(R.id.bt_luu_u);
        img = v.findViewById(R.id.img);

        // Load thông tin người dùng khi trang được tạo
        loadUserInfo();

        // Lưu thông tin khi người dùng nhấn nút lưu
        bt_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = et_hoten.getText().toString();
                String sdt = et_sdt.getText().toString();
                String diachi = et_diachi.getText().toString();

                // Không yêu cầu người dùng điền đầy đủ thông tin, có thể để trống
                // Cập nhật thông tin người dùng vào Firebase ngay cả khi các trường trống
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference userRef = mDatabase.child("users").child(userId);

                // Chỉ cập nhật những trường có giá trị
                if (!hoten.isEmpty()) {
                    userRef.child("name").setValue(hoten);
                }
                if (!sdt.isEmpty()) {
                    userRef.child("phoneNumber").setValue(sdt);
                }
                if (!diachi.isEmpty()) {
                    userRef.child("address").setValue(diachi);
                }

                userRef.updateChildren(new HashMap<String, Object>() {{
                    put("name", hoten);
                    put("phoneNumber", sdt);
                    put("address", diachi);
                }}).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tv_hoten.setText(hoten);  // Cập nhật lại tên trên UI
                        et_hoten.setText(hoten);
                        et_sdt.setText(sdt);
                        et_diachi.setText(diachi);
                        Toast.makeText(getActivity(), "Thông tin đã được lưu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        bt_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diagDoiMK();
            }
        });

        return v;
    }

    //lấy thông tin người dùng từ firebase
    private void loadUserInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        // Lắng nghe dữ liệu thời gian thực
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String hoten = dataSnapshot.child("name").getValue(String.class);
                    String sdt = dataSnapshot.child("phoneNumber").getValue(String.class);
                    String diachi = dataSnapshot.child("address").getValue(String.class);

                    // Hiển thị thông tin vào các EditText
                    if (hoten != null && !hoten.isEmpty()) {
                        tv_hoten.setText(hoten);  // Hiển thị tên người dùng nếu có
                        et_hoten.setText(hoten);
                    } else {
                        tv_hoten.setText("User");  // Hiển thị "User" nếu không có tên
                    }
                    et_sdt.setText(sdt);
                    et_diachi.setText(diachi);
                } else {
                    // Nếu không có thông tin người dùng trong Firebase
                    tv_hoten.setText("User");  // Mặc định là "User"
                    Toast.makeText(getActivity(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Lỗi khi tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });

        // Lấy email từ FirebaseAuth
        String email = mAuth.getCurrentUser().getEmail();
        if (email != null) {
            tv_email.setText(email); // Hiển thị email trong TextView
        }
    }


    private void diagDoiMK() {
        // Tạo hộp thoại để người dùng nhập mật khẩu mới
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Layout nhập mật khẩu mới
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.doimk, null);
        EditText etNewPassword = view.findViewById(R.id.et_new_password);
        EditText etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        Button bt_doimk=view.findViewById(R.id.bt_doimk);
        bt_doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                // Kiểm tra mật khẩu trống hoặc không khớp
                if (newPassword.isEmpty() || newPassword.length() < 6) {
                    Toast.makeText(getActivity(), "Mật khẩu phải ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                //ss mật khẩu mới và xác nhận mk
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                doiMK(newPassword);

            }
        });
        builder.setView(view);

        builder.setNegativeButton("Hủy",(dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void doiMK(String newPassword) {
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            // Thông báo lỗi nếu có
                            Toast.makeText(getActivity(), "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Bạn cần đăng nhập lại để đổi mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }




}
