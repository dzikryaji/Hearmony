package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightFinanceBinding;

public class InsightFinanceFragment extends Fragment {

    FragmentInsightFinanceBinding binding;
    int financeScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        financeScore = 75;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightFinanceBinding.inflate(inflater, container, false);

        // Set Finance Score
        binding.tvProgress.setText(String.valueOf(financeScore));
        binding.progressBar.setProgress(financeScore);

        // Set Adapter for Recycler View
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(new InsightAdapter());

        return binding.getRoot();
    }
}