package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightAllBinding;

public class InsightAllFragment extends Fragment {

    FragmentInsightAllBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightAllBinding.inflate(inflater, container, false);
        // Set Adapter for Recycler View
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(new InsightAdapter());
        return binding.getRoot();
    }
}