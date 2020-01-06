package zientek.lukasz.learnwords.reader;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import zientek.lukasz.learnwords.model.Pair;

public class FileReader {

    private String fileName;
    private Context context;

    public FileReader(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    public List<Pair<String, String>> getWords() {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            List<Pair<String, String>> wordsList = new ArrayList<>();
            String lines;
            String[] singleWords;
            while ((lines = bufferedReader.readLine()) != null) {
                singleWords = lines.split(" - ");
                wordsList.add(new Pair<>(singleWords[0], singleWords[1]));
            }
            return wordsList;
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {
        }

        return new ArrayList<>();
    }
}
