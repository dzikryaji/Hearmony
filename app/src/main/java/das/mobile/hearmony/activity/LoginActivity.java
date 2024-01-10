package das.mobile.hearmony.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private boolean forgotButtonEnabled = true;
    private final Handler handler = new Handler();
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize FirebaseDatabase
        db = FirebaseDatabase.getInstance("https://hackfest-ef21a-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Disable EditText
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(binding.etEmail);
        editTexts.add(binding.etPassword);

        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updateButtonState(); // Moved the button state update to a separate method
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        binding.btnLogin.setOnClickListener(v -> signInWithEmail()); // Button to invoke the login method with email
        // Add Forgot Password functionality with time limit
        binding.forgot.setOnClickListener(v -> {
            if (forgotButtonEnabled) {
                sendPasswordResetEmail();
                forgotButtonEnabled = false;
                handler.postDelayed(() -> forgotButtonEnabled = true, 30000); // Enable button after 30 seconds
            } else {
                Toast.makeText(LoginActivity.this, "Please wait before requesting another password reset.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Fixed the Intent constructor
            startActivity(intent);
            finish();
        }); // Intent to RegisterActivity

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(LoginActivity.this, OnBoardingActivity.class);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Checks if the user is already signed in and navigates to MainActivity if true
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "You've already login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Intent to MainActivity
        }
    }

    //Handle reset password
    private void sendPasswordResetEmail() {
        String email = binding.etEmail.getText().toString().trim();

        if (!email.isEmpty()) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "An email will be sent to " + email + " if the account has been registered ",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendPasswordResetEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Failed to send password reset email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Please enter your email before requesting a password reset.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Handle email sign-in
    private void signInWithEmail() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Check if the user's email is verified
                            if (user != null && user.isEmailVerified()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Intent to MainActivity
                            } else if (user != null) {
                                // If email is not verified, send verification email
                                sendVerificationEmail(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Your e-mail or password is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
     * Method to send an email verification link to the user
     * Displays a success message upon successful sending
     * Displays an error message if sending fails
     */
    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Email verification sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(LoginActivity.this, "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
     * Method to update the state of the login button based on email and password input
     * Enables the button if both email and password are not empty
     * Updates button appearance accordingly
     */
    private void updateButtonState() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            binding.btnLogin.setEnabled(true);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_auth_button);
            binding.btnLogin.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_disabled);
            binding.btnLogin.setTextColor(Color.parseColor("#AFBACA"));
        }
    }
}