package das.mobile.hearmony.fragment;

import android.content.Intent;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.DetailArticleActivity;
import das.mobile.hearmony.activity.DoctorProfileActivity;
import das.mobile.hearmony.activity.DetailScoreActivity;
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.activity.OrderDataActivity;
import das.mobile.hearmony.databinding.FragmentHomeBinding;
import das.mobile.hearmony.model.Article;
import das.mobile.hearmony.model.Consult;
import das.mobile.hearmony.model.Psikolog;

public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FragmentHomeBinding binding;
    private MainActivity main;
    private DatabaseReference priceRef;


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
        setupViewPsikolog();
        setUpViewInsight();
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

    private void setupViewPsikolog() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("psikolog");
        Query query = databaseReference.orderByChild("name").limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Psikolog psikolog = snapshot.getValue(Psikolog.class);

                    if (psikolog != null) {
                        binding.tvName.setText(psikolog.getName());
                        binding.category.setText(psikolog.getRoles());
                        binding.officeName.setText(psikolog.getOfficeName());
                        binding.officeLocation.setText(psikolog.getOfficeLocation());
                        binding.btnMakeAppointment.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), DoctorProfileActivity.class);
                            intent.putExtra("psikolog", psikolog);
                            startActivity(intent);
                        });

                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        final StorageReference imgRef = mStorageRef.child("/psikolog/psikolog" + psikolog.getId() + ".jpg");

                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivProfile);
                        }).addOnFailureListener(exception -> {
                            Log.d("error===========", exception.getMessage());
                        });

                        priceRef = FirebaseDatabase.getInstance().getReference().child("psikolog").child(psikolog.getId()).child("schedule");
                        Query query = priceRef.orderByChild("price").limitToFirst(1);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Consult cheapestConsult = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Consult consult = snapshot.getValue(Consult.class);

                                    if (cheapestConsult == null || (consult != null && Integer.parseInt(consult.getPrice()) < Integer.parseInt(cheapestConsult.getPrice()))) {
                                        cheapestConsult = consult;
                                    }
                                }

                                if (cheapestConsult != null && cheapestConsult.getPrice() != null) {
                                    binding.price.setText(OrderDataActivity.formatToRupiah(cheapestConsult.getPrice()));
                                } else {
                                    binding.price.setText("No schedule available");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle error if needed
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void setUpViewInsight () {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");
        Query query = databaseReference.orderByChild("timestamp").limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);

                    if (article != null) {
                        binding.insightTitle.setText(article.getTitle());
                        binding.insightCategory.setText(article.getCategory());
                        binding.insightTimestamt.setText(article.getTimestamp());
                        binding.insightLayout.setOnClickListener(view -> {
                            Intent intent = new Intent(getActivity(), DetailArticleActivity.class);
                            intent.putExtra("article", article);
                            startActivity(intent);
                        });

                        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                        final StorageReference imgRef = mStorageRef.child("/thumbnails/article-" + article.getId() + ".jpg");

                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.insightThumbnail);
                        }).addOnFailureListener(exception -> {
                            Log.d("error===========", exception.getMessage());
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
        binding.tvSeeMoreScore.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), DetailScoreActivity.class);
            startActivity(intent);
        });

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
