// InsightAdapter.java
package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import das.mobile.hearmony.activity.DetailArticleActivity;
import das.mobile.hearmony.databinding.ItemInsightBinding;
import das.mobile.hearmony.model.Article;

public class InsightAdapter extends FirebaseRecyclerAdapter<Article, InsightAdapter.InsightViewHolder> {

    private final Context context;

    public InsightAdapter(@NonNull FirebaseRecyclerOptions<Article> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull InsightViewHolder holder, int position, @NonNull Article article) {
        holder.bind(article);
    }

    @NonNull
    @Override
    public InsightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInsightBinding binding = ItemInsightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InsightViewHolder(binding);
    }

    // Add this method to update options
    public void updateOptions(FirebaseRecyclerOptions<Article> options) {
        this.updateOptions(options);
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

            Picasso.get().load(article.getThumbnailPath()).into(binding.ivArticle);
            binding.tvTitle.setText(article.getTitle());

            // Parse the input date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormat.parse(article.getTimestamp());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Format the date according to the desired output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM/dd/yyyy");
            String formattedTimeStamp = outputFormat.format(date);

            binding.tvDate.setText(formattedTimeStamp);

            binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailArticleActivity.class);
                context.startActivity(intent);
            });
        }
    }
}
