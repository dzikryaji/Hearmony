package das.mobile.hearmony.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityMainBinding;
import das.mobile.hearmony.fragment.ChatFragment;
import das.mobile.hearmony.fragment.GoalFragment;
import das.mobile.hearmony.fragment.HomeFragment;
import das.mobile.hearmony.fragment.InsightFragment;
import das.mobile.hearmony.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        HomeFragment homeFragment = new HomeFragment();
        InsightFragment insightFragment = new InsightFragment();
        GoalFragment goalFragment = new GoalFragment();
        ChatFragment chatFragment = new ChatFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    setCurrentFragment(homeFragment);
                } else if (item.getItemId() == R.id.nav_insight) {
                    setCurrentFragment(insightFragment);
                } else if (item.getItemId() == R.id.nav_goal) {
                    setCurrentFragment(goalFragment);
                } else if (item.getItemId() == R.id.nav_chat) {
                    setCurrentFragment(chatFragment);
                } else if (item.getItemId() == R.id.nav_profile) {
                    setCurrentFragment(profileFragment);
                }
                return true;
            }
        });

        setCurrentFragment(homeFragment);
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, fragment).commit();
    }
}
