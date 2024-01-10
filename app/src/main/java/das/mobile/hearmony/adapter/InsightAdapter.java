package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemInsightBinding;

public class InsightAdapter extends RecyclerView.Adapter<InsightAdapter.InsightViewHolder> {
    @NonNull
    @Override
    public InsightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InsightViewHolder(ItemInsightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InsightViewHolder holder, int position) {
        // TODO: Bind Data into View Holder
    }

    @Override
    public int getItemCount() {
        // TODO: Return Data Size from Database/API
        return 10;
    }

    public static class InsightViewHolder extends RecyclerView.ViewHolder {

        ItemInsightBinding binding;
        public InsightViewHolder(@NonNull ItemInsightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
