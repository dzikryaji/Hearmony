package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityMainBinding;
import das.mobile.hearmony.fragment.ChatConsultationFragment;
import das.mobile.hearmony.fragment.GoalFragment;
import das.mobile.hearmony.fragment.HomeFragment;
import das.mobile.hearmony.fragment.InsightFragment;
import das.mobile.hearmony.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        HomeFragment homeFragment = new HomeFragment(this);
        InsightFragment insightFragment = new InsightFragment();
        GoalFragment goalFragment = new GoalFragment();
        ChatConsultationFragment chatConsultationFragment = new ChatConsultationFragment();
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
                    setCurrentFragment(chatConsultationFragment);
                } else if (item.getItemId() == R.id.nav_profile) {
                    setCurrentFragment(profileFragment);
                }
                return true;
            }
        });
        setCurrentFragment(homeFragment);
    }

    public int setCurrentFragment(Fragment fragment) {
        return getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, fragment).commit();
    }

    public void setCurrentFragmentWithBackStack(Fragment fragment) {
        if (fragment != currentFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment, fragment)
                    .addToBackStack(null) // Add this line to enable back button functionality
                    .commit();
            currentFragment = fragment;
        }
    }
}
