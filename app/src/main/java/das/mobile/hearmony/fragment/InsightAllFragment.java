package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import java.util.List;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightAllBinding;
import das.mobile.hearmony.model.Article;

public class InsightAllFragment extends Fragment {

    private FragmentInsightAllBinding binding;
    private InsightAdapter adapter;
    private List<Article> articleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInsightAllBinding.inflate(inflater, container, false);
        articleList = new ArrayList<>();
        setUpFirebaseRecyclerView();
        return binding.getRoot();
    }

    private void setUpFirebaseRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");
        Query query = databaseReference.orderByChild("timestamp");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateArticleList(dataSnapshot);
                setUpAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateArticleList(DataSnapshot dataSnapshot) {
        articleList.clear();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Article article = snapshot.getValue(Article.class);
            if (article != null) {
                DatabaseReference articleId = FirebaseDatabase.getInstance().getReference().child("article");
                articleId.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            final String key= ds.getKey();
                            articleId.child(key).child("id").setValue(key);
                            articleList.add(article);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("color","Error: " + error.getMessage());
                    }
                });
                articleList.add(article);
            }
        }

        Collections.reverse(articleList);
    }

    private void setUpAdapter() {
        adapter = new InsightAdapter(getContext(), articleList);
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(adapter);
    }

    public void onResume()
    {
        super.onResume();
        setUpFirebaseRecyclerView();
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
