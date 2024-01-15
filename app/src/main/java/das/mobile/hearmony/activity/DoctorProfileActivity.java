package das.mobile.hearmony.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        binding.llMain.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        TransitionManager.beginDelayedTransition(binding.llMain, new AutoTransition());
        binding.cvDoctorProfile.setOnClickListener(view -> { accordionToggle(binding.tvDoctorProfileDescription, binding.llDoctorProfile);});
        binding.cvMedicalTreatment.setOnClickListener(view -> { accordionToggle(binding.tvMedicalTreatmentDescription, binding.llMedicalTreatment);});
        binding.cvPracticalExperience.setOnClickListener(view -> { accordionToggle(binding.tvPracticalExperienceDescription, binding.llPracticalExperience);});
        binding.cvEducationalBackground.setOnClickListener(view -> { accordionToggle(binding.tvEducationalBackgroundDescription, binding.llEducationalBackground);});

        ConsultationDateAdapter adapter = new ConsultationDateAdapter();
        binding.rvConsultationDate.setAdapter(adapter);
        binding.rvConsultationDate.setLayoutManager(new LinearLayoutManager(this));
        binding.btnMakeAppointment.setOnClickListener(view -> {
            if (adapter.getCheckedDay() > -1){
                Intent intent = new Intent(this, OrderDataActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select consultation time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void accordionToggle(View accordionItem, ViewGroup layout) {
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        TransitionManager.beginDelayedTransition(layout, new AutoTransition());

        if (accordionItem.getVisibility() == View.VISIBLE){
            accordionItem.setVisibility(View.GONE);
        } else {
            accordionItem.setVisibility(View.VISIBLE);
        }
    }
}