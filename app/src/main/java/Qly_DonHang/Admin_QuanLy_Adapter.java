package Qly_DonHang;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import DonHang.DonHangFragment;
import DonHang.DonHang_Viewholder;
import Model.HoaDon;

public class Admin_QuanLy_Adapter extends RecyclerView.Adapter<Admin_QuanLy_Viewholder> {
    ArrayList<HoaDon> ds ;
    Context context;
    Admin_QuanLyFragment f;
    public Admin_QuanLy_Adapter(ArrayList<HoaDon> ds, Context context,Admin_QuanLyFragment f )
    {
        this.ds = ds;
        this.context = context;
        this.f=f;
    }
    @NonNull
    @Override
    public Admin_QuanLy_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_qlydh_viewholder,parent,false);

        return new Admin_QuanLy_Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_QuanLy_Viewholder holder, int position) {
        HoaDon hd= ds.get(position);
        holder.tv_tensp.setText(hd.getTensp());
        holder.tv_size.setText(hd.getSize());
        holder.tv_soluong.setText(String.valueOf(hd.getSoLuong()));
        holder.tv_gia.setText(hd.getTongTien());


        holder.tv_chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.chitiet(hd);
            }
        });

        holder.bt_huy.setOnClickListener(v1->
        {
            String userID = hd.getUserID();
            String hoadonID = hd.getHoadonID();
            FirebaseDatabase.getInstance().getReference("HoaDon")
                    .child(userID)
                    .child(hoadonID)
                    .removeValue();
            ds.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();

        });


        if (hd.isXacNhan()) {
            holder.tv_dxn.setVisibility(View.VISIBLE); // Hiện "Đã xác nhận"
            holder.ln_huyvcn.setVisibility(View.GONE); // Ẩn nút hủy và xác nhận
        } else {
            holder.tv_dxn.setVisibility(View.GONE); // Ẩn "Đã xác nhận"
            holder.ln_huyvcn.setVisibility(View.VISIBLE); // Hiện nút hủy và xác nhận
        }
        holder.bt_xacnhan.setOnClickListener(v1->
        {
            String userID = hd.getUserID();
            String hoadonID = hd.getHoadonID();
            FirebaseDatabase.getInstance().getReference("HoaDon")
                    .child(userID)
                    .child(hoadonID)
                    .child("xacNhan")
                    .setValue(true);
            hd.setXacNhan(true);
            notifyItemChanged(position);
            Toast.makeText(context, "Xác nhận thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }
}
