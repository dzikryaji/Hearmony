package das.mobile.hearmony.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import das.mobile.hearmony.adapter.QuestionAdapter;
import das.mobile.hearmony.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.ivBack.setOnClickListener(view -> finish());
        binding.btnSubmit.setOnClickListener(view -> finish());

        List<String> questionList = new ArrayList<>();
        questionList.add("How often do you and your partner talk openly and honestly with each other?");
        questionList.add("How often do you and your partner talk about each other's feelings and thoughts?");
        questionList.add("How often do you and your partner talk about long-term plans and goals in your life together?");
        questionList.add("How often do you and your partner express appreciation and respect for each other?");
        questionList.add("How often do you and your partner fight over trivial communication issues?");
        questionList.add("How often do you and your partner raise concerns or problems in this relationship?");
        questionList.add("How often do you and your partner talk about how to improve the quality of this relationship?");
        questionList.add("How often do you and your partner share about your own issues?");
        questionList.add("How often you and your partner talk about past problems?");
        questionList.add("How often do you and your partner talk in irritation and anger?");

        binding.rvQuestion.setLayoutManager(new LinearLayoutManager(this));
        binding.rvQuestion.setAdapter(new QuestionAdapter(this, questionList));
    }
}