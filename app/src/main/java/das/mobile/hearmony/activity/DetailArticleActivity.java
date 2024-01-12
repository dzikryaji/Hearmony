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
import java.util.Date;
import java.util.TimeZone;

import das.mobile.hearmony.adapter.CommentAdapter;
import das.mobile.hearmony.databinding.ActivityDetailArticleBinding;
import das.mobile.hearmony.model.Article;

public class DetailArticleActivity extends AppCompatActivity {
    private ActivityDetailArticleBinding binding;
    private FirebaseAuth mAuth;
    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new CommentAdapter();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String time = sdf.format(new Date());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the Article object from the intent
        Article article = getIntent().getParcelableExtra("article");

        Log.i("MAIN", article.getContent());

        // Set the data to the views
        if (article != null) {
            binding.tvTitle.setText(article.getTitle());
            binding.tvCategory.setText(article.getCategory());
            binding.tvMain.setText(Html.fromHtml(article.getContent()), TextView.BufferType.SPANNABLE);
            binding.tvBadgeCategory.setText(article.getCategory());
            binding.tvTimestamp.setText(article.getTimestamp());
            binding.tvAuthor.setText(article.getAuthor());
            binding.ivBookmark.setOnClickListener(view -> {
                if (currentUser != null) {
                    DatabaseReference savedArticlesRef = databaseReference.child("users").child(currentUser.getUid()).child("savedArticles");
                    savedArticlesRef.orderByValue().equalTo(article.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    childSnapshot.getRef().removeValue();
                                }
                                Toast.makeText(DetailArticleActivity.this, "Article unsaved", Toast.LENGTH_SHORT).show();
                            } else {
                                // Article title does not exist, save it
                                savedArticlesRef.push().setValue(article.getTitle());
                                Toast.makeText(DetailArticleActivity.this, "Article saved", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("DetailArticleActivity", "Database error: " + databaseError.getMessage());
                            Toast.makeText(DetailArticleActivity.this, "Error checking saved articles", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("DetailArticleActivity", "User not authenticated");
                }
            });

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imgRef = mStorageRef.child("/thumbnails/article-" + article.getTitle() + ".jpg");
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivThumbnail);
            }).addOnFailureListener(exception -> {
                Log.d("error===========", exception.getMessage());
            });

            binding.sendComment.setOnClickListener(view -> {
                String comment = binding.tvComment.getText().toString().trim();
                if (!comment.isEmpty()) {
                    String articleTitle = article.getTitle();
                    DatabaseReference articlesRef = databaseReference.child("article");
                    articlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
                                    DatabaseReference commentsRef = articleSnapshot.getRef().child("comments").push();
                                    commentsRef.child("key").setValue(commentsRef.getKey());
                                    commentsRef.child("comment").setValue(comment);
                                    commentsRef.child("name").setValue(currentUser.getUid());
                                    commentsRef.child("timestamp").setValue(time);
                                    Toast.makeText(DetailArticleActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
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
            });
        }

        binding.ivBack.setOnClickListener(view -> {
            finish();
            setResult(RESULT_OK);
        });
        binding.rvComment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvComment.setAdapter(adapter);
    }
}
