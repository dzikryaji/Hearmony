package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.databinding.ActivityDetailArticleBinding;
import das.mobile.hearmony.model.Article;

public class DetailArticleActivity extends AppCompatActivity {
    private ActivityDetailArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imgRef = mStorageRef.child("/thumbnails/article-" + article.getTitle() + ".jpg");
            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivThumbnail);
            }).addOnFailureListener(exception -> {
                Log.d("error===========", exception.getMessage());
            });

        }

        binding.ivBack.setOnClickListener(view -> {finish();});
    }
}
