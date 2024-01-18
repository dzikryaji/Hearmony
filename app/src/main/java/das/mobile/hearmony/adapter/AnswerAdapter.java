package das.mobile.hearmony.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import das.mobile.hearmony.databinding.ItemAnswerBinding;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    List<String> answerList;
    private int checkedAnswer = -1;

    public AnswerAdapter(List<String> answerList) {
        this.answerList = answerList;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnswerViewHolder(ItemAnswerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        String answer = answerList.get(position);
        holder.binding.tvAnswer.setText(answer);
        if (checkedAnswer == position) {
            holder.binding.rbAnswer.setChecked(true);
        } else {
            holder.binding.rbAnswer.setChecked(false);
        }
        int fixedPosition = position;
        holder.binding.rbAnswer.setOnClickListener(view -> {
            checkedAnswer = fixedPosition;
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder {
        private final ItemAnswerBinding binding;

        public AnswerViewHolder(@NonNull ItemAnswerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
