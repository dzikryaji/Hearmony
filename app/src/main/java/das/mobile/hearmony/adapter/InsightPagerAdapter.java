package das.mobile.hearmony.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import das.mobile.hearmony.fragment.InsightAllFragment;
import das.mobile.hearmony.fragment.InsightCommunicationFragment;
import das.mobile.hearmony.fragment.InsightFinanceFragment;
import das.mobile.hearmony.fragment.InsightSexualEduFragment;

public class InsightPagerAdapter extends FragmentStateAdapter {
    public InsightPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InsightAllFragment();
            case 1:
                return new InsightCommunicationFragment();
            case 2:
                return new InsightSexualEduFragment();
            case 3:
                return new InsightFinanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
