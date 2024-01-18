package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import das.mobile.hearmony.activity.DoctorProfileActivity;
import das.mobile.hearmony.activity.OrderDataActivity;
import das.mobile.hearmony.databinding.ItemConsultationDateBinding;
import das.mobile.hearmony.model.Consult;

public class ConsultationDateAdapter extends RecyclerView.Adapter<ConsultationDateAdapter.ConsultationDateViewHolder> {

    private DoctorProfileActivity activity;
    private TreeMap<String, List<Consult>> dateListMap;
    private List<String> dates;
    private int checkedDay = -1;
    private int checkedHour = -1;

    // Add the 'parent' parameter to the constructor
    public ConsultationDateAdapter(DoctorProfileActivity activity, List<String> dates, TreeMap<String, List<Consult>> dateListMap) {
        this.activity = activity;
        this.dates = dates;
        this.dateListMap = dateListMap;
    }

    @NonNull
    @Override
    public ConsultationDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConsultationDateViewHolder(ItemConsultationDateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationDateViewHolder holder, int position) {
        String date = dates.get(position);
        List<Consult> consults = dateListMap.get(date);
        holder.binding.tvDate.setText(formatDateString(date));
        ConsultationHourAdapter adapter;

        // Set up and bind the ConsultationHourAdapter
        if (checkedDay == position) {
            adapter = new ConsultationHourAdapter(this, position, consults, checkedHour);
        } else {
            adapter = new ConsultationHourAdapter(this, position, consults);
        }
        holder.binding.rvHour.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        holder.binding.rvHour.setAdapter(adapter);

        if (checkedDay != -1){
            Consult consult = dateListMap.get(date).get(checkedHour);
            String price = consult.getPrice();
            activity.binding.price.setText("Consultation fee: " + OrderDataActivity.formatToRupiah(price));
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
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

    public static String formatDateString(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Handle the exception based on your needs
        }
    }

    public static class ConsultationDateViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationDateBinding binding;

        public ConsultationDateViewHolder(@NonNull ItemConsultationDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
