package das.mobile.hearmony.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import das.mobile.hearmony.R;
import das.mobile.hearmony.databinding.ActivityEditAvatarBinding;

public class EditAvatarActivity extends AppCompatActivity {

    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private ActivityEditAvatarBinding binding;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAvatarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get current user information
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize Firebase database reference
        usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("avatar");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int avatarValue = dataSnapshot.getValue(Integer.class);
                    switch (avatarValue) {
                        case 1:
                            binding.existingavatar.setImageResource(R.drawable.img_avatar1);
                            break;
                        case 2:
                            binding.existingavatar.setImageResource(R.drawable.img_avatar2);
                            break;
                        default:
                            binding.existingavatar.setImageResource(R.drawable.img_avatar3);
                            break;
                    }
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
        radioButtons.add(binding.rbAvatar8);

        // Set listeners for each radio button
        for (RadioButton radio : radioButtons) {
            radio.setOnClickListener(view -> checkRadioButton(radio));
        }

        binding.ivBack.setOnClickListener(view -> finish());
        binding.savebutton.setOnClickListener(view -> {
            Intent intent = new Intent(EditAvatarActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    private void checkRadioButton(RadioButton clickedRadioButton) {
        for (RadioButton radio : radioButtons) {
            radio.setChecked(false);
        }
        clickedRadioButton.setChecked(true);

        // Get the index of the clicked radio button
        int selectedIndex = radioButtons.indexOf(clickedRadioButton) + 1;

        // Update Firebase Realtime Database with the selected index
        usersRef.setValue(selectedIndex);
    }
}
