package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemRecommendationsBinding;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {
    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendationViewHolder(ItemRecommendationsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecommendationsBinding binding;

        public RecommendationViewHolder(@NonNull ItemRecommendationsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
