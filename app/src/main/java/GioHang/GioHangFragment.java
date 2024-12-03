package GioHang;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Banner.BannerFragmentAdapter;
import Helper.FirebaseHelper;

import Model.SanPham;
import TrangChu_User.User_TrangChu_Adapter;
import Helper.FirebaseHelper;

public class GioHangFragment extends Fragment {
    private RecyclerView rcv;
    private GioHang_Adapter adapter;
    private ArrayList<SanPham> gioHangList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giohang, container, false);

        rcv = view.findViewById(R.id.rcvGH);
        gioHangList = new ArrayList<>();

        // Set up RecyclerView
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GioHang_Adapter(gioHangList, getContext(), this);
        rcv.setAdapter(adapter);
        // Load data from Firebase
        loadGioHang();

        return view;
    }
    public void muasp(SanPham sp)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = getLayoutInflater();
        View v = inf.inflate(R.layout.muasp_gh, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        TextView tv_tensp = v.findViewById(R.id.tv_tensp);
        TextView tv_gia = v.findViewById(R.id.tv_gia);
        TextView tv_size = v.findViewById(R.id.tv_size);
        TextView tv_soluong=v.findViewById(R.id.tv_soluong);
        Button bt_mua = v.findViewById(R.id.bt_mua);
        Button bt_huy = v.findViewById(R.id.bt_huy);

        String tenSp = sp.getTensp();
        Map<String, Integer> giaSp = sp.getGiaSp();
        List<String> size = sp.getSize();
        tv_tensp.setText(tenSp);
        tv_soluong.setText(String.valueOf(sp.getSoLuong()));

        // Hiển thị giá sản phẩm
        String sizeChosen = size.get(0);
        int gia = giaSp.get(sizeChosen);
        int soLuong = Integer.parseInt(tv_soluong.getText().toString());
        int giaTong = gia * soLuong;
        tv_gia.setText(String.valueOf(giaTong));

        // Hiển thị các size của sản phẩm mà không có dấu []
        StringBuilder sizeList = new StringBuilder();
        for (String s : size) {
            sizeList.append(s).append(", ");
        }

        // Xóa dấu phẩy cuối cùng nếu có
        if (sizeList.length() > 0) {
            sizeList.setLength(sizeList.length() - 2);
        }
        tv_size.setText(sizeList.toString());  // Hiển thị size mà không có dấu []


        LinearLayout layoutDiaChiSDT = v.findViewById(R.id.ln_dc_sdt);
        layoutDiaChiSDT.setVisibility(View.GONE);


        EditText et_diachi,et_sdt;
        et_diachi=v.findViewById(R.id.et_diachi);
        et_sdt=v.findViewById(R.id.et_sdt);
        bt_mua.setOnClickListener(v1->
        {
            //hiện
            bt_mua.setVisibility(View.VISIBLE);
            layoutDiaChiSDT.setVisibility(View.VISIBLE);
            tv_size.setVisibility(View.VISIBLE);


            bt_mua.setOnClickListener(v12 -> {
                String diaChi = et_diachi.getText().toString();
                String soDT = et_sdt.getText().toString();
                String giaStr = String.valueOf(giaTong);
                String sizeStr = String.join(", ", size);


                if (diaChi.isEmpty() || soDT.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Hiển thị dialog chọn phương thức thanh toán

                User_TrangChu_Adapter adp= new User_TrangChu_Adapter(gioHangList,getContext(),this);
                adp.phuongThuc(tenSp,diaChi, soDT, giaStr,sizeStr, sp,soLuong);
                dialog.dismiss();

            });

        });

        bt_huy.setOnClickListener(v1-> dialog.dismiss());
        dialog.show();
    }



    private void loadGioHang() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userID != null) {
            DatabaseReference gioHangRef = FirebaseDatabase.getInstance().getReference("gioHang").child(userID);
            gioHangRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    gioHangList.clear(); // Clear old data

                    for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                        try {
                            // Lấy productID từ key của sản phẩm trong Firebase
                            String productID = productSnapshot.getKey(); // Đây chính là productID

                            // Lấy các thuộc tính khác của sản phẩm
                            String tenSanPham = productSnapshot.child("tenSanPham").getValue(String.class);
                            String gia = productSnapshot.child("gia").getValue(String.class);
                            String size = productSnapshot.child("size").getValue(String.class);
                            Long soLuong = productSnapshot.child("soLuong").getValue(Long.class);

                            // Tạo đối tượng SanPham và gán thông tin
                            SanPham sanPham = new SanPham();
                            sanPham.setIdsp(productID);  // Gán productID
                            sanPham.setTensp(tenSanPham);

                            // Map giá sản phẩm
                            Map<String, Integer> giaSp = new HashMap<>();
                            giaSp.put(size, Integer.parseInt(gia));

                            sanPham.setGiaSp(giaSp);
                            sanPham.setSize(Arrays.asList(size));
                            sanPham.setImage(sanPham.getImage());
                            sanPham.setSoLuong(soLuong.intValue()); // Gán số lượng

                            gioHangList.add(sanPham); // Thêm vào danh sách giỏ hàng
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Lỗi khi xử lý dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Cập nhật adapter sau khi có thay đổi dữ liệu
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Lỗi khi tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public GioHang_Adapter getAdapter() {
        if (adapter == null) {
            adapter = new GioHang_Adapter(gioHangList, getContext(), this); // Khởi tạo adapter nếu null
        }
        return adapter;
    }

    public ArrayList<SanPham> getGioHangList() {
        if (gioHangList == null) {
            gioHangList = new ArrayList<>(); // Khởi tạo danh sách nếu null
        }
        return gioHangList;
    }









}