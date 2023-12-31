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

import das.mobile.hearmony.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        //Disable EditText
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(binding.etEmail);
        editTexts.add(binding.etPassword);

        for (EditText editText: editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String email = binding.etEmail.getText().toString().trim();
                    String password = binding.etPassword.getText().toString().trim();

                    if (!email.isEmpty() && !password.isEmpty()){
                        binding.btnLogin.setEnabled(true);
                        binding.btnLogin.setBackgroundResource(R.drawable.bg_auth_button);
                        binding.btnLogin.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        binding.btnLogin.setEnabled(false);
                        binding.btnLogin.setBackgroundResource(R.drawable.bg_disabled);
                        binding.btnLogin.setTextColor(Color.parseColor("#AFBACA"));
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }

        //Intent to register
        binding.tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

    }
}