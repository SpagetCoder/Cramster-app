package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import zientek.lukasz.learnwords.model.FileReader;
import zientek.lukasz.learnwords.model.TestQuestions;

public class LearnScreen extends AppCompatActivity
{
    private LinearLayout parentLinearLayoutLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_screen);

        String mFileName = getIntent().getStringExtra("FILE_NAME");
        parentLinearLayoutLearn = findViewById(R.id.parent_linear_layout_learn);
        readTestWords(mFileName);
    }

    private void readTestWords(String fileName)
    {
        FileReader fileReader = new FileReader(this, fileName);
        List<TestQuestions> questionsList = fileReader.getWords();

        for(int i = 0; i < questionsList.size(); i++)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.learn_field, null);
            TextView out1 = rowView.findViewById(R.id.edit_text_learn);
            TextView out2 = rowView.findViewById(R.id.edit_text2_learn);

            out1.setText(questionsList.get(i).getWord());
            out2.setText(questionsList.get(i).getTranslation());
            parentLinearLayoutLearn.addView(rowView, parentLinearLayoutLearn.getChildCount() - 1);
        }
    }
}
