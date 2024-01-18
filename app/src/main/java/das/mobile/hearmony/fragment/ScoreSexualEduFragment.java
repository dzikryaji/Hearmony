package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.TestAdapter;
import das.mobile.hearmony.databinding.FragmentScoreSexualEduBinding;

public class ScoreSexualEduFragment extends Fragment {

    FragmentScoreSexualEduBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScoreSexualEduBinding.inflate(inflater, container, false);

        binding.rvTest.setAdapter(new TestAdapter(getActivity()));
        binding.rvTest.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}