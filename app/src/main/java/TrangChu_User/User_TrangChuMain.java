package TrangChu_User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

import DangKy_Nhap.DangNhap;



public class User_TrangChuMain extends AppCompatActivity {
    DrawerLayout drawer;
    Toolbar tbar;
    NavigationView naviga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_trang_chu_user);

        drawer=(DrawerLayout)findViewById(R.id.main);
        tbar=(Toolbar)findViewById(R.id.toolbar);
        naviga=(NavigationView)findViewById(R.id.nvView);



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

                }
                if(id==R.id.mn_Donhang)
                {

                }
                if(id==R.id.mn_TaiKhoan)
                {

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
}
