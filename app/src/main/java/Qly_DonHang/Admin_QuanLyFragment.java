package Qly_DonHang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;

import java.util.ArrayList;

import Qly_DonHang.Admin_QuanLy_Adapter;
import Helper.FirebaseHelper;
import Model.HoaDon;

public class Admin_QuanLyFragment extends Fragment {
    private RecyclerView rcv;
    private FirebaseHelper firebaseHelper;
    private Admin_QuanLy_Adapter adapterQl;
    ArrayList<HoaDon> ds ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_donhang, container, false);
        rcv = v.findViewById(R.id.rcvDh);
        firebaseHelper = new FirebaseHelper();

        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        ds = new ArrayList<>();

        adapterQl = new Admin_QuanLy_Adapter(ds,getContext(),this);
        rcv.setAdapter(adapterQl);
        dulieu();
        return  v;
    }

    public void chitiet(HoaDon hd)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = getLayoutInflater();
        View v = inf.inflate(R.layout.hoadonchitiet, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        TextView tv_tensp,tv_soluong,tv_size,tv_gia,tv_diachi,tv_sdt,tv_trangthai;
        Button bt_qlai;

        tv_tensp=v.findViewById(R.id.tv_tensp);
        tv_soluong=v.findViewById(R.id.tv_soluong);
        tv_size=v.findViewById(R.id.tv_size);
        tv_gia=v.findViewById(R.id.tv_gia);
        tv_diachi=v.findViewById(R.id.tv_diachi);
        tv_sdt=v.findViewById(R.id.tv_sdt);
        tv_trangthai=v.findViewById(R.id.tv_trangthai);

        bt_qlai=v.findViewById(R.id.bt_qlai);

        tv_tensp.setText(hd.getTensp());
        tv_gia.setText(hd.getTongTien());
        tv_soluong.setText(String.valueOf(hd.getSoLuong()));
        tv_size.setText(hd.getSize());
        tv_diachi.setText(hd.getDiaChi());
        tv_sdt.setText(hd.getSoDT());
        tv_trangthai.setText(hd.isDaNhanHang() ? "Đã nhận hàng" : "Chưa nhận hàng");

        bt_qlai.setOnClickListener(v1-> dialog.dismiss());
        dialog.show();

    }

    public void dulieu() {
        firebaseHelper.getAllHoaDonToAdmin(new FirebaseHelper.HoaDonCallback() {
            @Override
            public void onDataReceived(ArrayList<HoaDon> hd) {
                // Cập nhật danh sách sản phẩm
                ds.clear();  // Xóa danh sách cũ trước khi thêm dữ liệu mới
                ds.addAll(hd);  // Thêm hóa từ Firebase vào danh sách

                // Cập nhật giao diện RecyclerView
                if (ds.isEmpty()) {
                    rcv.setVisibility(View.GONE);
                } else {
                    rcv.setVisibility(View.VISIBLE);
                    //hiển thị rcv sp
                    adapterQl.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure() {
                // Xử lý khi lấy dữ liệu thất bại
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
