package TrangChu_User;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Banner.BannerFragmentAdapter;
import Helper.FirebaseHelper;
import Model.SanPham;


public class User_TrangChuFragment extends Fragment {
    private RecyclerView rcv;
    private ArrayList<SanPham> ds;
    private FirebaseHelper firebaseHelper;
    private ViewPager2 bannerViewPager;
    private Handler handler;
    private Runnable runnable;
    private User_TrangChu_Adapter adapterSP;
    private String tongTien = "0";
    private int tongSl;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmenttrangchu_user, container, false);
        rcv = v.findViewById(R.id.rcvSP);
        bannerViewPager = v.findViewById(R.id.bannerViewPager);
        firebaseHelper = new FirebaseHelper();

        rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ds = new ArrayList<>();


        // Cấu hình banner
        List<Integer> bannerImages = Arrays.asList(R.drawable.banner1, R.drawable.banner2, R.drawable.banner3);
        BannerFragmentAdapter adapter = new BannerFragmentAdapter(requireActivity(), bannerImages);
        bannerViewPager.setAdapter(adapter);

        handler = new Handler();
        runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == bannerImages.size()) {
                    currentPage = 0;
                }
                bannerViewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 1000);

        adapterSP = new User_TrangChu_Adapter(ds, getContext(),this);
        rcv.setAdapter(adapterSP);
        dulieu();

        return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable); // Ngăn chặn việc cuộn sau khi fragment bị hủy
    }

    //mua
    public void muasp(SanPham sp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = getLayoutInflater();
        View v = inf.inflate(R.layout.muasp, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        TextView tv_tensp = v.findViewById(R.id.tv_tensp);
        TextView tv_giaM = v.findViewById(R.id.tv_giaM);
        TextView tv_giaL = v.findViewById(R.id.tv_giaL);
        CheckBox cb_sizeM = v.findViewById(R.id.cb_sizeM);
        TextView tv_size=v.findViewById(R.id.tv_size);
        CheckBox cb_sizeL = v.findViewById(R.id.cb_sizeL);
        Button bt_mua = v.findViewById(R.id.bt_mua);
        Button bt_huy = v.findViewById(R.id.bt_huy);
        Button bt_themg=v.findViewById(R.id.bt_themgh);

        tv_tensp.setText(sp.getTensp());

        // Kiểm tra xem có giá cho size M và L không
        boolean hasSizeM = sp.getGiaSp().containsKey("M");
        boolean hasSizeL = sp.getGiaSp().containsKey("L");

        // Cập nhật giá cho size M và L
        if (hasSizeM) {
            tv_giaM.setText(String.valueOf(sp.getGiaSp().get("M")));
            tv_giaM.setVisibility(View.VISIBLE);
            cb_sizeM.setVisibility(View.VISIBLE); // Hiển thị checkbox size M
            tv_size.setText("M");
        } else {
            tv_giaM.setVisibility(View.GONE);
            cb_sizeM.setVisibility(View.GONE); // Ẩn checkbox size M nếu không có
        }

        if (hasSizeL) {
            tv_giaL.setText(String.valueOf(sp.getGiaSp().get("L")));
            tv_giaL.setVisibility(View.VISIBLE);
            cb_sizeL.setVisibility(View.VISIBLE); // Hiển thị checkbox size L
            tv_size.setText("L");
        } else {
            tv_giaL.setVisibility(View.GONE);
            cb_sizeL.setVisibility(View.GONE); // Ẩn checkbox size L nếu không có
        }




        // Nếu chỉ có size L, tự động chọn size L
        if (!hasSizeM && hasSizeL) {
            cb_sizeL.setChecked(true); // Mặc định chọn size L
            tv_giaM.setVisibility(View.GONE); // Ẩn giá size M
            tv_giaL.setVisibility(View.VISIBLE); // Hiển thị giá size L
        }

        // Nếu cả 2 size có, mặc định chọn size M
        if (hasSizeM && !hasSizeL) {
            cb_sizeM.setChecked(true); // Mặc định chọn size M
            tv_giaM.setVisibility(View.VISIBLE);
            tv_giaL.setVisibility(View.GONE);
        } else if (hasSizeM && hasSizeL) {
            cb_sizeM.setChecked(true); // Mặc định chọn size M khi cả 2 size có
            tv_giaM.setVisibility(View.VISIBLE);
            tv_giaL.setVisibility(View.GONE);
            tv_size.setText("M");
        }



        // Xử lý sự kiện thay đổi chọn size M
        cb_sizeM.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Nếu chọn size M, bỏ chọn size L
                cb_sizeL.setChecked(false); // Bỏ chọn size L
                tv_giaM.setVisibility(View.VISIBLE); // Hiển thị giá của size M
                tv_giaL.setVisibility(View.GONE); // Ẩn giá của size L
                tv_size.setText("M");
            } else {
                // Nếu bỏ chọn size M, nhưng size L có sẵn, thì chọn lại size M
                if (!cb_sizeL.isChecked()) {
                    cb_sizeM.setChecked(true); // Không cho phép bỏ chọn cả hai
                    Toast.makeText(getContext(), "Vui lòng chọn một size", Toast.LENGTH_SHORT).show(); // Thông báo
                }
            }
        });

// Xử lý sự kiện thay đổi chọn size L
        cb_sizeL.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Nếu chọn size L, bỏ chọn size M
                cb_sizeM.setChecked(false); // Bỏ chọn size M
                tv_giaL.setVisibility(View.VISIBLE); // Hiển thị giá của size L
                tv_giaM.setVisibility(View.GONE); // Ẩn giá của size M
                tv_size.setText("L");
            } else {
                // Nếu bỏ chọn size L, nhưng size M có sẵn, thì chọn lại size L
                if (!cb_sizeM.isChecked()) {
                    cb_sizeL.setChecked(true); // Không cho phép bỏ chọn cả hai
                    Toast.makeText(getContext(), "Vui lòng chọn một size", Toast.LENGTH_SHORT).show(); // Thông báo
                }
            }
        });

        //ẩn ln
        LinearLayout layoutDiaChiSDT = v.findViewById(R.id.ln_dc_sdt);
        LinearLayout layoutSoLuong = v.findViewById(R.id.ln_sl);
        layoutSoLuong.setVisibility(View.GONE);
        layoutDiaChiSDT.setVisibility(View.GONE);
        TextView hien=v.findViewById(R.id.hien);

        EditText et_diachi,et_sdt,et_soluong;
        et_soluong=v.findViewById(R.id.et_soluong);
        et_diachi=v.findViewById(R.id.et_diachi);
        et_sdt=v.findViewById(R.id.et_sdt);
        // Xử lý sự kiện nhấn nút Mua
        bt_mua.setOnClickListener(v1 -> {

            //ẩn
            bt_huy.setVisibility(View.GONE);
            bt_themg.setVisibility(View.GONE);
            cb_sizeL.setVisibility(View.GONE);
            cb_sizeM.setVisibility(View.GONE);
            //hiện
            bt_mua.setVisibility(View.VISIBLE);
            layoutDiaChiSDT.setVisibility(View.VISIBLE);
            layoutSoLuong.setVisibility(View.VISIBLE);
            hien.setVisibility(View.VISIBLE);
            tv_size.setVisibility(View.VISIBLE);

            String size = "";

            // Kiểm tra xem người dùng đã chọn size M
            if (cb_sizeM.isChecked()) {
                size = "M"; // Lưu size M
            }

            // Kiểm tra xem người dùng đã chọn size L
            if (cb_sizeL.isChecked()) {
                size = "L"; // Lưu size L
            }
            String finalSize = size;
            et_soluong.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String soluongStr = editable.toString();
                    int soluong = 0;  // Mặc định là 1 nếu không có số lượng nhập vào
                    if (!soluongStr.isEmpty()) {
                        soluong = Integer.parseInt(soluongStr);
                    }
                    tongSl=soluong;

                    // Tính lại giá dựa trên số lượng và size đã chọn
                    String gia = "";
                    if (cb_sizeM.isChecked()) {
                        gia = String.valueOf(sp.getGiaSp().get("M"));
                    } else if (cb_sizeL.isChecked()) {
                        gia = String.valueOf(sp.getGiaSp().get("L"));
                    }

                    if (!gia.isEmpty()) {
                        int totalPrice = Integer.parseInt(gia) * soluong;  // Tính giá tổng
                        if (cb_sizeL.isChecked()){
                            tv_giaL.setText(String.valueOf(totalPrice));
                        }
                        if (cb_sizeM.isChecked()){
                            tv_giaM.setText(String.valueOf(totalPrice));
                        }
                        tongTien = String.valueOf(totalPrice);
                    }
                }
            });

            String tensp=sp.getTensp();





            bt_mua.setOnClickListener(v12 -> {
                String diaChi = et_diachi.getText().toString();
                String soDT = et_sdt.getText().toString();
                String soLuong=et_soluong.getText().toString();

                if (diaChi.isEmpty() || soDT.isEmpty()|| soLuong.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hiển thị dialog chọn phương thức thanh toán
                User_TrangChu_Adapter adp= new User_TrangChu_Adapter(ds,getContext(),this);
                adp.phuongThuc(tensp,diaChi, soDT, tongTien,finalSize, sp,tongSl);
                dialog.dismiss();
            });






        });
        bt_themg.setOnClickListener(v12 -> {
            String gia;
            String size;

            if (cb_sizeM.isChecked()) {
                gia = String.valueOf(sp.getGiaSp().get("M"));
                size = "M";
            } else if (cb_sizeL.isChecked()) {
                gia = String.valueOf(sp.getGiaSp().get("L"));
                size = "L";
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn một size", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy userID từ Firebase Authentication
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (userID == null) {
                Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo khóa duy nhất cho sản phẩm theo size
            String sanPhamKey = sp.getIdsp() + "_" + size;

            DatabaseReference gioHangRef = FirebaseDatabase.getInstance().getReference("gioHang").child(userID).child(sanPhamKey);

            gioHangRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    // Nếu sản phẩm đã tồn tại, tăng số lượng
                    int currentQuantity = task.getResult().child("soLuong").getValue(Integer.class);
                    gioHangRef.child("soLuong").setValue(currentQuantity + 1).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật số lượng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Nếu sản phẩm chưa tồn tại, thêm mới
                    HashMap<String, Object> sanPhamInfo = new HashMap<>();
                    sanPhamInfo.put("tenSanPham", sp.getTensp());
                    sanPhamInfo.put("gia", gia);
                    sanPhamInfo.put("size", size);
                    sanPhamInfo.put("soLuong", 1); // Mặc định số lượng là 1

                    gioHangRef.setValue(sanPhamInfo).addOnCompleteListener(addTask -> {
                        if (addTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            dialog.dismiss(); // Đóng dialog sau khi thêm sản phẩm
        });

        bt_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    public void dulieu() {
        firebaseHelper.getAllSanPham(new FirebaseHelper.SanPhamCallback() {
            @Override
            public void onDataReceived(ArrayList<SanPham> sanPhams) {
                // Cập nhật danh sách sản phẩm
                ds.clear();  // Xóa danh sách cũ trước khi thêm dữ liệu mới
                ds.addAll(sanPhams);  // Thêm sản phẩm từ Firebase vào danh sách

                // Cập nhật giao diện RecyclerView
                if (ds.isEmpty()) {
                    rcv.setVisibility(View.GONE);
                } else {
                    rcv.setVisibility(View.VISIBLE);
                    //hiển thị rcv sp
                    adapterSP.notifyDataSetChanged();
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