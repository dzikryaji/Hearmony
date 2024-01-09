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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityLoginBinding;
import das.mobile.hearmony.model.User;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    GoogleSignInClient mGoogleSignIn;
    FirebaseDatabase db;
    private FirebaseAuth mAuth;
    int RC_SIGN_IN = 20;
    private boolean forgotButtonEnabled = true;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignIn = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize FirebaseDatabase
        db = FirebaseDatabase.getInstance("https://hackfest-ef21a-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

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
//TODO : Apus ini?
//        binding.cvGoogle.setOnClickListener(v -> signInWithGoogle()); // Button to invoke the login method with google
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

    // Method to initiate Google Sign-In
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignIn.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Handle the result of the Google Sign-In activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(com.google.android.gms.common.api.ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (com.google.android.gms.common.api.ApiException e) {
                // Google Sign-In failed, handle the error
                Log.w(TAG, "Google sign-in failed", e);
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
     * Method to authenticate with Firebase using Google Sign-In credentials
     * Updates UI with user information if successful
     * Displays an error message if authentication fails
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Save user information to the Realtime Database
                            saveUserInfoToDatabase(user);

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*
     * Method to save user information to the Realtime Database
     * Creates a User object with user details and saves it to the database
     */
    private void saveUserInfoToDatabase(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userProfileUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

            // Create a user object
            User userInfo = new User(userId, userName, userEmail, userProfileUrl);

            // Save user information to the Realtime Database
            db.getReference().child("users").child(user.getUid()).setValue(userInfo);
        }
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