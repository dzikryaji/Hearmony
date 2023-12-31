package das.mobile.hearmony;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.databinding.ActivityOnBoardingBinding;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        List<Integer> imgList = new ArrayList<>();
        List<String> txtList = new ArrayList<>();

        imgList.add(R.drawable.img_onboarding1);
        imgList.add(R.drawable.img_onboarding2);
        imgList.add(R.drawable.img_onboarding3);

        txtList.add("Family Harmony Enrichment");
        txtList.add("Goal-oriented Relationship Planner");
        txtList.add("Let's Strengthen Our Marital Bonds!");

        OnBoardingPagerAdapter adapter = new OnBoardingPagerAdapter(imgList, txtList);
        binding.viewpager.setAdapter(adapter);
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.i("Current", String.valueOf(binding.viewpager.getCurrentItem()));
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                changeColor(binding.viewpager.getCurrentItem());
            }
        });

    }

    private void changeColor(int currentItem) {
        if (currentItem == 0) {
            binding.ivIndicator1.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_green));
            binding.ivIndicator2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator3.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        } else if (currentItem == 1) {
            binding.ivIndicator1.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator2.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_green));
            binding.ivIndicator3.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        } else if (currentItem == 2) {
            binding.ivIndicator1.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator3.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_green));
        } else {
            binding.ivIndicator1.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator2.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
            binding.ivIndicator3.setBackgroundColor(ContextCompat.getColor(this, R.color.gray));
        }
    }

}