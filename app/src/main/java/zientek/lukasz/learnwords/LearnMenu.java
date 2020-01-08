package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import zientek.lukasz.learnwords.dialogs.DialogMessage;
import zientek.lukasz.learnwords.model.Helpers;

public class LearnMenu extends AppCompatActivity
{
    private ListView mListViewTests;
    private DialogMessage dialogMessage;
    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_menu);

        mListViewTests = findViewById(R.id.main_listview_tests);
        dialogMessage = new DialogMessage();
        helpers = new Helpers(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, helpers.testList());
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

        mListViewTests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String filename = mListViewTests.getItemAtPosition(position).toString();

                Intent intent = new Intent(LearnMenu.this, TestScreen.class);
                intent.putExtra("FILE_NAME", filename);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_test_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.help)
            dialogMessage.showHelpDialog(this,"How does it work?",
                    "To start learning tap name of the test that you are interested in. \n"
                    + "To take a test click and hold name of the test that you want to take.");

        return super.onOptionsItemSelected(item);
    }
}
