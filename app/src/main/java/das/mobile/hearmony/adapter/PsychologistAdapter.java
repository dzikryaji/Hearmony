package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.activity.DoctorProfileActivity;
import das.mobile.hearmony.databinding.ItemPsychologistBinding;

public class PsychologistAdapter extends RecyclerView.Adapter<PsychologistAdapter.PsychologistViewHolder> {
    private Context context;

    @NonNull
    @Override
    public PsychologistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new PsychologistViewHolder(ItemPsychologistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PsychologistViewHolder holder, int position) {
        holder.binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorProfileActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class PsychologistViewHolder extends RecyclerView.ViewHolder {
        ItemPsychologistBinding binding;
        public PsychologistViewHolder(@NonNull ItemPsychologistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
