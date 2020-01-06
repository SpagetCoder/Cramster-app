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
        try
        {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            while((lines = bufferedReader.readLine()) != null)
            {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.learn_field, null);
                TextView out1 = rowView.findViewById(R.id.edit_text_learn);
                TextView out2 = rowView.findViewById(R.id.edit_text2_learn);

                String[] singleWords = lines.split(" - ");
                out1.setText(singleWords[0]);
                out2.setText(singleWords[1]);
                parentLinearLayoutLearn.addView(rowView, parentLinearLayoutLearn.getChildCount() - 1);
            }
        }

        catch (FileNotFoundException x) { }
        catch(IOException x ) { }
    }

}
