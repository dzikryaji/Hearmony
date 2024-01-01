package das.mobile.hearmony;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Disable EditText
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(binding.etName);
        editTexts.add(binding.etEmail);
        editTexts.add(binding.etPassword);
        editTexts.add(binding.etConfirmPassword);

        for (EditText editText: editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String name = binding.etName.getText().toString().trim();
                    String email = binding.etEmail.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();
                    String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setBackgroundResource(R.drawable.bg_auth_button);
                        binding.btnRegister.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        binding.btnRegister.setEnabled(false);
                        binding.btnRegister.setBackgroundResource(R.drawable.bg_disabled);
                        binding.btnRegister.setTextColor(Color.parseColor("#AFBACA"));
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }

        //Intent to register
        binding.tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}