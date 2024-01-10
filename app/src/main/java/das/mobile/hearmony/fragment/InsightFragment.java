package das.mobile.hearmony.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;

import das.mobile.hearmony.adapter.InsightPagerAdapter;
import das.mobile.hearmony.databinding.FragmentInsightBinding;

public class InsightFragment extends Fragment {

    FragmentInsightBinding binding;
    int progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = 55;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), HomeFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

        //set progressbar
        binding.tvProgress.setText(String.valueOf(progress));
        binding.progressBar.setProgress(progress);

        return binding.getRoot();
    }
}