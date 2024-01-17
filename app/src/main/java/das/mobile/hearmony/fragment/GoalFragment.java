package das.mobile.hearmony.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import das.mobile.hearmony.R;
import das.mobile.hearmony.adapter.InvestationAdapter;
import das.mobile.hearmony.adapter.RecommendationAdapter;
import das.mobile.hearmony.R;
import das.mobile.hearmony.activity.MainActivity;
import das.mobile.hearmony.databinding.FragmentGoalBinding;

public class GoalFragment extends Fragment {

    FragmentGoalBinding binding;
    private static final String url = "https://script.google.com/macros/s/AKfycbxm4L6aiPmPOPTEWlHckau7rEqNq-UgutE7ISB1_NcJt3gWmTHriWjY4xvwo0WxtNNuLA/exec?spreadsheetUrl=https://docs.google.com/spreadsheets/d/1Jigqj3JiKoUaP8ZYEUNG_Gltgs1chBUN5JMtIsY6P5I/edit?usp=sharing";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding in onCreate
        binding = FragmentGoalBinding.inflate(getLayoutInflater());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the desired value from the JSON response
                    JSONObject quotes = response.getJSONObject("data");
                    String aapl = quotes.getString("aapl");
                    String bitcoin = quotes.getString("bitcoin now");

                    // Update the TextView with the result
                    binding.test.setText(aapl);
                    binding.test2.setText(bitcoin);
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
            binding.llResult.setVisibility(View.VISIBLE);
        });

        binding.rvInvestation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvRecommendation.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvInvestation.setAdapter(new InvestationAdapter());
        binding.rvRecommendation.setAdapter(new RecommendationAdapter());

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
                    String formatted = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format((parsed/100));

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

        // Use the inflated binding.root
        return binding.getRoot();
    }


    private void setSpinner() {
        List<String> items = new ArrayList<>();
        items.add("Vacation");
        items.add("Properties");
        items.add("Desired Items");
        items.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, items);

        // Specify the custom layout for the dropdown view
        adapter.setDropDownViewResource(R.layout.spinner);

        // Apply the adapter to the spinner
        binding.spinnerGoalsType.setAdapter(adapter);
    }
}
