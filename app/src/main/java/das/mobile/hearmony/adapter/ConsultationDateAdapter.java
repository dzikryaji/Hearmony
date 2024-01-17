package das.mobile.hearmony.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import das.mobile.hearmony.databinding.ItemConsultationDateBinding;
import das.mobile.hearmony.model.Consult;
import das.mobile.hearmony.model.Psikolog;

public class ConsultationDateAdapter extends RecyclerView.Adapter<ConsultationDateAdapter.ConsultationDateViewHolder> {

    private Context context;
    private Psikolog psikolog;
    private int checkedDay = -1;
    private List<Consult> dates;
    private Set<String> uniqueDates = new HashSet<>();
    private ConsultationHourAdapter adapter;

    // Add the 'parent' parameter to the constructor
    public ConsultationDateAdapter(Context context, Psikolog psikolog, List<Consult> date) {
        this.context = context;
        this.psikolog = psikolog;
        this.dates = date;
    }

    @NonNull
    @Override
    public ConsultationDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ConsultationDateViewHolder(ItemConsultationDateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationDateViewHolder holder, int position) {
        Consult date = dates.get(position);
        // Check if the date is unique
        if (uniqueDates.add(date.getDate())) {
            // If unique, set the text and proceed
            holder.binding.tvDate.setText(date.getDate());

            DatabaseReference hoursRef = FirebaseDatabase.getInstance().getReference()
                    .child("psikolog")
                    .child(psikolog.getId())
                    .child("schedule");

            Query query = hoursRef.orderByChild("date").equalTo(date.getDate());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Assuming a Consult model to represent each hour
                    List<Consult> hours = new ArrayList<>();

                    for (DataSnapshot hourSnapshot : snapshot.getChildren()) {
                        Consult hour = hourSnapshot.getValue(Consult.class);
                        if (hour != null) {
                            hours.add(hour);
                        }
                    }

                    Log.d("ConsultationDateAdapter", "onDataChange - Hours size: " + hours.size());

                    // Set up and bind the ConsultationHourAdapter
                    adapter = new ConsultationHourAdapter(ConsultationDateAdapter.this, holder.getLayoutPosition(), hours);
                    holder.binding.rvHour.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    holder.binding.rvHour.setAdapter(adapter);
                    holder.binding.rvHour.setHasFixedSize(true);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if needed
                }
            });

            // Set click listener for date item
            holder.binding.getRoot().setOnClickListener(view -> {
                // Update checkedDay and notify data changed
                setCheckedDay(holder.getAdapterPosition());
                notifyDataSetChanged();
            });
        } else {
            // If not unique, hide the item
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
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

    public void setCheckedHour(int position) {
    }

    public static class ConsultationDateViewHolder extends RecyclerView.ViewHolder {
        ItemConsultationDateBinding binding;

        public ConsultationDateViewHolder(@NonNull ItemConsultationDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
