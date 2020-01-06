package zientek.lukasz.learnwords.learn_screen;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import zientek.lukasz.learnwords.model.Pair;
import zientek.lukasz.learnwords.reader.FileReader;

public class LearnViewModel extends ViewModel {

    private MutableLiveData<List<Pair<String, String>>> words;
    LiveData<List<Pair<String, String>>> getWords(){
        if (words == null){
            words = new MutableLiveData<>();
        }
        return words;
    }

    public LearnViewModel() {

    }

    void readFile(Context context, String fileName){
        FileReader fileReader = new FileReader(context, fileName);
        List<Pair<String, String>> list = fileReader.getWords();
        words.postValue(list);
    }

}
