package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import das.mobile.hearmony.adapter.ConsultationDateAdapter;
import das.mobile.hearmony.databinding.ActivityDoctorProfileBinding;

public class DoctorProfileActivity extends AppCompatActivity {

    ActivityDoctorProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(view -> {finish();});
        binding.cvDoctorProfile.setOnClickListener(view -> { accordionToggle(binding.tvDoctorProfileDescription);});
        binding.cvEducationalBackground.setOnClickListener(view -> { accordionToggle(binding.tvEducationalBackgroundDescription);});
        binding.cvMedicalTreatment.setOnClickListener(view -> { accordionToggle(binding.tvMedicalTreatmentDescription);});
        binding.cvPracticalExperience.setOnClickListener(view -> { accordionToggle(binding.tvPracticalExperienceDescription);});

        binding.rvConsultationDate.setAdapter(new ConsultationDateAdapter());
        binding.rvConsultationDate.setLayoutManager(new LinearLayoutManager(this));
        binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(this, OrderDataActivity.class);
            startActivity(intent);
        });
    }

    private void accordionToggle(View accordionItem) {
        if (accordionItem.getVisibility() == View.VISIBLE){
            accordionItem.setVisibility(View.GONE);
        } else {
            accordionItem.setVisibility(View.VISIBLE);
        }
    }
}