package das.mobile.hearmony.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.BookmarkActivity;
import das.mobile.hearmony.activity.EditProfileActivity;
import das.mobile.hearmony.activity.LoginActivity;
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment  {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignIn;
    private FragmentProfileBinding binding;
    private FirebaseStorage storage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Initialize mGoogleSignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignIn = GoogleSignIn.getClient(getActivity(), gso);

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance("gs://hackfest-ef21a.appspot.com");

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back press event here
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    // Use the existing bottom navigation logic to switch to the HomeFragment
                    MenuItem item = mainActivity.binding.bottomNav.getMenu().findItem(R.id.nav_home);
                    item.setChecked(true);
                    mainActivity.setCurrentFragment(new HomeFragment(mainActivity));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }


    private void setupView() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String displayName = dataSnapshot.child("name").getValue(String.class);
                        String phoneNum = dataSnapshot.child("phoneNum").getValue(String.class);
                        String avatarValue = dataSnapshot.child("profilePict").getValue(String.class);

                        binding.tvName.setText(displayName);

                        if (phoneNum.equals("")) {
                            binding.phone.setText("Phone number not yet added.");
                        } else {
                            binding.phone.setText(phoneNum);
                        }

                        setProfileImage(avatarValue);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors if any
                }
            });
        }

        binding.btnLogout.setOnClickListener(v -> logout());

        binding.tvEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        binding.llBookmarks.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), BookmarkActivity.class);
            startActivity(intent);
        });
    }

    private void setProfileImage(String avatarValue) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child("/avatar/" + avatarValue + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.profilepict);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }

    private void logout() {
        mGoogleSignIn.revokeAccess();
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}