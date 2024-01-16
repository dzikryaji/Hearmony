package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import das.mobile.hearmony.activity.DoctorProfileActivity;
import das.mobile.hearmony.databinding.ItemPsychologistBinding;
import das.mobile.hearmony.model.Psikolog;

public class PsychologistAdapter extends RecyclerView.Adapter<PsychologistAdapter.PsychologistViewHolder> {
    private final Context context;
    private final List<Psikolog> psikologList;

    public PsychologistAdapter(Context context, List<Psikolog> psikologList) {
        this.context = context;
        this.psikologList = psikologList;
    }

    @NonNull
    @Override
    public PsychologistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PsychologistViewHolder(ItemPsychologistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PsychologistViewHolder holder, int position) {
        Psikolog psikolog = psikologList.get(position);
        holder.binding.tvName.setText(psikolog.getName());
        holder.binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorProfileActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return psikologList.size();
    }

    public class PsychologistViewHolder extends RecyclerView.ViewHolder {
        ItemPsychologistBinding binding;
        public PsychologistViewHolder(@NonNull ItemPsychologistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
