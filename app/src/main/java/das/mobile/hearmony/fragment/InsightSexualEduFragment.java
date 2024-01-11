package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
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

import das.mobile.hearmony.R;
import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightSexualEduBinding;
import das.mobile.hearmony.model.Article;

public class InsightSexualEduFragment extends Fragment {

    private FragmentInsightSexualEduBinding binding;
    private InsightAdapter adapter;
    private List<Article> articleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInsightSexualEduBinding.inflate(inflater, container, false);
        articleList = new ArrayList<>();
        setUpFirebaseRecyclerView();
        setUpProgressScore(10);
        return binding.getRoot();
    }

    private void setUpFirebaseRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");
        Query query = databaseReference.orderByChild("category").equalTo("Sexual Edu");

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

    private void setUpProgressScore(int score){
        if (score < 50){
            binding.tvProgress.setBackgroundResource(R.drawable.bg_score_red);
            binding.progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.bg_progressbar_red));
//            binding.tvScoreDescription.setText("");
        } else if (score <= 75) {
            binding.tvProgress.setBackgroundResource(R.drawable.bg_score_yellow);
            binding.progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.bg_progressbar_yellow));
//            binding.tvScoreDescription.setText("");
        } else {
            binding.tvProgress.setBackgroundResource(R.drawable.bg_score_green);
            binding.progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.bg_progressbar_green));
//            binding.tvScoreDescription.setText("");
        }
        binding.progressBar.setProgress(score);
        binding.tvProgress.setText(String.valueOf(score));
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