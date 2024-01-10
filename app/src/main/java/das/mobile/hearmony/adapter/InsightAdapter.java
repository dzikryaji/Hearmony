package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import das.mobile.hearmony.activity.DetailArticleActivity;
import das.mobile.hearmony.databinding.ItemInsightBinding;
import das.mobile.hearmony.model.Article;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightViewHolder> {
    Context context;
    List<Article> articleList;

    public InsightAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public InsightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new InsightViewHolder(ItemInsightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InsightViewHolder holder, int position) {
        Article article = articleList.get(position);
//
//        Picasso.get().load(article.getThumbnailPath()).into(holder.binding.ivArticle);
//        holder.binding.tvTitle.setText(article.getTitle());
//
//        // Parse the input date string
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = inputFormat.parse(article.getTimestamp());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // Format the date according to the desired output format
//        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM/dd/yyyy");
//        String formattedTimeStamp = outputFormat.format(date);
//
//        holder.binding.tvDate.setText(article.getTimestamp());

        holder.binding.getRoot().setOnClickListener(view ->{
            Intent intent = new Intent(context, DetailArticleActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class InsightViewHolder extends RecyclerView.ViewHolder {

        ItemInsightBinding binding;
        public InsightViewHolder(@NonNull ItemInsightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
