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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.adapter.ConsultationDateAdapter;
import das.mobile.hearmony.databinding.ActivityDoctorProfileBinding;
import das.mobile.hearmony.model.Psikolog;

public class DoctorProfileActivity extends AppCompatActivity {

    ActivityDoctorProfileBinding binding;
    private Psikolog psikolog;

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

        psikolog = getIntent().getParcelableExtra("psikolog");

        if (psikolog != null) {
            setPsikologData(psikolog);
            setProfileImage(psikolog);
            binding.ivBack.setOnClickListener(view -> {
                finish();
                setResult(RESULT_OK);
            });
        }
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

        if (accordionItem.getVisibility() == View.VISIBLE){
            accordionItem.setVisibility(View.GONE);
        } else {
            accordionItem.setVisibility(View.VISIBLE);
        }
    }
}