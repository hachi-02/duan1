package TrangChu_Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import Model.SanPham;

public class Admin_TrangChu_Adapter extends RecyclerView.Adapter<Admin_TrangChu_Viewholder>{
    ArrayList<SanPham> ds ;
    Context context;
    Admin_TrangChuFragment f;
    public Admin_TrangChu_Adapter(ArrayList<SanPham> ds, Context context,Admin_TrangChuFragment f) {
        this.ds = ds;
        this.context = context;
        this.f=f;
    }

    @NonNull
    @Override
    public Admin_TrangChu_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sanpham_viewholder_admin,parent,false);

        return new Admin_TrangChu_Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_TrangChu_Viewholder holder, @SuppressLint("RecyclerView") int position) {
        SanPham sp= ds.get(position);

        holder.tv_idsp.setText(sp.getIdsp()+"");
        holder.tv_tensp.setText(sp.getTensp()+"");
        Map<String, Integer> giaMap = sp.getGiaSp();
        Map<String, Integer> giaLap = sp.getGiaSp();
        Integer giaM = giaMap.get("M");
        Integer giaL=giaLap.get("L");
        if (giaM != null&&giaL==null) {
            holder.tv_giasp.setText("Giá: " + giaM );
        }
        if (giaM ==null&&giaL!=null) {
            holder.tv_giasp.setText("Giá: " + giaL );
        }
        if (giaM !=null&&giaL!=null) {
            holder.tv_giasp.setText("Giá: " + giaM );
        }

        Picasso.get()
                .load(sp.getImage()) // URL hình ảnh dạng String
                .into(holder.img);
        if (sp.getImage() != null && !sp.getImage().isEmpty()) {
            Picasso.get().load(sp.getImage()).placeholder(R.drawable.trasua1).into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.trasua1); // Hiển thị ảnh mặc định khi lỗi
        }
        holder.ln_sizeM.removeAllViews();
        holder.ln_sizeL.removeAllViews();

        holder.ln_sizeM.setVisibility(View.GONE);
        holder.ln_sizeL.setVisibility(View.GONE);

        // Hiển thị kích thước M (nếu có)
        if (sp.getSize().contains("M")) {
            TextView sizeM = new TextView(holder.itemView.getContext());
            sizeM.setText("M");
            sizeM.setPadding(8, 8, 8, 8); // Thêm khoảng cách
            sizeM.setTextSize(15);  // Kích thước chữ
            holder.ln_sizeM.addView(sizeM);  // Thêm vào LinearLayout cho size M
            holder.ln_sizeM.setVisibility(View.VISIBLE);
        }
        else {
            holder.ln_sizeM.setVisibility(View.GONE); // Ẩn nếu không có size M
        }

        // Hiển thị kích thước L (nếu có)
        if (sp.getSize().contains("L")) {
            TextView sizeL = new TextView(holder.itemView.getContext());
            sizeL.setText("L");
            sizeL.setPadding(8, 8, 8, 8);  // Thêm khoảng cách
            sizeL.setTextSize(15);  // Kích thước chữ
            holder.ln_sizeL.addView(sizeL);  // Thêm vào LinearLayout cho size L
            holder.ln_sizeL.setVisibility(View.VISIBLE);


            //xóa khoảng cách khi không chọn size M
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.ln_sizeL.getLayoutParams();
            if (holder.ln_sizeM.getVisibility() == View.VISIBLE) {
                params.leftMargin = 20;  // Nếu size M hiển thị, giữ margin
            } else {
                params.leftMargin = 0;  // Nếu không có size M, loại bỏ margin
            }
            holder.ln_sizeL.setLayoutParams(params);
        }
        else {
            holder.ln_sizeL.setVisibility(View.GONE); // Ẩn nếu không có size L
        }

        holder.bt_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masp=ds.get(position).getIdsp();
                f.xoasp(masp);
            }
        });
        holder.bt_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sp=ds.get(position);
                f.suasp(sp);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ds.size();
    }
}
