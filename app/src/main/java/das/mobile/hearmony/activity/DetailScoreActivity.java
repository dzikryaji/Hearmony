package das.mobile.hearmony.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;

import das.mobile.hearmony.adapter.ScorePagerAdapter;
import das.mobile.hearmony.databinding.ActivityDetailScoreBinding;

public class DetailScoreActivity extends AppCompatActivity {

    ActivityDetailScoreBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ScorePagerAdapter adapter = new ScorePagerAdapter(this);
        binding.viewpager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Communication");
                    break;
                case 1:
                    tab.setText("Sexual Edu");
                    break;
                case 2:
                    tab.setText("Finance");
                    break;
            }
        }).attach();

        binding.ivBack.setOnClickListener(view -> finish());


    }
}