package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
        // Use the inflated binding.root
        return binding.getRoot();
    }
}
