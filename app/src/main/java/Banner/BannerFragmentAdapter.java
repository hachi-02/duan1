package Banner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class BannerFragmentAdapter extends FragmentStateAdapter {
    private List<Integer> imageResIds;

    public BannerFragmentAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> imageResIds) {
        super(fragmentActivity);
        this.imageResIds = imageResIds;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return BannerFragment.newInstance(imageResIds.get(position));
    }

    @Override
    public int getItemCount() {
        return imageResIds.size();
    }
}
