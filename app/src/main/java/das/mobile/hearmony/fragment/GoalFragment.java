package das.mobile.hearmony.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import das.mobile.hearmony.databinding.FragmentGoalBinding;

public class GoalFragment extends Fragment {

    FragmentGoalBinding binding;
    private static final String crypto = "https://api.coinpaprika.com/v1/tickers/btc-bitcoin";
    private static final String usStock = "nanti dulu ya key nya, ada limit soalnya";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding in onCreate
        binding = FragmentGoalBinding.inflate(getLayoutInflater());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, crypto, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Extract the desired value from the JSON response
                    JSONObject quotes = response.getJSONObject("quotes").getJSONObject("USD");
                    String resultCrypto = quotes.getString("percent_change_1y");

                    // Update the TextView with the result
                    binding.test.setText(resultCrypto);
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

        JsonArrayRequest requestA = new JsonArrayRequest(Request.Method.GET, usStock, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Assuming the array contains JSON objects
                    if (response.length() > 0) {
                        JSONObject stockData = response.getJSONObject(0);
                        String resultUsStock = stockData.getString("1Y");
                        // Update the TextView with the result
                        binding.test2.setText(resultUsStock);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response if needed
            }
        });

// Add the request to the Volley RequestQueue
        RequestQueue queue2 = Volley.newRequestQueue(requireContext());
        queue2.add(requestA);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), HomeFragment.class);
                startActivity(intent);
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use the inflated binding.root
        return binding.getRoot();
    }
}
