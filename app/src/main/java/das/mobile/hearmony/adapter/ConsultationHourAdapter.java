package das.mobile.hearmony.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import das.mobile.hearmony.databinding.ItemConsultationHourBinding;
import das.mobile.hearmony.model.Consult;

public class ConsultationHourAdapter extends RecyclerView.Adapter<ConsultationHourAdapter.ConsultationHourViewHolder> {

    private final int dayPosition;
    private final ConsultationDateAdapter parentAdapter;
    private int checkedHour = -1;
    private List<Consult> consults;

    public ConsultationHourAdapter(ConsultationDateAdapter parentAdapter, int dayPosition, List<Consult> consults) {
        this.parentAdapter = parentAdapter;
        this.dayPosition = dayPosition;
        Collections.sort(consults, new HourComparator());
        this.consults = consults;
    }

    public ConsultationHourAdapter(ConsultationDateAdapter parentAdapter, int dayPosition, List<Consult> consults, int checkedHour) {
        this(parentAdapter, dayPosition, consults);
        this.checkedHour = checkedHour;
    }

    @NonNull
    @Override
    public ConsultationHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConsultationHourViewHolder(ItemConsultationHourBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationHourViewHolder holder, int position) {
        Consult hour = consults.get(position);
        holder.binding.rgHour.setText(formatHour(hour.getHour()));
        if (checkedHour == position) {
            holder.binding.rgHour.setChecked(true);
            holder.binding.rgHour.setTextColor(Color.parseColor("#57CC99"));
        } else {
            holder.binding.rgHour.setChecked(false);
            holder.binding.rgHour.setTextColor(Color.parseColor("#1A66A0"));
        }
        holder.binding.rgHour.setOnClickListener(view -> {
            parentAdapter.setCheckedDay(dayPosition);
            parentAdapter.setCheckedHour(position);
            parentAdapter.notifyDataSetChanged();
        });
    }

    public static String formatHour(String hour) {
        return hour + ":00 - " + (Integer.parseInt(hour) + 1) + ":00";
    }

    @Override
    public int getItemCount() {
        return consults.size();
    }

    public static class ConsultationHourViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationHourBinding binding;

        public ConsultationHourViewHolder(@NonNull ItemConsultationHourBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private static class HourComparator implements Comparator<Consult> {
        @Override
        public int compare(Consult consult1, Consult consult2) {
            return Integer.compare(Integer.parseInt(consult1.getHour()), Integer.parseInt(consult2.getHour()));
        }
    }
}
