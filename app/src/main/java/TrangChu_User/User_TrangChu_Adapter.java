package TrangChu_User;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import GioHang.GioHangFragment;
import GioHang.GioHang_Adapter;
import Model.HoaDon;
import Model.SanPham;
import TrangChu_User.User_TrangChu_Viewholder;
import ZaloPay.Api.CreateOrder;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class User_TrangChu_Adapter extends RecyclerView.Adapter<User_TrangChu_Viewholder>{
    ArrayList<SanPham> ds ;
    GioHang_Adapter adapter;
    Context context;
    User_TrangChuFragment f;
    ArrayList<SanPham> gioHangList;
    GioHangFragment fGH;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    public User_TrangChu_Adapter(ArrayList<SanPham> ds, Context context, User_TrangChuFragment f) {
        this.ds = ds;
        this.context = context;
        this.f=f;
    }
    public User_TrangChu_Adapter(ArrayList<SanPham> ds, Context context, GioHangFragment fGH) {
        this.ds = ds;
        this.context = context;
        this.fGH=fGH;
        this.gioHangList = fGH.getGioHangList(); // Lấy danh sách giỏ hàng từ fragment
        this.adapter = fGH.getAdapter(); // Lấy adapter từ fragment
    }

    @NonNull
    @Override
    public User_TrangChu_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sanpham_viewholder_user,parent,false);

        return new User_TrangChu_Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull User_TrangChu_Viewholder holder, @SuppressLint("RecyclerView") int position) {
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

        holder.bt_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.bt_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sp=ds.get(position);
                f.muasp(sp);
            }
        });

    }

    public void phuongThuc(String tensp,String diaChi, String soDT, String tongTien, String finalSize, SanPham sp, int tongSl)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.ptthanhtoan, null);

        TextView tv_back=v.findViewById(R.id.tv_back);
        Button bt_offline=v.findViewById(R.id.bt_offline);
        Button bt_online=v.findViewById(R.id.bt_online);


        bt_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(tongTien);
                    String code = data.getString("return_code");
                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder((Activity) context, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                Intent intent=new Intent((Activity) context,User_TrangChuFragment.class);
                                intent.putExtra("result","Thành công");
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {

                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });





        builder.setView(v);
        AlertDialog dialog = builder.create();
        bt_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hoaDon(tensp, diaChi, soDT, tongTien, finalSize, sp, tongSl);// truyền dữ liệu cho hóa đơn
                dialog.dismiss();
            }
        });


        tv_back.setOnClickListener(v1-> dialog.dismiss());
        dialog.show();
    }



    public void hoaDon(String tensp, String diaChi, String soDT, String tongTien, String finalSize, SanPham sp, int tongSl)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.hoadon, null);

        TextView tv_tensp,tv_size,tv_soluong,tv_gia,tv_diachi,tv_sdt;
        Button bt_huy,bt_xacnhan;

        tv_tensp=v.findViewById(R.id.tv_tensp);
        tv_size=v.findViewById(R.id.tv_size);
        tv_soluong=v.findViewById(R.id.tv_soluong);
        tv_gia=v.findViewById(R.id.tv_gia);
        tv_sdt=v.findViewById(R.id.tv_sdt);
        tv_diachi=v.findViewById(R.id.tv_diachi);

        bt_huy=v.findViewById(R.id.bt_huy);
        bt_xacnhan=v.findViewById(R.id.bt_mua);


        // Hiển thị thông tin hóa đơn trên dialog
        tv_tensp.setText(tensp);
        tv_size.setText(finalSize);
        tv_soluong.setText(String.valueOf(tongSl));
        tv_gia.setText(tongTien);
        tv_diachi.setText(diaChi);
        tv_sdt.setText(soDT);


        builder.setView(v);
        AlertDialog dialog = builder.create();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        bt_xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy tham chiếu Firebase
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("HoaDon");

                String userId = user.getUid();
                // Tạo ID duy nhất cho hóa đơn
                String hoaDonId = ref.push().getKey(); // Tạo ID ngẫu nhiên từ Firebase
                HoaDon hoaDon = new HoaDon(userId,hoaDonId, tensp, diaChi, soDT, tongTien, finalSize, tongSl,false,false);

                //Lưu hóa đơn
                gioHangList = new ArrayList<>();
                if (hoaDonId != null) {
                    ref.child(userId).child(hoaDonId).setValue(hoaDon).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                            Log.d("Firebase", "Hóa đơn đã được lưu thành công!");
                        } else {
                            Log.e("Firebase", "Lỗi khi lưu hóa đơn: ", task.getException());
                        }
                    });
                }


                // Sau khi đặt hàng thành công, xóa sản phẩm khỏi Firebase
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (userID != null) {
                    DatabaseReference gioHangRef = FirebaseDatabase.getInstance()
                            .getReference("gioHang")
                            .child(userID)
                            .child(sp.getIdsp());

                    gioHangRef.removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    gioHangList.remove(sp); // Cập nhật danh sách trong bộ nhớ
                                    if (adapter != null) {
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.e("GioHang_Adapter", "Adapter is null!");
                                    }
                                }
                            });
                }

                // Đóng dialog sau khi hoàn tất
                dialog.dismiss();
            }
        });

        bt_huy.setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();

    }



    @Override
    public int getItemCount() {
        return ds.size();
    }


}
