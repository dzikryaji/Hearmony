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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightAllBinding;
import das.mobile.hearmony.model.Article;

public class InsightAllFragment extends Fragment {

    FragmentInsightAllBinding binding;
    private ArrayList<Article> allArticlesList = new ArrayList<>();
    private InsightAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsightAllBinding.inflate(inflater, container, false);

        //Set Adapter and layout manager into recyclerview
        adapter = new InsightAdapter(new ArrayList<Article>());
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(adapter);

        // Assuming you have initialized the database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allArticlesList.clear(); // Clear the list before adding new data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                        Article article = articleSnapshot.getValue(Article.class);
                        allArticlesList.add(article);
                    }
                }

                adapter.setArticleList(allArticlesList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        return binding.getRoot();
    }
}
