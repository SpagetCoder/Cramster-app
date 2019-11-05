package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import zientek.lukasz.learnwords.model.TestQuestions;

public class TestScreen extends AppCompatActivity
{
    private String mFileName;
    private EditText mUserInput;
    private TextView mQuestion, mQuestionNumber;
    private ProgressBar mProgressBar;
    private ArrayList<TestQuestions> questions;
    private int position = 0;
    private int correctAnws = 0;
    private StringBuilder wrongWords = new StringBuilder();
    private int wrongWordsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_screen);

        mFileName = getIntent().getStringExtra("FILE_NAME");
        mUserInput = findViewById(R.id.anwswer);
        mQuestion = findViewById(R.id.question);
        mQuestionNumber = findViewById(R.id.question_number);
        mProgressBar = findViewById(R.id.progress);
        questions = new ArrayList<>();
        setQuestions();
        askQuestion();
    }


    public void nextQuestion(View view)
    {
        if(mUserInput.getText().toString().trim().equals(questions.get(position).getCorrectTranslation()))
        {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            correctAnws++;
        }

        else
        {
            wrongWords.append(questions.get(position).getWord()).append(" - ").append(questions.get(position).getCorrectTranslation()).append("\n");
            wrongWordsCount++;
        }

        position++;
        mProgressBar.setProgress((position+1)*100 / questions.size());
        askQuestion();

    }

    public void askQuestion()
    {
        if(questions.size() > position)
        {
            mUserInput.setText("");
            mQuestion.setText(questions.get(position).getWord());
            mQuestionNumber.setText("Test progress: " + (position+1));
        }

        else
        {
            if(wrongWordsCount < questions.size())
            {
                if(!mFileName.contains(" - words you still need to learn"))
                    this.mFileName = mFileName + " - words you still need to learn";

                String fileContents = wrongWords.toString();
                FileOutputStream outputStream;

                try
                {
                    outputStream = openFileOutput(mFileName, Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            final AlertDialog builder = new AlertDialog.Builder(this)
                    .setTitle("Test completed")
                    .setCancelable(false)
                    .setPositiveButton("Again", null)
                    .setNeutralButton("Only worng words", null)
                    .setNegativeButton("Leave",null)
                    .show();


            Button positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), TestScreen.class);
                    intent.putExtra("FILE_NAME", mFileName);
                    startActivity(intent);
                }
            });

            Button neutralButton = builder.getButton(AlertDialog.BUTTON_NEUTRAL);
            if(wrongWordsCount == questions.size())
            neutralButton.setEnabled(false);

            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), TestScreen.class);
                    intent.putExtra("FILE_NAME", mFileName);
                    startActivity(intent);
                }
            });

            Button netativeButton = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            netativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
    }

    public void setQuestions()
    {
        try
        {
            FileInputStream fileInputStream = openFileInput(mFileName);
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
                String[] singleWords = lineWords.split(" - ");
                questions.add(new TestQuestions(singleWords[0],singleWords[1]));
            }
        }

        catch (FileNotFoundException x)
        {

        }

        catch (IOException x)
        {

        }

    }
}
