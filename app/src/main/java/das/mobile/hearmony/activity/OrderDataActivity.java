package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import das.mobile.hearmony.adapter.ConsultationDateAdapter;
import das.mobile.hearmony.adapter.ConsultationHourAdapter;
import das.mobile.hearmony.databinding.ActivityOrderDataBinding;
import das.mobile.hearmony.model.Psikolog;

public class OrderDataActivity extends AppCompatActivity {

    ActivityOrderDataBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Psikolog psikolog = getIntent().getParcelableExtra("psikolog");
        if (psikolog != null) {
            setPsikologData(psikolog);
        } else {
            Log.e("OrderDataActivity", "Psikolog object is null");
        }


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentName = dataSnapshot.child("name").getValue(String.class);
                    String currentEmail = dataSnapshot.child("email").getValue(String.class);
                    String currentPhone = dataSnapshot.child("phoneNum").getValue(String.class);

                    // Set hints based on existing user data
                    binding.tvOrdererName.setText(currentName);
                    binding.tvOrdererEmail.setText(currentEmail);
                    binding.tvOrdererPhoneNumber.setText(currentPhone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });
    }

    private void setPsikologData(Psikolog psikolog) {
        String selectedDate = getIntent().getStringExtra("selectedDate");
        String selectedHour = getIntent().getStringExtra("selectedHour");
        String selectedPrice = getIntent().getStringExtra("selectedPrice");

        binding.selectedDate.setText(ConsultationDateAdapter.formatDateString(selectedDate));
        binding.selectedHour.setText(ConsultationHourAdapter.formatHour(selectedHour));
        binding.price.setText("Consultation fee: " + formatToRupiah(selectedPrice));

        binding.ivBack.setOnClickListener(view -> finish());
        binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(this, MakeAppoinmentActivity.class);
            intent.putExtra("psikolog", psikolog);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("selectedHour", selectedHour);
            startActivity(intent);
        });

        binding.tvDoctorName.setText(psikolog.getName());
        binding.tvCategory.setText(psikolog.getRoles());
        binding.officeName.setText(psikolog.getOfficeName());

        final StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("/psikolog/psikolog" + psikolog.getId() + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivProfile);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
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

    public static String formatToRupiah(String price) {
        double amount = Double.parseDouble(price);
        NumberFormat formatter = DecimalFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }
}