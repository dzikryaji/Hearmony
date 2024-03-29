package das.mobile.hearmony.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
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

import java.util.ArrayList;

import das.mobile.hearmony.databinding.ActivityEditAvatarBinding;

public class EditAvatarActivity extends AppCompatActivity {

    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private ActivityEditAvatarBinding binding;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAvatarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get current user information
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("profilePict");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String avatarValue = dataSnapshot.getValue(String.class);
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    final StorageReference imgRef = mStorageRef.child("/avatar/" + avatarValue + ".jpg");

                    imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.existingavatar);
                    }).addOnFailureListener(exception -> {
                        Log.d("error===========", exception.getMessage());
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        // Add Radio Button into array
        radioButtons.add(binding.rbAvatar1);
        radioButtons.add(binding.rbAvatar2);
        radioButtons.add(binding.rbAvatar3);
        radioButtons.add(binding.rbAvatar4);
        radioButtons.add(binding.rbAvatar5);
        radioButtons.add(binding.rbAvatar6);
        radioButtons.add(binding.rbAvatar7);

        // Set listeners for each radio button
        for (RadioButton radio : radioButtons) {
            radio.setOnClickListener(view -> checkRadioButton(radio));
        }

        binding.ivBack.setOnClickListener(view -> finish());
        binding.savebutton.setOnClickListener(view -> {
            if (selectedIndex == -1) {
                Toast.makeText(this, "Please Select Avatar", Toast.LENGTH_SHORT).show();
            } else {
                // Update Firebase Realtime Database with the selected index
                usersRef.setValue(String.valueOf(selectedIndex));
                finish();
            }
        });
    }

    private void checkRadioButton(RadioButton clickedRadioButton) {
        for (RadioButton radio : radioButtons) {
            radio.setChecked(false);
        }
        clickedRadioButton.setChecked(true);

        // Get the index of the clicked radio button
        selectedIndex = radioButtons.indexOf(clickedRadioButton) + 1;
    }
}
