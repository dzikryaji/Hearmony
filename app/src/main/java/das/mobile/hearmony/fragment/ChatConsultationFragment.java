package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import das.mobile.hearmony.adapter.PsychologistAdapter;
import das.mobile.hearmony.databinding.FragmentChatConsultationBinding;
import das.mobile.hearmony.model.Psikolog;

public class ChatConsultationFragment extends Fragment {

    FragmentChatConsultationBinding binding;
    PsychologistAdapter adapter;
    ArrayList<Psikolog> psychologistList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatConsultationBinding.inflate(inflater, container, false);
        psychologistList = new ArrayList<>();
        setUpFirebaseRecyclerView();
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void setUpFirebaseRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("psikolog");
        Query query = databaseReference.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updatePsychologistList(dataSnapshot);
                setUpAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updatePsychologistList(DataSnapshot dataSnapshot) {
        psychologistList.clear();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Psikolog psikolog = snapshot.getValue(Psikolog.class);
            if (psikolog != null) {
                psychologistList.add(psikolog);
            }
        }

        Collections.reverse(psychologistList);
    }


    private void setUpAdapter() {
        adapter = new PsychologistAdapter(getActivity(), psychologistList);
        binding.rvPsychologist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPsychologist.setAdapter(adapter);
    }
}