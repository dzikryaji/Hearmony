package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import das.mobile.hearmony.databinding.ActivityMakeAppoinmentBinding;

public class MakeAppoinmentActivity extends AppCompatActivity {

    ActivityMakeAppoinmentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeAppoinmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
}