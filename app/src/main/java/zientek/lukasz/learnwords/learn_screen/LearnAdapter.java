package zientek.lukasz.learnwords.learn_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import zientek.lukasz.learnwords.R;
import zientek.lukasz.learnwords.model.Pair;

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.LearnViewHolder> {

    private List<Pair<String, String>> wordsList;

    LearnAdapter(){
        wordsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public LearnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_pair, parent, false);
        return new LearnViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnViewHolder holder, int position) {
        holder.bind(wordsList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordsList.size();
    }

    void submitList(List<Pair<String, String>> wordsList){
        this.wordsList = wordsList;
        notifyDataSetChanged();
    }

    class LearnViewHolder extends RecyclerView.ViewHolder{

        private TextView left;
        private TextView right;

        LearnViewHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.tv_word_left);
            right = itemView.findViewById(R.id.tv_word_right);
        }

        void bind(Pair<String, String> wordPair){
            left.setText(wordPair.getFirst());
            right.setText(wordPair.getSecond());
        }

    }
}
