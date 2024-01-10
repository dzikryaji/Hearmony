package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightCommunicationBinding;
import das.mobile.hearmony.model.Article;

public class InsightCommunicationFragment extends Fragment {

    FragmentInsightCommunicationBinding binding;
    int communicationScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicationScore = 55;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightCommunicationBinding.inflate(inflater, container, false);

        // Set Communication Score
        binding.tvProgress.setText(String.valueOf(communicationScore));
        binding.progressBar.setProgress(communicationScore);

        // Set Adapter for Recycler View
        InsightAdapter adapter = new InsightAdapter(new ArrayList<Article>());
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(adapter);

        return binding.getRoot();
    }
}