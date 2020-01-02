package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

import cn.pedant.SweetAlert.SweetAlertDialog;
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
            position++;
            mProgressBar.setProgress((position+1)*100 / questions.size());
            askQuestion();
        }

        else
        {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            dialog.setTitle("Wrong answer");
            dialog.setContentText("Correct translation is: " + questions.get(position).getCorrectTranslation());
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
            {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    position++;
                    mProgressBar.setProgress((position+1)*100 / questions.size());
                    askQuestion();
                    dialog.dismiss();
                }
            });
            dialog.show();
            Button button = dialog.findViewById(R.id.confirm_button);
            button.setBackground(ContextCompat.getDrawable(TestScreen.this, R.drawable.button_green));

            wrongWords.append(questions.get(position).getWord()).append(" - ").append(questions.get(position).getCorrectTranslation()).append("\n");
            wrongWordsCount++;
        }
    }

    public void askQuestion()
    {
        if(questions.size() > position)
        {
            mUserInput.setText("");
            mQuestion.setText(questions.get(position).getWord());
            mQuestionNumber.setText("Test progress: " + (position+1) + "/" + questions.size());
        }

        else
        {
            if(wrongWordsCount < questions.size() && wrongWordsCount != 0)
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

            SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitle("Test completed");
            dialog.setContentText("Correct answers: " + correctAnws + "/" + questions.size());
            dialog.setCancelable(false);
            dialog.setConfirmButton("⟳", new SweetAlertDialog.OnSweetClickListener()
            {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), TestScreen.class);
                    intent.putExtra("FILE_NAME", mFileName);
                    startActivity(intent);
                }
            });

            dialog.setNeutralButton("⟳", new SweetAlertDialog.OnSweetClickListener()
            {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), TestScreen.class);
                    intent.putExtra("FILE_NAME", mFileName);
                    startActivity(intent);
                }
            });

            dialog.setCancelButton("←", new SweetAlertDialog.OnSweetClickListener()
            {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog)
                {
                    finish();
                }
            });

            dialog.show();
            if(wrongWordsCount == questions.size() || correctAnws == questions.size())
                dialog.getButton(SweetAlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);

            Button buttonWrongWords = dialog.findViewById(R.id.neutral_button);
            buttonWrongWords.setTextSize(35);
            buttonWrongWords.setPadding(0,-17,0,0);
            buttonWrongWords.setBackground(ContextCompat.getDrawable(TestScreen.this, R.drawable.button_accent));

            Button button = dialog.findViewById(R.id.confirm_button);
            button.setTextSize(35);
            button.setPadding(0,-17,0,0);
            button.setBackground(ContextCompat.getDrawable(TestScreen.this, R.drawable.button_green));

            Button buttonLeave = dialog.findViewById(R.id.cancel_button);
            buttonLeave.setTextSize(35);
            buttonLeave.setPadding(0,-24,0,0);
            buttonLeave.setBackground(ContextCompat.getDrawable(TestScreen.this, R.drawable.button_red));

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

        catch (FileNotFoundException x) { }

        catch (IOException x) { }

    }
}
