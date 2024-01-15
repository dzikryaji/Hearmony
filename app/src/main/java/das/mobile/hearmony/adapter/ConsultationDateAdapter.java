package das.mobile.hearmony.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.databinding.ItemConsultationDateBinding;

public class ConsultationDateAdapter extends RecyclerView.Adapter<ConsultationDateAdapter.ConsultationDateViewHolder> {

    private Context context;
    private int checkedDay = -1;
    private int checkedHour = -1;

    @NonNull
    @Override
    public ConsultationDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ConsultationDateViewHolder(ItemConsultationDateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationDateViewHolder holder, int position) {
        ConsultationHourAdapter hourAdapter;
        if (checkedDay == position){
            hourAdapter = new ConsultationHourAdapter(this, position, checkedHour);
        } else {
            hourAdapter = new ConsultationHourAdapter(this, position);
        }
        holder.binding.rvHour.setAdapter(hourAdapter);
        holder.binding.rvHour.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public int getCheckedDay() {
        return checkedDay;
    }

    public void setCheckedDay(int checkedDay) {
        this.checkedDay = checkedDay;
    }

    public int getCheckedHour() {
        return checkedHour;
    }

    public void setCheckedHour(int checkedHour) {
        this.checkedHour = checkedHour;
    }

    public static class ConsultationDateViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationDateBinding binding;

        public ConsultationDateViewHolder(@NonNull ItemConsultationDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
