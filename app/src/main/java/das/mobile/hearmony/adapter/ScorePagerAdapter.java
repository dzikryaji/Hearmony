package das.mobile.hearmony.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import das.mobile.hearmony.fragment.ScoreCommunicationFragment;
import das.mobile.hearmony.fragment.ScoreFinanceFragment;
import das.mobile.hearmony.fragment.ScoreSexualEduFragment;

public class ScorePagerAdapter extends FragmentStateAdapter {
    public ScorePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ScoreCommunicationFragment();
            case 1:
                return new ScoreSexualEduFragment();
            case 2:
                return new ScoreFinanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
