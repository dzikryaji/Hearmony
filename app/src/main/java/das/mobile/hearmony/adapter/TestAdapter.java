package das.mobile.hearmony.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import das.mobile.hearmony.activity.TestActivity;
import das.mobile.hearmony.databinding.ItemTestBinding;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    Context context;
    public TestAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestViewHolder(ItemTestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.binding.cvContainer.setOnClickListener(view -> {
            Intent intent = new Intent(context, TestActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        private final ItemTestBinding binding;

        public TestViewHolder(@NonNull ItemTestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
