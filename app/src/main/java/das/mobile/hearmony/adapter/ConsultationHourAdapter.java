package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemConsultationHourBinding;

public class ConsultationHourAdapter extends RecyclerView.Adapter<ConsultationHourAdapter.ConsultationHourViewHolder> {

    @NonNull
    @Override
    public ConsultationHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConsultationHourViewHolder(ItemConsultationHourBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationHourViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ConsultationHourViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationHourBinding binding;

        public ConsultationHourViewHolder(@NonNull ItemConsultationHourBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
