package zientek.lukasz.learnwords;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import zientek.lukasz.learnwords.adapter.ListViewAdapter;
import zientek.lukasz.learnwords.dialogs.DialogMessage;
import zientek.lukasz.learnwords.model.Helpers;

public class TestCreationMenu extends AppCompatActivity
{
    private ListView mListViewTests;
    private Helpers helpers;
    private ArrayList<String> tests;
    private DialogMessage dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creation_menu);
        helpers = new Helpers();
        dialogMessage = new DialogMessage();
        mListViewTests = findViewById(R.id.main_listview_tests);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        final File fileDir = this.getFilesDir();
        tests = helpers.TestList(this);
        final ListViewAdapter listViewAdapter = new ListViewAdapter(this, R.layout.test_item_layout, tests);
        mListViewTests.setAdapter(listViewAdapter);

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

        mListViewTests.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListViewTests.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener()
        {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
            {
                final int checkedCount = mListViewTests.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                listViewAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                mode.getMenuInflater().inflate(R.menu.menu_test_list_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu)
            {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item)
            {
                int id = item.getItemId();

                if(id == R.id.delete)
                {
                    SweetAlertDialog dialog = new SweetAlertDialog(TestCreationMenu.this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitle("Delete?");

                    if(listViewAdapter.getSelectedIds().size() == 1)
                        dialog.setContentText("Are you sure you want to delete selected test?");
                    else
                        dialog.setContentText("Are you sure you want to delete selected tests?");

                    dialog.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener()
                    {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {
                            SparseBooleanArray selected = listViewAdapter.getSelectedIds();
                            for (int i = (selected.size() - 1); i >= 0; i--)
                            {
                                if (selected.valueAt(i))
                                {
                                    String filename = mListViewTests.getItemAtPosition(selected.keyAt(i)).toString();
                                    File fileToDelete = new File(fileDir,filename);
                                    fileToDelete.delete();
                                    onResume();
                                    sweetAlertDialog.dismiss();
                                }
                            }
                            mode.finish();
                        }
                    });

                    dialog.setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {
                            sweetAlertDialog.dismiss();
                        }
                    });

                    dialog.show();
                    Button confirm = dialog.findViewById(R.id.confirm_button);
                    confirm.setBackground(ContextCompat.getDrawable(TestCreationMenu.this, R.drawable.button_green));
                    Button cancel = dialog.findViewById(R.id.cancel_button);
                    cancel.setBackground(ContextCompat.getDrawable(TestCreationMenu.this, R.drawable.button_red));
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode)
            {
                listViewAdapter.removeSelection();
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
            dialogMessage.showHelpDialog(this,"How does it work?",
                    "To create a test click on the plus button. "
                    + "To modify a test click on the name of the test. "
                    + "To delete a test click and hold name of the test.");

        return super.onOptionsItemSelected(item);
    }
}
