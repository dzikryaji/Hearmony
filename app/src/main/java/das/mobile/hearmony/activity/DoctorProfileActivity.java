package das.mobile.hearmony.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import das.mobile.hearmony.adapter.ConsultationDateAdapter;
import das.mobile.hearmony.databinding.ActivityDoctorProfileBinding;
import das.mobile.hearmony.model.Consult;
import das.mobile.hearmony.model.Psikolog;

public class DoctorProfileActivity extends AppCompatActivity {

    public ActivityDoctorProfileBinding binding;
    private Psikolog psikolog;
    private List<Consult> dateList = new ArrayList<>();
    private DatabaseReference dateIdRef;
    private ConsultationDateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        psikolog = getIntent().getParcelableExtra("psikolog");
        setUpFirebaseRecycleView();

        binding.ivBack.setOnClickListener(view -> { finish(); });
        binding.llMain.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        TransitionManager.beginDelayedTransition(binding.llMain, new AutoTransition());
        binding.cvDoctorProfile.setOnClickListener(view -> { accordionToggle(binding.tvDoctorProfileDescription, binding.llDoctorProfile); });
        binding.cvMedicalTreatment.setOnClickListener(view -> { accordionToggle(binding.tvMedicalTreatmentDescription, binding.llMedicalTreatment); });
        binding.cvPracticalExperience.setOnClickListener(view -> { accordionToggle(binding.tvPracticalExperienceDescription, binding.llPracticalExperience); });
        binding.cvEducationalBackground.setOnClickListener(view -> { accordionToggle(binding.tvEducationalBackgroundDescription, binding.llEducationalBackground); });

        if (psikolog != null) {
            setPsikologData(psikolog);
            setProfileImage(psikolog);
            binding.ivBack.setOnClickListener(view -> {
                finish();
                setResult(RESULT_OK);
            });
        }
    }

    private void setUpFirebaseRecycleView() {
        dateIdRef = FirebaseDatabase.getInstance().getReference().child("psikolog").child(psikolog.getId()).child("schedule");
        Query query = dateIdRef.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateDateList(dataSnapshot);
                setUpAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void setUpAdapter() {
        TreeMap<String, List<Consult>> dateListMap = groupConsultByDate(dateList);
        List<String> dates = new ArrayList<>(dateListMap.keySet());
        adapter = new ConsultationDateAdapter(this, dates, dateListMap);
        binding.rvConsultationDate.setLayoutManager(new LinearLayoutManager(this));
        binding.rvConsultationDate.setAdapter(adapter);

        binding.btnMakeAppointment.setOnClickListener(view -> {
            if (adapter.getCheckedDay() > -1) {
                int checkedDay = adapter.getCheckedDay();
                int checkedHour = adapter.getCheckedHour();
                String date = dates.get(checkedDay);
                Consult consult = dateListMap.get(date).get(checkedHour);

                // Assuming getPrice() is a method in the Consult class to retrieve the price
                String price = consult.getPrice();
                binding.price.setText("Consultation fee: " + OrderDataActivity.formatToRupiah(price));

                Intent intent = new Intent(this, OrderDataActivity.class);
                intent.putExtra("psikolog", psikolog);
                intent.putExtra("selectedDate", date);
                intent.putExtra("selectedHour", consult.getHour());
                intent.putExtra("selectedPrice", price);
                this.startActivity(intent);
            } else {
                Toast.makeText(this, "Please select consultation time", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setPsikologData(Psikolog psikolog) {
        binding.tvName.setText(psikolog.getName());
        binding.roles.setText(psikolog.getRoles());
        binding.officeName.setText(psikolog.getOfficeName());
        binding.officeLocation.setText(psikolog.getOfficeLocation());
        binding.tvDoctorProfileDescription.setText(psikolog.getProfileDescription());
        binding.tvMedicalTreatmentDescription.setText(psikolog.getTreatment());
        binding.tvPracticalExperienceDescription.setText(psikolog.getExperience());
        binding.tvEducationalBackgroundDescription.setText(psikolog.getEducation());

    }

    private void setProfileImage(Psikolog psikolog) {
        final StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("/psikolog/psikolog" + psikolog.getId() + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivProfile);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }

    private void accordionToggle(View accordionItem, ViewGroup layout) {
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        TransitionManager.beginDelayedTransition(layout, new AutoTransition());

        if (accordionItem.getVisibility() == View.VISIBLE) {
            accordionItem.setVisibility(View.GONE);
        } else {
            accordionItem.setVisibility(View.VISIBLE);
        }
    }

    private void updateDateList(DataSnapshot dataSnapshot) {
        dateList.clear();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Consult date = snapshot.getValue(Consult.class);
            if (date != null) {
                dateList.add(date);
            }
        }
        Collections.reverse(dateList);
    }

    private TreeMap<String, List<Consult>> groupConsultByDate(List<Consult> consults) {
        TreeMap<String, List<Consult>> groupedConsults = new TreeMap<>(Comparator.reverseOrder());

        for (Consult consult : consults) {
            String date = consult.getDate();

            //Check if the date is in the past
            if (isDateInPast(date)){
                continue;
            }

            // Check if the date is already a key in the map
            if (groupedConsults.containsKey(date)) {
                // If yes, add the consult to the existing list
                groupedConsults.get(date).add(consult);
            } else {
                // If no, create a new list with the consult and put it in the map
                List<Consult> consultList = new ArrayList<>();
                consultList.add(consult);
                groupedConsults.put(date, consultList);
            }
        }

        return groupedConsults;
    }

    private boolean isDateInPast(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            Date consultDate = dateFormat.parse(dateStr);
            return consultDate.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception based on your needs
            return false; // Assume the date is not in the past if there's an error
        }
    }
}
