package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import das.mobile.hearmony.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.ivBack.setOnClickListener(view -> {
            finish();
        });

        binding.cvEditAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditAvatarActivity.class);
            startActivity(intent);
        });
    }
}