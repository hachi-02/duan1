package GioHang;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import Model.SanPham;
import TrangChu_User.User_TrangChuFragment;
import TrangChu_User.User_TrangChu_Viewholder;

public class GioHang_Adapter extends RecyclerView.Adapter<GioHang_Viewholder>{
    ArrayList<SanPham> ds ;
    Context context;
    GioHangFragment f;
    public GioHang_Adapter(ArrayList<SanPham> ds, Context context, GioHangFragment f) {
        this.ds = ds;
        this.context = context;
        this.f=f;
    }

    @NonNull
    @Override
    public GioHang_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_giohang_viewholder,parent,false);

        return new GioHang_Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHang_Viewholder holder, @SuppressLint("RecyclerView") int position) {
        SanPham sp= ds.get(position);

        holder.tv_tensp.setText(sp.getTensp()+"");
        holder.tv_sl.setText(sp.getSoLuong()+"");
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
        Picasso.get()
                .load(sp.getImage()) // URL hình ảnh dạng String
                .into(holder.img);
        if (sp.getImage() != null && !sp.getImage().isEmpty()) {
            Picasso.get().load(sp.getImage()).placeholder(R.drawable.trasua1).into(holder.img);
        } else {
            holder.img.setImageResource(R.drawable.trasua1); // Hiển thị ảnh mặc định khi lỗi
        }
        // Xử lý sự kiện tăng số lượng
        holder.tv_tang_sl.setOnClickListener(v -> {
            int newSoLuong = sp.getSoLuong() + 1;
            sp.setSoLuong(newSoLuong);
            holder.tv_sl.setText(String.valueOf(newSoLuong));

            // Kiểm tra nếu productID hợp lệ
            String productID = sp.getIdsp(); // Lấy productID từ SanPham

            if (productID != null) {
                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("gioHang")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Lấy userID từ Firebase
                        .child(productID);  // Sử dụng productID để cập nhật

                productRef.child("soLuong").setValue(newSoLuong);
            }
        });


        holder.tv_giam_sl.setOnClickListener(v -> {
            int newSoLuong = sp.getSoLuong() > 1 ? sp.getSoLuong() - 1 : 1; // Đảm bảo số lượng không nhỏ hơn 1
            sp.setSoLuong(newSoLuong);
            holder.tv_sl.setText(String.valueOf(newSoLuong));

            // Kiểm tra nếu productID hợp lệ
            String productID = sp.getIdsp(); // Lấy productID từ SanPham

            if (productID != null) {
                // Cập nhật số lượng trong Firebase
                DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("gioHang")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) // Lấy userID từ Firebase
                        .child(productID);  // Sử dụng productID để cập nhật

                // Cập nhật số lượng mới
                productRef.child("soLuong").setValue(newSoLuong);
            }
        });

        holder.bt_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productID = sp.getIdsp(); // Lấy productID của sản phẩm

                if (productID != null) {
                    // Xóa sản phẩm khỏi Firebase
                    FirebaseDatabase.getInstance().getReference("gioHang")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(productID)
                            .removeValue();

                    // Xóa sản phẩm khỏi danh sách
                    ds.remove(position);

                    // Cập nhật RecyclerView
                    notifyItemRemoved(position);

                    // Thông báo cho người dùng
                    Toast.makeText(context, "Đã xóa sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.bt_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.muasp(sp);
            }
        });

    }




    @Override
    public int getItemCount() {
        return ds.size();
    }
}

