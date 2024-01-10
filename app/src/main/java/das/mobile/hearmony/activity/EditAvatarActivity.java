package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import das.mobile.hearmony.databinding.ActivityEditAvatarBinding;

public class EditAvatarActivity extends AppCompatActivity {

    ArrayList<RadioButton> radioButtons = new ArrayList<>();
    ActivityEditAvatarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAvatarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Add Radio Button into array
        radioButtons.add(binding.rbAvatar1);
        radioButtons.add(binding.rbAvatar2);
        radioButtons.add(binding.rbAvatar3);
        radioButtons.add(binding.rbAvatar4);
        radioButtons.add(binding.rbAvatar5);
        radioButtons.add(binding.rbAvatar6);
        radioButtons.add(binding.rbAvatar7);
        radioButtons.add(binding.rbAvatar8);

        for (RadioButton radios: radioButtons) {
            radios.setOnClickListener(view -> {checkRadioButton(radios);});
        }
    }

    private void checkRadioButton(RadioButton view) {
        for (RadioButton radios: radioButtons) {
            radios.setChecked(false);
        }
        view.setChecked(true);
    }


}