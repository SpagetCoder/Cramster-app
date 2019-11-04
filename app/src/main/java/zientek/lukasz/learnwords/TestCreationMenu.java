package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestCreationMenu extends AppCompatActivity
{
    private ListView mListViewTests;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creation_menu);

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

                Intent intent = new Intent(TestCreationMenu.this, TestEditor.class);
                intent.putExtra("FILE_NAME", filename);
                startActivity(intent);
            }
        });

        mListViewTests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String filename = mListViewTests.getItemAtPosition(position).toString();
                File fileToDelete = new File(fileDir,filename);
                fileToDelete.delete();
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

    public void goToTestEditor(View view)
    {
        Intent intent = new Intent(TestCreationMenu.this, TestEditor.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.help)
            showHelpDialog();

        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("How does it work?")
                .setMessage("Here you can find list of all created tests. \n" +
                        "To create a new test click on plus button below. \n" +
                        "To modify a test click on the name of the test. \n" +
                        "To delete a test swipe the name of the test.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.create().show();
    }


}
