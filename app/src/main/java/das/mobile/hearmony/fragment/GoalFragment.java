package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.adapter.InvestationAdapter;
import das.mobile.hearmony.adapter.RecommendationAdapter;
import das.mobile.hearmony.databinding.FragmentGoalBinding;
import das.mobile.hearmony.model.Recommendation;

public class GoalFragment extends Fragment {

    FragmentGoalBinding binding;
    ArrayList<Recommendation> recommendationList;
    ArrayList<String> investList;
    private static final String url = "https://script.google.com/macros/s/AKfycbxm4L6aiPmPOPTEWlHckau7rEqNq-UgutE7ISB1_NcJt3gWmTHriWjY4xvwo0WxtNNuLA/exec?spreadsheetUrl=https://docs.google.com/spreadsheets/d/1Jigqj3JiKoUaP8ZYEUNG_Gltgs1chBUN5JMtIsY6P5I/edit?usp=sharing";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentGoalBinding.inflate(getLayoutInflater());

        investList = new ArrayList<>();
        recommendationList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the desired value from the JSON response
                    JSONObject quotes = response.getJSONObject("data");
                    List<Map.Entry<String, Double>> investDataList = new ArrayList<>();

                    // Iterate through the JSON object and add entries to the investDataList
                    for (Iterator<String> it = quotes.keys(); it.hasNext(); ) {
                        String key = it.next();
                        Double value = quotes.getDouble(key);
                        investDataList.add(new AbstractMap.SimpleEntry<>(key, value));
                    }

                    // Now, you have the data in investDataList
                    investList.clear(); // Clear the existing investList
                    for (Map.Entry<String, Double> entry : investDataList) {
                        // Display the asset name and return as a formatted string
                        String formattedEntry = entry.getKey() + ": " + entry.getValue();
                        investList.add(formattedEntry);
                    }

                    // After updating the investList, set up the RecyclerView for investments
                    setUpRVInvest();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response if needed
            }
        });

        // Add the request to the Volley RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);

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
        binding = FragmentGoalBinding.inflate(getLayoutInflater());

        binding.btnSearch.setOnClickListener(view -> {
            String savingTenor = binding.etSavingTenor.getText().toString();
            String goalsName = binding.etGoalsName.getText().toString();
            String targetFunds = binding.etTargetFunds.getText().toString();
            if (savingTenor.isEmpty() || goalsName.isEmpty() || targetFunds.isEmpty()) {
                Toast.makeText(getActivity(), "Please Fill All Necessary Input", Toast.LENGTH_SHORT).show();
            } else {
                setUpFirebaseRecyclerView();
                binding.llResult.setVisibility(View.VISIBLE);
            }
        });

        binding.etTargetFunds.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!charSequence.toString().equals(current)) {
                    binding.etTargetFunds.removeTextChangedListener(this);
                    String cleanString = charSequence.toString().replaceAll("[Rp.,]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format((parsed / 100));

                    current = formatted;
                    binding.etTargetFunds.setText(formatted);
                    binding.etTargetFunds.setSelection(formatted.length());
                    binding.etTargetFunds.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        setSpinner();
        return binding.getRoot();
    }

    private void setUpFirebaseRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("recommendation");
        Query query = databaseReference.orderByChild("category").equalTo(binding.spinnerGoalsType.getSelectedItem().toString());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateRecommendationList(dataSnapshot);
                setUpRVInvest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }

    private void updateRecommendationList(DataSnapshot dataSnapshot) {
        recommendationList.clear();
        String targetFunds = binding.etTargetFunds.getText().toString();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Recommendation recommendation = snapshot.getValue(Recommendation.class);
            if (recommendation != null && Long.parseLong(recommendation.getPrice()) <= (Long.parseLong(targetFunds.replaceAll("[^\\d]", "").replaceAll("\\d{2}$", "")))) {
                recommendationList.add(recommendation);
            }
        }

        Collections.reverse(recommendationList);
        setUpRVRecommendation();
    }

    private void setUpRVRecommendation() {
        RecommendationAdapter adapter = new RecommendationAdapter(getActivity(), recommendationList);
        binding.rvRecommendation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvRecommendation.setAdapter(adapter);
    }

    private void setUpRVInvest() {
        if (!isAdded()) {
            // Fragment is not attached to the activity
            return;
        }

        String savingTenor = binding.etSavingTenor.getText().toString();
        String targetFunds = binding.etTargetFunds.getText().toString();

        if (savingTenor.isEmpty() || targetFunds.isEmpty()) {
            // Show a Toast warning if either field is empty
            Toast.makeText(requireContext(), "Please fill in both Saving Tenor and Target Funds", Toast.LENGTH_SHORT).show();
            return; // Do not proceed further if any of the fields is empty
        }

        InvestationAdapter adapter = new InvestationAdapter(getActivity(), investList, savingTenor, targetFunds);
        binding.rvInvestation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvInvestation.setAdapter(adapter);
    }


    private void setSpinner() {
        List<String> items = new ArrayList<>();
        items.add("Vacation");
        items.add("Residence");
        items.add("Laptop");
        items.add("Mobile Device");
        items.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, items);

        // Specify the custom layout for the dropdown view
        adapter.setDropDownViewResource(R.layout.spinner);

        // Apply the adapter to the spinner
        binding.spinnerGoalsType.setAdapter(adapter);
    }
}
