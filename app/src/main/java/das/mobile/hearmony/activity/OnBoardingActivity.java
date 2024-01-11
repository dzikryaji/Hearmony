package das.mobile.hearmony.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

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

import das.mobile.hearmony.adapter.OnBoardingPagerAdapter;
import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityOnBoardingBinding;
import das.mobile.hearmony.model.User;

public class OnBoardingActivity extends AppCompatActivity {

    ActivityOnBoardingBinding binding;
    int RC_SIGN_IN = 20;
    GoogleSignInClient mGoogleSignIn;
    FirebaseDatabase db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignIn = GoogleSignIn.getClient(this, gso);

        // Initialize FirebaseDatabase
        db = FirebaseDatabase.getInstance("https://hackfest-ef21a-default-rtdb.asia-southeast1.firebasedatabase.app/");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        binding.btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        binding.btnGoogle.setOnClickListener(v -> signInWithGoogle());

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

                            Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(OnBoardingActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
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

            // Create a user object
            User userInfo = new User(userId, userName, userEmail, 1, "");

            // Save user information to the Realtime Database
            db.getReference().child("users").child(user.getUid()).setValue(userInfo);
        }
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