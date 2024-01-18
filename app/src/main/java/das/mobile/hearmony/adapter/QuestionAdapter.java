package das.mobile.hearmony.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.databinding.ItemQuestionBinding;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    Context context;
    List<String> questionList;

    public QuestionAdapter(Context context, List<String> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        String question = questionList.get(position);
        holder.binding.tvNumber.setText((position + 1)+ ".");
        holder.binding.tvQuestion.setText(question);

        List<String> answerList = new ArrayList<>();
        answerList.add("Very Rarely");
        answerList.add("Rarely");
        answerList.add("Often");
        answerList.add("Very Often");

        holder.binding.rvAnswer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.binding.rvAnswer.setAdapter(new AnswerAdapter(answerList));
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuestionBinding binding;

        public QuestionViewHolder(@NonNull ItemQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
