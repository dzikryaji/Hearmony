package das.mobile.hearmony.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemConsultationHourBinding;

public class ConsultationHourAdapter extends RecyclerView.Adapter<ConsultationHourAdapter.ConsultationHourViewHolder> {

    private final int dayPosition;
    private final ConsultationDateAdapter parentAdapter;
    private int checkedHour = -1;

    public ConsultationHourAdapter(ConsultationDateAdapter parentAdapter, int dayPosition) {
        this.parentAdapter = parentAdapter;
        this.dayPosition = dayPosition;
    }
    public ConsultationHourAdapter(ConsultationDateAdapter parentAdapter, int dayPosition, int checkedHour) {
        this.parentAdapter = parentAdapter;
        this.dayPosition = dayPosition;
        this.checkedHour = checkedHour;
    }

    @NonNull
    @Override
    public ConsultationHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConsultationHourViewHolder(ItemConsultationHourBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationHourViewHolder holder, int position) {
        if (checkedHour == position){
            holder.binding.rgHour.setChecked(true);
            holder.binding.rgHour.setTextColor(Color.parseColor("#57CC99"));
        }else {
            holder.binding.rgHour.setChecked(false);
            holder.binding.rgHour.setTextColor(Color.parseColor("#1A66A0"));
        }
        holder.binding.rgHour.setOnClickListener(view -> {
            parentAdapter.setCheckedDay(dayPosition);
            parentAdapter.setCheckedHour(position);
            parentAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class ConsultationHourViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationHourBinding binding;
        public ConsultationHourViewHolder(@NonNull ItemConsultationHourBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
