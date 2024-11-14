package TrangChu_Admin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.duan_1.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import Banner.BannerFragmentAdapter;
import Helper.FirebaseHelper;
import Model.SanPham;

public class Admin_TrangChuFragment extends Fragment {
    private RecyclerView rcv;
    private Button bt_them;
    private TextView tv_linkanh;
    private Admin_TrangChu_Adapter adapterSP;
    private ArrayList<SanPham> ds;
    private FirebaseHelper firebaseHelper;
    private Uri imageUri = null; // Lưu URI của ảnh đã chọn
    private ViewPager2 bannerViewPager;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmenttrangchu_admin, container, false);
        rcv = v.findViewById(R.id.rcvSP);
        bt_them = v.findViewById(R.id.bt_themsp);
        bannerViewPager = v.findViewById(R.id.bannerViewPager);

        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
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

        // Gọi hàm thêm sản phẩm vào Firebase
        firebaseHelper = new FirebaseHelper();
        adapterSP = new Admin_TrangChu_Adapter(ds, getContext(),this);
        rcv.setAdapter(adapterSP);

        bt_them.setOnClickListener(v1 -> dialogThemSP());

        dulieu();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable); // Ngăn chặn việc cuộn sau khi fragment bị hủy
    }

    // Đăng ký launcher để chọn ảnh
    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    String imageName = getImageName(imageUri);
                    displayImage(imageUri, imageName, tv_linkanh);
                }
            });

    // Lấy tên ảnh từ URI
    public String getImageName(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

    // Hàm thêm sản phẩm vào Firebase
    public void dialogThemSP() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inf = getLayoutInflater();
        View v = inf.inflate(R.layout.themsp, null);

        EditText et_tensp = v.findViewById(R.id.et_tensp);
        CheckBox cb_sizeM = v.findViewById(R.id.cb_sizeM);
        CheckBox cb_sizeL = v.findViewById(R.id.cb_sizeL);
        EditText et_gia = v.findViewById(R.id.et_gia);
        Button bt_them = v.findViewById(R.id.bt_themsp);
        Button bt_huy = v.findViewById(R.id.bt_huy);
        Button bt_choanh = v.findViewById(R.id.bt_chonanh);
        tv_linkanh = v.findViewById(R.id.tv_linkanh);

        // Mở thư viện ảnh khi nhấn vào button chọn ảnh
        bt_choanh.setOnClickListener(v1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        bt_them.setOnClickListener(v1 -> {
            String tensp = et_tensp.getText().toString().trim();
            int giasp;
            try {
                giasp = Integer.parseInt(et_gia.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> sizes = new ArrayList<>();
            if (cb_sizeM.isChecked()) sizes.add("M");
            if (cb_sizeL.isChecked()) sizes.add("L");

            // Kiểm tra nếu người dùng chưa chọn ảnh
            if (imageUri == null) {
                Toast.makeText(getContext(), "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu ảnh lên Firebase Storage

            // Chỉ lấy link ảnh từ Google Drive hoặc dịch vụ lưu trữ khác
            String imageUrl = tv_linkanh.getText().toString().trim();

            // Thêm sản phẩm vào Firebase Realtime Database
            String idsp = FirebaseDatabase.getInstance().getReference("sanpham").push().getKey();
            SanPham sp = new SanPham(idsp, tensp, giasp, sizes, imageUrl);

            firebaseHelper.themSanPham(sp, new FirebaseHelper.OnCompleteListener() {
                @Override
                public void onSuccess() {
                    ds.add(sp);
                    adapterSP.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Hủy thêm sản phẩm
        builder.setView(v);
        AlertDialog dialog = builder.create();
        bt_huy.setOnClickListener(v1 -> dialog.dismiss());
        dialog.show();
    }

    public void displayImage(Uri imageUri, String imageName, TextView tv_linkanh) {
        try {
            tv_linkanh.setText(imageName); // Hiển thị tên ảnh vào TextView trong dialog
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dulieu() {
        firebaseHelper.getAllSanPham(new FirebaseHelper.DataCallback() {
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
                    adapterSP.notifyDataSetChanged();  // Thông báo cho adapter rằng dữ liệu đã thay đổi
                }
            }

            @Override
            public void onFailure() {
                // Xử lý khi lấy dữ liệu thất bại
                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void xoasp(String idsp)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn có muốn xóa");
        builder.setCancelable(false);


        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("sanpham").child(idsp).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                            dulieu(); // Tải lại danh sách sau khi xóa
                        });
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
