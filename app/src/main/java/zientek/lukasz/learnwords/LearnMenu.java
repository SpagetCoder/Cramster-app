package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class LearnMenu extends AppCompatActivity
{

    private ListView mListViewTests;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_menu);

        mListViewTests = (ListView) findViewById(R.id.main_listview_tests);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ArrayList<String> tests = new ArrayList<>();
        final File fileDir = this.getFilesDir();

        for(String file: fileDir.list())
            tests.add(0,file);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,tests);
        mListViewTests.setAdapter(arrayAdapter);

        mListViewTests.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String filename = mListViewTests.getItemAtPosition(position).toString();

                Intent intent = new Intent(LearnMenu.this, LearnScreen.class);
                intent.putExtra("FILE_NAME", filename);
                startActivity(intent);
            }
        });
    }


}
