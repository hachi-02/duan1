package DangKy_Nhap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan_1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import TrangChu_Admin.Admin_TrangChuMain;
import TrangChu_User.User_TrangChuMain;

public class DangNhap extends AppCompatActivity {
    Button bt_DangKyDN,bt_DangNhap;
    EditText et_UserNameDN, et_PassDN;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        et_UserNameDN=findViewById(R.id.et_UserNameDN);
        et_PassDN=findViewById(R.id.et_PassDN);
        bt_DangNhap=findViewById(R.id.bt_DangNhap);
        bt_DangKyDN=findViewById(R.id.bt_DangKyDN);
        mAuth = FirebaseAuth.getInstance();

        bt_DangKyDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DangNhap.this, DangKy.class);
                startActivity(i);
            }
        });

        bt_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=et_UserNameDN.getText().toString();
                String pass=et_PassDN.getText().toString();
                if(username.isEmpty()||pass.isEmpty())
                {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(username,pass);

            }
        });

    }
    private void login(String username, String pass) {
        mAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(DangNhap.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String adminUser = "admin@gmail.com";
                String adminPass = "123456789";

                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        if (user.getEmail().equals(adminUser) && pass.equals(adminPass)) {
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công với quyền quản trị", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(DangNhap.this, Admin_TrangChuMain.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(DangNhap.this, "Đăng nhập thành công ", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(DangNhap.this, User_TrangChuMain.class);
                            startActivity(i);

                        }
                        finish();
                    }
                } else {
                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}