package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;
    private MainActivity main;

    public HomeFragment(MainActivity main) {
        this.main = main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setupView();
        return binding.getRoot();
    }

    private void setupView() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String displayName = dataSnapshot.child("name").getValue(String.class);
                    String avatarValue = dataSnapshot.child("profilePict").getValue(String.class);
                    binding.tvUserName.setText(displayName);
                    setProfileImage(avatarValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        setOnClickListenerForFragment(new ProfileFragment(), R.id.nav_profile, binding.setAvatar);
        setOnClickListenerForFragment(new ChatConsultationFragment(), R.id.nav_chat, binding.seeConsultChat);
        setOnClickListenerForFragment(new GoalFragment(), R.id.nav_goal, binding.seeGoalRecommendation);
        setOnClickListenerForFragment(new InsightFragment(), R.id.nav_insight, binding.seeInsight);
        setOnClickListenerForFragment(new ChatConsultationFragment(), R.id.nav_chat, binding.buttonConsultChat);

    }

    private void setOnClickListenerForFragment(Fragment fragment, int menuItemId, View view) {
        view.setOnClickListener(v -> {
            MenuItem item = main.binding.bottomNav.getMenu().findItem(menuItemId);
            item.setChecked(true);
            main.setCurrentFragmentWithBackStack(fragment);
        });
    }

    private void setProfileImage(String avatarValue) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child("/avatar/" + avatarValue + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.setAvatar);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }
}
