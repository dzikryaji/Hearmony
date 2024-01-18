package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.activity.DoctorProfileActivity;
import das.mobile.hearmony.activity.OrderDataActivity;
import das.mobile.hearmony.databinding.ItemPsychologistBinding;
import das.mobile.hearmony.model.Consult;
import das.mobile.hearmony.model.Psikolog;

public class PsychologistAdapter extends RecyclerView.Adapter<PsychologistAdapter.PsychologistViewHolder> {
    private final Context context;
    private List<Psikolog> psikologList;
    private DatabaseReference priceRef;

    public PsychologistAdapter(Context context, List<Psikolog> psikologList) {
        this.context = context;
        this.psikologList = psikologList;
    }

    @NonNull
    @Override
    public PsychologistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PsychologistViewHolder(ItemPsychologistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void setFilteredList(ArrayList<Psikolog> filteredList) {
        this.psikologList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull PsychologistViewHolder holder, int position) {

        Psikolog psikolog = psikologList.get(position);
        final StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("/psikolog/psikolog" + psikolog.getId() + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri)
                    .into(holder.binding.ivProfile);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
        holder.binding.tvName.setText(psikolog.getName());
        holder.binding.category.setText(psikolog.getRoles());
        holder.binding.officeName.setText(psikolog.getOfficeName());
        holder.binding.officeLocation.setText(psikolog.getOfficeLocation());
        holder.binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorProfileActivity.class);
            intent.putExtra("psikolog", psikolog);
            context.startActivity(intent);
        });

        priceRef = FirebaseDatabase.getInstance().getReference().child("psikolog").child(psikolog.getId()).child("schedule");
        Query query = priceRef.orderByChild("price").limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consult cheapestConsult = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Consult consult = snapshot.getValue(Consult.class);

                    if (cheapestConsult == null || (consult != null && Integer.parseInt(consult.getPrice()) < Integer.parseInt(cheapestConsult.getPrice()))) {
                        cheapestConsult = consult;
                    }
                }

                if (cheapestConsult != null && cheapestConsult.getPrice() != null) {
                    holder.binding.price.setText(OrderDataActivity.formatToRupiah(cheapestConsult.getPrice()));
                } else {
                    holder.binding.price.setText("No schedule available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
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
