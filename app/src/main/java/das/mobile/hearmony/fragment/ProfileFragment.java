package das.mobile.hearmony.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.EditProfileActivity;
import das.mobile.hearmony.activity.LoginActivity;
import das.mobile.hearmony.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignIn;
    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        // Initialize mGoogleSignIn (you need to do this based on your existing setup)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignIn = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Replace "name" with the actual key under which the display name is stored
                        String displayName = dataSnapshot.child("name").getValue(String.class);
                        String phoneNum = dataSnapshot.child("phoneNum").getValue(String.class);

                        // Set the display name to the TextView
                        binding.tvName.setText(displayName);

                        // Set the phone number text
                        if (phoneNum.equals("")) {
                            binding.phone.setText("Phone number not yet added.");
                        } else {
                            binding.phone.setText(phoneNum);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors if any
                }
            });
        }

        // Set click listener for the logout button
        binding.btnLogout.setOnClickListener(v -> logout());

        binding.tvEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void logout() {
        mGoogleSignIn.revokeAccess();
        mAuth.getInstance().signOut();
        // Redirect to the login activity after logout
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}