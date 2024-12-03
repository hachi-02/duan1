package DonHang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.HoaDon;

public class DonHang_Apdapter extends RecyclerView.Adapter<DonHang_Viewholder> {
    ArrayList<HoaDon> ds ;
    Context context;
    DonHangFragment f;
    public DonHang_Apdapter(ArrayList<HoaDon> ds, Context context,DonHangFragment f ) {
        this.ds = ds;
        this.context = context;
        this.f=f;
    }
    @NonNull
    @Override
    public DonHang_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_donhang_viewholder,parent,false);

        return new DonHang_Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHang_Viewholder holder, int position) {
        HoaDon hd= ds.get(position);
        holder.tv_tensp.setText(hd.getTensp());
        holder.tv_size.setText(hd.getSize());
        holder.tv_soluong.setText(String.valueOf(hd.getSoLuong()));
        holder.tv_gia.setText(hd.getTongTien());


        if (hd.isXacNhan()) {
            holder.bt_xacnhan.setVisibility(View.VISIBLE); // Hiện "Đã xác nhận"
            holder.bt_huy.setVisibility(View.GONE); // Ẩn nút hủy
            holder.tv_dxn.setVisibility(View.GONE);
            if(hd.isDaNhanHang())
            {
                holder.tv_dxn.setVisibility(View.VISIBLE);
                holder.bt_xacnhan.setVisibility(View.GONE);
            }
        } else {
            holder.bt_xacnhan.setVisibility(View.GONE); // Ẩn "Đã xác nhận"
            holder.bt_huy.setVisibility(View.VISIBLE); // Hiện nút hủy
            holder.tv_dxn.setVisibility(View.GONE);
        }

        holder.bt_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = hd.getUserID();
                String hoadonID = hd.getHoadonID();
                FirebaseDatabase.getInstance().getReference("HoaDon")
                        .child(userID)
                        .child(hoadonID)
                        .child("daNhanHang")
                        .setValue(true);
                hd.setDaNhanHang(true);
                notifyItemChanged(position);
                Toast.makeText(context, "Đã nhận được hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoadonID = hd.getHoadonID();
                //lấy id của người dùng hiện tại
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (hoadonID != null) {
                    // Xóa sản phẩm khỏi Firebase
                    FirebaseDatabase.getInstance().getReference("HoaDon")
                            .child(userID)
                            .child(hoadonID)
                            .removeValue();

                    ds.remove(position);

                    // Cập nhật RecyclerView
                    notifyItemRemoved(position);

                    // Thông báo cho người dùng
                    Toast.makeText(context, "Đã hủy!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.tv_chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.chitiet(hd);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }
}
