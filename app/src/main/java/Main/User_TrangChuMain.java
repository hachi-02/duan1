package Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import DonHang.DonHangFragment;
import GioHang.GioHangFragment;
import TaiKhoan_User.User_TaikhoanFragment;
import TrangChu_User.User_TrangChuFragment;
import vn.zalopay.sdk.ZaloPaySDK;


public class User_TrangChuMain extends AppCompatActivity {
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
        setContentView(R.layout.main_trang_chu_user);

        drawer=(DrawerLayout)findViewById(R.id.main);
        tbar=(Toolbar)findViewById(R.id.toolbar);
        naviga=(NavigationView)findViewById(R.id.nvView);

        // Lấy header của NavigationView
        View headerView = naviga.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.tv_ten);

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
                    tv_name.setText("Người dùng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(User_TrangChuMain.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });






        setSupportActionBar(tbar);

        tbar.setNavigationIcon(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User_TrangChuFragment f= new User_TrangChuFragment();
        FragmentManager fagment=getSupportFragmentManager();
        fagment.beginTransaction()
                .replace(R.id.flContent_user,f)
                .commit();

        naviga.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();
                Fragment f;
                if(id==R.id.mn_TrangChu)
                {
                    f=new User_TrangChuFragment();
                    Toast.makeText(User_TrangChuMain.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                }
                else{
                    f=new User_TrangChuFragment();
                }

                if(id==R.id.mn_Giohang)
                {
                    f=new GioHangFragment();
                    Toast.makeText(User_TrangChuMain.this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
                }
                if(id==R.id.mn_Donhang)
                {
                    f=new DonHangFragment();
                    Toast.makeText(User_TrangChuMain.this, "Đơn hàng", Toast.LENGTH_SHORT).show();
                }
                if(id==R.id.mn_TaiKhoan)
                {
                    f=new User_TaikhoanFragment();
                }
                if(id==R.id.mn_dandgxuat)
                {
                    Intent i= new Intent(User_TrangChuMain.this, DangNhap.class);
                    startActivity(i);
                    Toast.makeText(User_TrangChuMain.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                }

                FragmentManager fagment=getSupportFragmentManager();
                fagment.beginTransaction()
                        .replace(R.id.flContent_user,f)
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
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
