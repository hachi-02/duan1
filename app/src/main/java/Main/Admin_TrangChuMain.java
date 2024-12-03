package Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.example.duan_1.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import DangKy_Nhap.DangNhap;
import DoanhThu.DoanhThuFragment;
import DonHang.DonHangFragment;
import Qly_DonHang.Admin_QuanLyFragment;
import TaiKhoan_Admin.Admin_TaiKhoanFragment;
import TrangChu_Admin.Admin_TrangChuFragment;

public class Admin_TrangChuMain extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar tbar;
    NavigationView naviga;
    TextView tv_name;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_trang_chu_admin);

        drawer=(DrawerLayout)findViewById(R.id.main);
        tbar=(Toolbar)findViewById(R.id.toolbar);
        naviga=(NavigationView)findViewById(R.id.nvView);

        // Lấy header của NavigationView
        View headerView = naviga.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.tv_ten_a);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String userId = mAuth.getCurrentUser().getUid(); // Lấy ID người dùng hiện tại
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        // Lấy dữ liệu từ Firebase
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    if (name != null) {
                        tv_name.setText(name); // Tự động cập nhật NavigationView
                    }
                } else {
                    tv_name.setText("Admin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_TrangChuMain.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });



        setSupportActionBar(tbar);

        tbar.setNavigationIcon(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Admin_TrangChuFragment f= new Admin_TrangChuFragment();
        FragmentManager fagment=getSupportFragmentManager();
        fagment.beginTransaction()
                .replace(R.id.flContent,f)
                .commit();

        naviga.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                Fragment f;
                if(id==R.id.mn_QlySP)
                {
                    f=new Admin_TrangChuFragment();
                    Toast.makeText(Admin_TrangChuMain.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                }
                else{
                    f=new Admin_TrangChuFragment();
                }

                if(id==R.id.mn_QlyDH)
                {
                    f=new Admin_QuanLyFragment();
                }
                if(id==R.id.mn_DoanhThu)
                {
                    f=new DoanhThuFragment();
                }
                if(id==R.id.mn_TaiKhoan)
                {
                    f=new Admin_TaiKhoanFragment();
                }
                if(id==R.id.mn_dandgxuat)
                {
                    Intent i= new Intent(Admin_TrangChuMain.this,DangNhap.class);
                    startActivity(i);
                    Toast.makeText(Admin_TrangChuMain.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fagment=getSupportFragmentManager();
                fagment.beginTransaction()
                        .replace(R.id.flContent,f)
                        .commit();

                setTitle(item.getTitle());
                drawer.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}