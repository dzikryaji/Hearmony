package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import das.mobile.hearmony.adapter.CommentAdapter;
import das.mobile.hearmony.databinding.ActivityDetailArticleBinding;
import das.mobile.hearmony.model.Article;
import das.mobile.hearmony.model.Comment;

public class DetailArticleActivity extends AppCompatActivity {
    private ActivityDetailArticleBinding binding;
    private FirebaseAuth mAuth;
    private Article article;
    private CommentAdapter adapter;
    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        commentList = new ArrayList<>();

        // Initialize Time Format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String time = sdf.format(new Date());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the Article object from the intent
        article = getIntent().getParcelableExtra("article");

        // Set the data to the views
        if (article != null) {
            setArticleData(article);
            setBookmarkClickListener(article);
            setThumbnailImage(article);
            setUpComment();

            binding.sendComment.setOnClickListener(view -> addComment(article, time));

            binding.ivBack.setOnClickListener(view -> {
                finish();
                setResult(RESULT_OK);
            });
        }
    }

    private void setArticleData(Article article) {
        binding.tvTitle.setText(article.getTitle());
        binding.tvCategory.setText(article.getCategory());
        binding.tvMain.setText(Html.fromHtml(article.getContent()), TextView.BufferType.SPANNABLE);
        binding.tvBadgeCategory.setText(article.getCategory());
        binding.tvTimestamp.setText(article.getTimestamp());
        binding.tvAuthor.setText(article.getAuthor());
    }

    private void setBookmarkClickListener(Article article) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference savedArticlesRef = FirebaseDatabase.getInstance()
                    .getReference().child("users").child(currentUser.getUid()).child("savedArticles");

            binding.ivBookmark.setOnClickListener(view -> toggleBookmark(savedArticlesRef, article.getTitle()));
        }
    }

    private void toggleBookmark(DatabaseReference savedArticlesRef, String articleTitle) {
        savedArticlesRef.orderByValue().equalTo(articleTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        childSnapshot.getRef().removeValue();
                    }
                    Toast.makeText(DetailArticleActivity.this, "Article unsaved", Toast.LENGTH_SHORT).show();
                } else {
                    // Article title does not exist, save it
                    savedArticlesRef.push().setValue(articleTitle);
                    Toast.makeText(DetailArticleActivity.this, "Article saved", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailArticleActivity", "Database error: " + databaseError.getMessage());
                Toast.makeText(DetailArticleActivity.this, "Error checking saved articles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setThumbnailImage(Article article) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child("/thumbnails/article-" + article.getId() + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivThumbnail);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }

    private void addComment(Article article, String time) {
        String comment = binding.tvComment.getText().toString().trim();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (!comment.isEmpty() && currentUser != null) {
            String articleTitle = article.getTitle();
            DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("article");

            articlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                            String title = articleSnapshot.child("title").getValue(String.class);
                            if (articleTitle.equals(title)) {
                                DatabaseReference commentsRef = articleSnapshot.getRef().child("comments").push();
                                commentsRef.child("key").setValue(commentsRef.getKey());
                                commentsRef.child("comment").setValue(comment);
                                commentsRef.child("userId").setValue(currentUser.getUid());
                                commentsRef.child("timestamp").setValue(time);
                                binding.tvComment.setText("");
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(DetailArticleActivity.this, "Article not found: " + articleTitle, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DetailArticleActivity", "Database error: " + databaseError.getMessage());
                    Toast.makeText(DetailArticleActivity.this, "Error adding comment", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Handle the case where the comment is empty
            Toast.makeText(DetailArticleActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }


    private void setUpComment() {
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("article").child(article.getId()).child("comments");
        Log.i("Comment List : Set Up Comment", commentList.toString());
        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    commentList.clear();
                    Log.i("Comment List : Set Up Comment onDataChange", commentList.toString());

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.i("Comment Snapshot", snapshot.toString());
                        Comment comment = snapshot.getValue(Comment.class);
                        Log.i("Comment List : Set Up Comment onDataChange Loop", commentList.toString());
                        if (comment != null) {
                            commentList.add(comment);
                            Log.i("Comment List : Set Up Comment onDataChange Add to List", commentList.toString());
                        }
                    }
                    Collections.reverse(commentList);
                        binding.tvCommentCount.setText(commentList.size() + " Comments");
                    setUpAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setUpAdapter() {
        adapter = new CommentAdapter(commentList);
        binding.rvComment.setAdapter(adapter);
        binding.rvComment.setLayoutManager(new LinearLayoutManager(this));
    }


}
