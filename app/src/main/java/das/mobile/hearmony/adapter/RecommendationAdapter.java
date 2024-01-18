package das.mobile.hearmony.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import das.mobile.hearmony.databinding.ItemRecommendationsBinding;
import das.mobile.hearmony.model.Recommendation;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    private final Context context;
    private List<Recommendation> recommendation;


    public RecommendationAdapter(Context context, List<Recommendation> recommendation) {
        this.context = context;
        this.recommendation = recommendation;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendationViewHolder(ItemRecommendationsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        if (position < recommendation.size()) {
            Recommendation recommendations = recommendation.get(position);
            holder.binding.title.setText(recommendations.getTitle());
            holder.binding.subCategory.setText(recommendations.getSubcategory());
            final StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("/recommendation/recommendation" + recommendations.getId() + ".jpg");

            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Picasso.get().load(uri)
                        .into(holder.binding.thumbnail);
            }).addOnFailureListener(exception -> {
                Log.d("error===========", exception.getMessage());
            });
        }
    }

    @Override
    public int getItemCount() {
        return recommendation.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecommendationsBinding binding;

        public RecommendationViewHolder(@NonNull ItemRecommendationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
