package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import das.mobile.hearmony.activity.DetailArticleActivity;
import das.mobile.hearmony.databinding.ItemInsightBinding;
import das.mobile.hearmony.model.Article;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightViewHolder> {

    private final Context context;
    private final List<Article> articles;

    public InsightAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public InsightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInsightBinding binding = ItemInsightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InsightViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InsightViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.bind(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class InsightViewHolder extends RecyclerView.ViewHolder {

        private final ItemInsightBinding binding;

        public InsightViewHolder(@NonNull ItemInsightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Article article) {
            // Clear existing image to handle view recycling
            binding.ivArticle.setImageDrawable(null);

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imgRef = mStorageRef.child("/thumbnails/article-" + article.getId() + ".jpg");

            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivArticle);
            }).addOnFailureListener(exception -> {
                Log.d("error===========", exception.getMessage());
            });

            binding.tvTitle.setText(article.getTitle());

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormat.parse(article.getTimestamp());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM/dd/yyyy");
            String formattedTimeStamp = outputFormat.format(date);

            binding.tvDate.setText(formattedTimeStamp);

            binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailArticleActivity.class);
                intent.putExtra("article", article); // Pass the actual Article object
                context.startActivity(intent);
            });

        }
    }
}