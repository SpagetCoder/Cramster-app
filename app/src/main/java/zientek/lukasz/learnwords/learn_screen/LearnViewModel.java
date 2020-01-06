package zientek.lukasz.learnwords.learn_screen;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import zientek.lukasz.learnwords.model.TestQuestions;
import zientek.lukasz.learnwords.reader.FileReader;

public class LearnViewModel extends ViewModel {

    private MutableLiveData<List<TestQuestions>> words;
    LiveData<List<TestQuestions>> getWords(){
        if (words == null){
            words = new MutableLiveData<>();
        }
        return words;
    }

    public LearnViewModel() {

    }

    void readFile(Context context, String fileName){
        FileReader fileReader = new FileReader(context, fileName);
        List<TestQuestions> list = fileReader.getWords();
        words.postValue(list);
    }

}
