package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import das.mobile.hearmony.databinding.ActivityOrderDataBinding;

public class OrderDataActivity extends AppCompatActivity {

    ActivityOrderDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(view -> finish());
        binding.btnMakeAppointment.setOnClickListener(view -> {
            Intent intent = new Intent(this, MakeAppoinmentActivity.class);
            startActivity(intent);
        });
    }
}