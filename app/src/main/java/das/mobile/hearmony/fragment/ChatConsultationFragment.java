package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.PsychologistAdapter;
import das.mobile.hearmony.databinding.FragmentChatConsultationBinding;

public class ChatConsultationFragment extends Fragment {

    FragmentChatConsultationBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatConsultationBinding.inflate(inflater, container, false);
        binding.rvPsychologist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPsychologist.setAdapter(new PsychologistAdapter());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}