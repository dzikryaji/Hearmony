package das.mobile.hearmony.activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.databinding.ActivityEditProfileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance("gs://hackfest-ef21a.appspot.com");

        // Make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        binding.ivBack.setOnClickListener(view -> finish());

        // Assuming you have EditText views with IDs et_name, et_email, et_phone
        binding.save.setOnClickListener(view -> saveChanges());

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String currentName = dataSnapshot.child("name").getValue(String.class);
                        String currentEmail = dataSnapshot.child("email").getValue(String.class);
                        String currentPhone = dataSnapshot.child("phoneNum").getValue(String.class);
                        String avatarValue = dataSnapshot.child("profileUrl").getValue(String.class);

                        // Set hints based on existing user data
                        binding.etName.setHint(currentName);
                        binding.etEmail.setHint(currentEmail);
                        binding.etPhone.setHint(currentPhone);
                        setProfileImage(avatarValue);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors if any
                }
            });
        }
    }

    private void setProfileImage(String avatarValue) {
        if (avatarValue != null) {
            String imagePath;

            switch (avatarValue) {
                case "1":
                    imagePath = "/avatar/boy.png";
                    break;
                case "2":
                    imagePath = "/avatar/girl.png";
                    break;
                default:
                    imagePath = "/avatar/boy.png";
                    break;
            }

            storage.getReference().child(imagePath)
                    .getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(binding.ivPfp);
                    }).addOnFailureListener(exception -> {
                        Log.e("FirebaseStorage", "Error loading profile image: " + exception.getMessage(), exception);
                    });
        }
    }
    private void saveChanges() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get values from EditText views
            String newName = binding.etName.getText().toString().trim();
            String newPhone = binding.etPhone.getText().toString().trim();

            // Update the values in the "users" node in the Realtime Database
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            if (!newName.isEmpty()) {
                usersRef.child("name").setValue(newName);
            }

            if (!newPhone.isEmpty()) {
                usersRef.child("phoneNum").setValue(newPhone);
            }

            binding.etName.setText(newName);
            binding.etPhone.setText(newPhone);
            Toast.makeText(EditProfileActivity.this, "Data changes saved successfully.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}