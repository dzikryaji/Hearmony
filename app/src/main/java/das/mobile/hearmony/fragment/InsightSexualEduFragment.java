package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import das.mobile.hearmony.databinding.FragmentInsightSexualEduBinding;

public class InsightSexualEduFragment extends Fragment {

    FragmentInsightSexualEduBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightSexualEduBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}