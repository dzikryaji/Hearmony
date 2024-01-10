// InsightAllFragment.java
package das.mobile.hearmony.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import das.mobile.hearmony.adapter.InsightAdapter;
import das.mobile.hearmony.databinding.FragmentInsightAllBinding;
import das.mobile.hearmony.model.Article;

public class InsightAllFragment extends Fragment {

    private FragmentInsightAllBinding binding;
    private InsightAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInsightAllBinding.inflate(inflater, container, false);

        // Set up Firebase RecyclerView
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("article");
        Query query = databaseReference.orderByChild("timestamp"); // Adjust the query as needed

        FirebaseRecyclerOptions<Article> options =
                new FirebaseRecyclerOptions.Builder<Article>()
                        .setQuery(query, Article.class)
                        .build();

        adapter = new InsightAdapter(options, getContext());

        // Set Adapter and layout manager into RecyclerView
        binding.rvInsight.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvInsight.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}