package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.databinding.ActivityMakeAppoinmentBinding;
import das.mobile.hearmony.model.Psikolog;

public class MakeAppoinmentActivity extends AppCompatActivity {

    ActivityMakeAppoinmentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeAppoinmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Psikolog psikolog = getIntent().getParcelableExtra("psikolog");
        setPsikologData(psikolog);

        binding.ivBack.setOnClickListener(view -> finish());
        binding.btnFinish.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // equal to Intent.FLAG_ACTIVITY_CLEAR_TASK which is only available from API level 11
            startActivity(intent);
        });
    }

    private void setPsikologData(Psikolog psikolog) {
        binding.tvDoctorName.setText(psikolog.getName());
        binding.category.setText(psikolog.getRoles());
        binding.officeName.setText(psikolog.getOfficeName());

        final StorageReference imgRef = FirebaseStorage.getInstance().getReference().child("/psikolog/psikolog" + psikolog.getId() + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivProfile);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }
}