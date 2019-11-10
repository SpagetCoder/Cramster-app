package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LearnScreen extends AppCompatActivity
{
    private LinearLayout parentLinearLayoutLearn;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_screen);

        mFileName = getIntent().getStringExtra("FILE_NAME");
        parentLinearLayoutLearn = findViewById(R.id.parent_linear_layout_learn);
        readTestWords(mFileName);
    }

    private void readTestWords(String fileName)
    {
        try
        {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            String lines;
            while((lines = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(lines).append("\n");
            }

            String[] linesOfWords = stringBuilder.toString().split("\n");

            for(int i = 0; i < linesOfWords.length; i++)
            {
                String lineWords = linesOfWords[i];
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.learn_field, null);
                TextView out1 = rowView.findViewById(R.id.edit_text_learn);
                TextView out2 = rowView.findViewById(R.id.edit_text2_learn);

                String[] singleWords = lineWords.split(" - ");
                out1.setText(singleWords[0]);
                out2.setText(singleWords[1]);
                parentLinearLayoutLearn.addView(rowView, parentLinearLayoutLearn.getChildCount() - 1);
            }

        }

        catch(FileNotFoundException x)
        {

        }
        catch(IOException x )
        {

        }
    }

}
