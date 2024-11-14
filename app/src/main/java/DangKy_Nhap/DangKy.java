package DangKy_Nhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class DangKy extends AppCompatActivity {
    EditText et_UserNameDK,et_PassDK,et_ConfirmPass;
    Button bt_DangKy;
    TextView tv_CTaiKhoan;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        et_UserNameDK=findViewById(R.id.et_UserNameDK);
        et_PassDK=findViewById(R.id.et_PassDK);
        et_ConfirmPass=findViewById(R.id.et_ConfirmPassDK);
        tv_CTaiKhoan=findViewById(R.id.tv_CDangNhap);
        bt_DangKy=findViewById(R.id.bt_DangKyDK);
        mAuth = FirebaseAuth.getInstance();


        tv_CTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DangKy.this,DangNhap.class);
                startActivity(i);
            }
        });
        bt_DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=et_UserNameDK.getText().toString();
                String pass=et_PassDK.getText().toString();
                String confirmPass=et_ConfirmPass.getText().toString();
                if(userName.isEmpty()||pass.isEmpty())
                {
                    Toast.makeText(DangKy.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmail(userName))
                {
                    Toast.makeText(DangKy.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(confirmPass))
                {
                    Toast.makeText(DangKy.this, "Mật khẩu nhập lại phải chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()<6)
                {
                    Toast.makeText(DangKy.this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
                }

                mAuth.getInstance().createUserWithEmailAndPassword(userName, pass)
                        .addOnCompleteListener(DangKy.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser(); // Lấy người dùng hiện tại
                                    if (user != null) {
                                        String userID = user.getUid(); // Lấy ID của người dùng
                                        // Tạo một đối tượng HashMap để lưu thông tin người dùng
                                        HashMap<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("email", userName);
                                        userInfo.put("userID", userID); // Lưu ID vào cơ sở dữ liệu
                                        // Lưu thông tin người dùng vào cơ sở dữ liệu
                                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userID);
                                        databaseRef.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(DangKy.this, DangNhap.class);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(DangKy.this, "Lưu thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }
        });
    }

    //check email
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return email != null && email.matches(emailPattern);
    }
}
