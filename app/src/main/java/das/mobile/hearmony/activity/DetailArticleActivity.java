package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.adapter.CommentAdapter;
import das.mobile.hearmony.databinding.ActivityDetailArticleBinding;
import das.mobile.hearmony.model.Article;

public class DetailArticleActivity extends AppCompatActivity {
    private ActivityDetailArticleBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    databaseReference.child("users").child(currentUser.getUid()).child("savedArticles").push().setValue(article.getTitle());
                    Toast.makeText(DetailArticleActivity.this, "Article saved", Toast.LENGTH_SHORT).show();
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

        }

        binding.ivBack.setOnClickListener(view -> {finish();});
        binding.rvComment.setLayoutManager(new LinearLayoutManager(this));
        binding.rvComment.setAdapter(new CommentAdapter());
    }
}