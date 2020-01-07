package zientek.lukasz.learnwords.model;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileReader
{

    private String fileName;
    private Context context;

    public FileReader(Context context, String fileName)
    {
        this.fileName = fileName;
        this.context = context;
    }

    public ArrayList<TestQuestions> getWords()
    {
        try
        {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lines;
            String[] singleWords;
            ArrayList<TestQuestions> wordsList = new ArrayList<>();
            while ((lines = bufferedReader.readLine()) != null)
            {
                singleWords = lines.split(" - ");
                wordsList.add(new TestQuestions(singleWords[0], singleWords[1]));
            }
            return wordsList;
        }
        catch (FileNotFoundException ignored) { }
        catch (IOException ignored) { }

        return new ArrayList<>();
    }

}