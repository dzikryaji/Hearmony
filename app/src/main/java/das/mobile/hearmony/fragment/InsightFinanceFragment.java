package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import das.mobile.hearmony.databinding.FragmentInsightFinanceBinding;

public class InsightFinanceFragment extends Fragment {

    FragmentInsightFinanceBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightFinanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}