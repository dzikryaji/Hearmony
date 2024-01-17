package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemInvestationBinding;

public class InvestationAdapter extends RecyclerView.Adapter<InvestationAdapter.InvestationViewHolder> {
    @NonNull
    @Override
    public InvestationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvestationViewHolder(ItemInvestationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvestationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class InvestationViewHolder extends RecyclerView.ViewHolder {
        private final ItemInvestationBinding binding;

        public InvestationViewHolder(@NonNull ItemInvestationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
