package zientek.lukasz.learnwords.learn_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import zientek.lukasz.learnwords.R;
import zientek.lukasz.learnwords.model.Pair;

public class LearnScreen extends AppCompatActivity
{

    private LearnViewModel viewModel;
    private LearnAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_screen);

        viewModel = ViewModelProviders.of(this).get(LearnViewModel.class);
        setupRecyclerView();
        subscribeWords();

        String mFileName = getIntent().getStringExtra("FILE_NAME");
        readTestWords(mFileName);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_learn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LearnAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void subscribeWords() {
        viewModel.getWords().observe(this, new Observer<List<Pair<String, String>>>() {
            @Override
            public void onChanged(List<Pair<String, String>> pairs) {
                adapter.submitList(pairs);
            }
        });
    }

    private void readTestWords(String fileName)
    {
        viewModel.readFile(this, fileName);
    }

}
