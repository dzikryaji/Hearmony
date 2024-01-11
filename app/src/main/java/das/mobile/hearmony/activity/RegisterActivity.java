package das.mobile.hearmony.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityRegisterBinding;
import das.mobile.hearmony.model.User;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize FirebaseDatabase
        db = FirebaseDatabase.getInstance("https://hackfest-ef21a-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // Disable EditText
        Map<String, EditText> editTexts = new HashMap<>();
        editTexts.put("name", binding.etName);
        editTexts.put("email", binding.etEmail);
        editTexts.put("password", binding.etPassword);
        editTexts.put("confirmPassword", binding.etConfirmPassword);

        for (EditText editText : editTexts.values()) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updateButtonState(); // Moved the button state update to a separate method
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Unused
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Unused
                }
            });
        }

        // Intent to login
        binding.tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnRegister.setOnClickListener(v -> registerUser()); // Button to invoke the registration method with email

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RegisterActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private Map<String, String> getInputValues() {
        Map<String, String> inputValues = new HashMap<>();
        inputValues.put("name", binding.etName.getText().toString().trim());
        inputValues.put("email", binding.etEmail.getText().toString().trim());
        inputValues.put("password", binding.etPassword.getText().toString().trim());
        inputValues.put("confirmPassword", binding.etConfirmPassword.getText().toString().trim());
        return inputValues;
    }

    // Handle email registration
    private void registerUser() {
        Map<String, String> values = getInputValues();
        String name = values.get("name");
        String email = values.get("email");
        String password = values.get("password");
        String confirmPassword = values.get("confirmPassword");

        // Conditions
        if (!password.matches("^(?=.*[A-Z]).{8,12}$")) {
            // Password does not meet requirements
            Toast.makeText(RegisterActivity.this, "Password must contain 8-12 characters, including 1 capital letter.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            // Passwords do not match, show a warning
            Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success
                        Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                        // Save user information to the Realtime Database
                        saveUserInfoToDatabase(mAuth.getCurrentUser());

                        // Sign out the user to prevent automatic sign-in
                        mAuth.signOut();

                        // Proceed to the login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If registration fails, display a message to the user.
                        if (task.getException() != null) {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("email address is already in use")) {
                                // Email already exists
                                Toast.makeText(RegisterActivity.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                            } else {
                                // Other registration failure
                                Toast.makeText(RegisterActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // Save user information to the Realtime Database
    private void saveUserInfoToDatabase(FirebaseUser user) {
        if (user != null) {
            Map<String, String> values = getInputValues();
            String userId = user.getUid();
            String userName = values.get("name");
            String userEmail = user.getEmail();

            // Create a user object
            User userInfo = new User(userId, userName, userEmail, 1, "");

            // Save user information to the Realtime Database
            db.getReference().child("users").child(user.getUid()).setValue(userInfo);
        }
    }

    private void updateButtonState() {
        Map<String, String> values = getInputValues();
        String name = values.get("name");
        String email = values.get("email");
        String password = values.get("password");
        String confirmPassword = values.get("confirmPassword");

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            binding.btnRegister.setEnabled(true);
            binding.btnRegister.setBackgroundResource(R.drawable.bg_auth_button);
            binding.btnRegister.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            binding.btnRegister.setEnabled(false);
            binding.btnRegister.setBackgroundResource(R.drawable.bg_disabled);
            binding.btnRegister.setTextColor(Color.parseColor("#AFBACA"));
        }
    }
}