package GioHang;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;

public class GioHang_Viewholder extends RecyclerView.ViewHolder {
    TextView tv_tensp,tv_giasp,tv_sl, tv_giam_sl,tv_tang_sl;
    LinearLayout ln_sizeM,ln_sizeL;
    CheckBox cb;
    ImageView img;
    Button bt_thanhtoan,bt_xoa;
    public GioHang_Viewholder(@NonNull View itemView)
    {
        super(itemView);
        this.tv_tensp=itemView.findViewById(R.id.tv_tensp);
        this.tv_giasp=itemView.findViewById(R.id.tv_giasp);
        this.ln_sizeM=itemView.findViewById(R.id.ln_sizeM);
        this.ln_sizeL=itemView.findViewById(R.id.ln_sizeL);
        this.img=itemView.findViewById(R.id.image);
        this.tv_sl=itemView.findViewById(R.id.tv_soluong);
        this.tv_tang_sl=itemView.findViewById(R.id.bt_tang_sl);
        this.tv_giam_sl=itemView.findViewById(R.id.bt_giam_sl);
        this.bt_thanhtoan=itemView.findViewById(R.id.bt_thanhtoan);
        this.bt_xoa=itemView.findViewById(R.id.bt_xoa);



    }
}
