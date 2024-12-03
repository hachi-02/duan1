package DonHang;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;

public class DonHang_Viewholder extends RecyclerView.ViewHolder{
    TextView tv_tensp,tv_size,tv_soluong,tv_gia,tv_dxn,tv_chitiet;
    LinearLayout bt_huy,bt_xacnhan;
    public DonHang_Viewholder(@NonNull View v) {
        super(v);
        tv_tensp=v.findViewById(R.id.tv_tensp);
        tv_size=v.findViewById(R.id.tv_size);
        tv_soluong=v.findViewById(R.id.tv_soluong);
        tv_gia=v.findViewById(R.id.tv_giasp);
        bt_huy=v.findViewById(R.id.bt_huy);
        tv_chitiet=v.findViewById(R.id.tv_chitiet);
        tv_dxn=v.findViewById(R.id.tv_dxn);
        bt_xacnhan=v.findViewById(R.id.bt_xacnhan);
    }
}
