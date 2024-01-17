package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    ArrayList<Psikolog> filteredList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatConsultationBinding.inflate(inflater, container, false);
        psychologistList = new ArrayList<>();
        filteredList = new ArrayList<>();
        setUpFirebaseRecyclerView();

        // Set up the TextWatcher for the search EditText
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the data based on the search text
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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
        setUpAdapter();
        filterData(binding.search.getText().toString());
    }


    private void filterData(String searchText) {
        filteredList.clear();

        for (Psikolog psikolog : psychologistList) {
            // Filter logic based on the name or any other attribute
            if (psikolog.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(psikolog);
            }
        }

        // Update the adapter with the filtered data
        adapter.setFilteredList(filteredList);
    }

    private void setUpAdapter() {
        adapter = new PsychologistAdapter(getActivity(), psychologistList);
        binding.rvPsychologist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPsychologist.setAdapter(adapter);
    }
}
