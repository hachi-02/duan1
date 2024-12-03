package Qly_DonHang;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;


public class Admin_QuanLy_Viewholder extends RecyclerView.ViewHolder{
    TextView tv_tensp,tv_size,tv_soluong,tv_gia,tv_dxn,tv_chitiet;
    Button bt_huy,bt_xacnhan;
    LinearLayout ln_huyvcn;
    public Admin_QuanLy_Viewholder(@NonNull View v) {
        super(v);
        tv_tensp=v.findViewById(R.id.tv_tensp);
        tv_size=v.findViewById(R.id.tv_size);
        tv_soluong=v.findViewById(R.id.tv_soluong);
        tv_gia=v.findViewById(R.id.tv_giasp);
        bt_huy=v.findViewById(R.id.bt_huy);
        tv_chitiet=v.findViewById(R.id.tv_chitiet);
        bt_xacnhan=v.findViewById(R.id.bt_xacnhan);
        ln_huyvcn=v.findViewById(R.id.ln_hyvcn);
        tv_dxn=v.findViewById(R.id.tv_dxn);
    }
}
