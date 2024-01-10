package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightSexualEduBinding;

public class InsightSexualEduFragment extends Fragment {

    FragmentInsightSexualEduBinding binding;
    int sexualEduScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sexualEduScore = 20;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightSexualEduBinding.inflate(inflater, container, false);


        // Set Sexual Edu Score
        binding.tvProgress.setText(String.valueOf(sexualEduScore));
        binding.progressBar.setProgress(sexualEduScore);

        // Set Adapter for Recycler View
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(new InsightAdapter());

        return binding.getRoot();
    }
}