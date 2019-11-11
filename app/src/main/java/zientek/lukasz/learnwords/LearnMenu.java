package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

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

        mListViewTests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                String filename = mListViewTests.getItemAtPosition(position).toString();

                Intent intent = new Intent(LearnMenu.this, TestScreen.class);
                intent.putExtra("FILE_NAME", filename);
                startActivity(intent);
//                updateView();
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
            showHelpDialog();

        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog()
    {
        SweetAlertDialog dialog = new SweetAlertDialog(this);
        dialog.setTitleText("How does it work?");
        dialog.setContentText("To start learning tap name of the test that you are interested in. \n"
                        + "To take a test click and hold name of the test that you want to take.");
        dialog.show();
        Button button = dialog.findViewById(R.id.confirm_button);
        button.setBackground(ContextCompat.getDrawable(LearnMenu.this, R.drawable.button_green));
    }
}
