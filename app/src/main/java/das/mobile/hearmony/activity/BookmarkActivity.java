package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
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
    private List<String> articleIdList;
    private FirebaseAuth mAuth;
    private static final int DETAIL_ARTICLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        articleList = new ArrayList<>();
        articleIdList = new ArrayList<>();
        setUpFirebaseRecyclerView();
        binding.ivBack.setOnClickListener(view -> {
            finish();
        });
    }

    // Modify setUpFirebaseRecyclerView method
    private void setUpFirebaseRecyclerView() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("savedArticles");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateArticleIdList(dataSnapshot);  // Pass the DataSnapshot to updateArticleIdList
                updateArticleList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateArticleIdList(DataSnapshot dataSnapshot) {
        articleIdList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String articleId = snapshot.getValue(String.class);
            if (articleId != null) {
                articleIdList.add(articleId);
            }
        }
    }

    private void updateArticleList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    Log.i("Article Id List", articleIdList.toString());
                    Log.i("Article id", article.getTitle());

                    // Check if the article is in the saved list
                    if (articleIdList.contains(article.getTitle())) {
                        Log.i("Tag", "Masuk");
                        articleList.add(article);
                        Log.i("Tag", articleList.toString());
                    }
                }

                Collections.reverse(articleList);
                setUpAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
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

    @Override
    public void onRestart()
    {
        super.onRestart();
        setUpFirebaseRecyclerView();
    }
}
