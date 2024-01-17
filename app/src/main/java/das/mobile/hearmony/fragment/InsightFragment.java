package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.adapter.InsightPagerAdapter;
import das.mobile.hearmony.databinding.FragmentInsightBinding;

public class InsightFragment extends Fragment {

    FragmentInsightBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back press event here
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    // Use the existing bottom navigation logic to switch to the HomeFragment
                    MenuItem item = mainActivity.binding.bottomNav.getMenu().findItem(R.id.nav_home);
                    item.setChecked(true);
                    mainActivity.setCurrentFragment(new HomeFragment(mainActivity));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightBinding.inflate(inflater, container, false);
        InsightPagerAdapter adapter = new InsightPagerAdapter(requireActivity());
        binding.viewpager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("All");
                    break;
                case 1:
                    tab.setText("Communication");
                    break;
                case 2:
                    tab.setText("Sexual Edu");
                    break;
                case 3:
                    tab.setText("Finance");
                    break;
            }
        }).attach();

        return binding.getRoot();
    }
}