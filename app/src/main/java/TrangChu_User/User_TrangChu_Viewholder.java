package TrangChu_User;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_1.R;

public class User_TrangChu_Viewholder extends RecyclerView.ViewHolder
{
    TextView tv_idsp,tv_tensp,tv_giasp;
    LinearLayout ln_sizeM,ln_sizeL;
    ImageView img;
    Button  bt_mua;
    public User_TrangChu_Viewholder(@NonNull View itemView)
    {
        super(itemView);
        this.tv_idsp=itemView.findViewById(R.id.tv_idsp);
        this.tv_tensp=itemView.findViewById(R.id.tv_tensp);
        this.tv_giasp=itemView.findViewById(R.id.tv_giasp);
        this.ln_sizeM=itemView.findViewById(R.id.ln_sizeM);
        this.ln_sizeL=itemView.findViewById(R.id.ln_sizeL);
        this.img=itemView.findViewById(R.id.image);
        this.bt_mua=itemView.findViewById(R.id.bt_mua);

    }
}
