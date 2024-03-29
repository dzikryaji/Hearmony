package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
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


        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
        binding.cvEditAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(EditProfileActivity.this, EditAvatarActivity.class);
            startActivity(intent);
        });

        // Assuming you have EditText views with IDs et_name, et_email, et_phone
        binding.save.setOnClickListener(view -> saveChanges());

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            getUserInformation(currentUser.getUid());
        }
    }

    private void getUserInformation(String uid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String currentName = dataSnapshot.child("name").getValue(String.class);
                    String currentEmail = dataSnapshot.child("email").getValue(String.class);
                    String currentPhone = dataSnapshot.child("phoneNum").getValue(String.class);
                    String avatarValue = dataSnapshot.child("profilePict").getValue(String.class);

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

    private void setProfileImage(String avatarValue) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child("/avatar/" + avatarValue + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivPfp);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
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
            Toast.makeText(getApplicationContext(), "Data changes saved successfully.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        getUserInformation(currentUser.getUid());
    }
}