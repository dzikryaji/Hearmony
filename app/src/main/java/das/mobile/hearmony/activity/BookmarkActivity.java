package das.mobile.hearmony.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.ActivityBookmarkBinding;
import das.mobile.hearmony.model.Article;

public class BookmarkActivity extends AppCompatActivity {
    private ActivityBookmarkBinding binding;
    private InsightAdapter adapter;
    private List<Article> articleList;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        articleList = new ArrayList<>();
        setUpFirebaseRecyclerView();
        binding.ivBack.setOnClickListener(view -> {
            finish();
        });

    }


    private void setUpFirebaseRecyclerView() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("savedArticles");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        adapter = new InsightAdapter(this, articleList);
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(this));
        binding.rvInsight.setAdapter(adapter);
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