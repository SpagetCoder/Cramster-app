package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import zientek.lukasz.learnwords.dialogs.DialogMessage;
import zientek.lukasz.learnwords.model.Helpers;

public class MainActivity extends AppCompatActivity
{
    private ArrayList<String> tests;
    private DialogMessage dialogMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogMessage = new DialogMessage();
        Helpers helpers = new Helpers();

        tests = helpers.TestList(this);
    }

    public void goToTestCreation(View view)
    {
        Intent intent = new Intent(MainActivity.this,TestCreationMenu.class);
        startActivity(intent);
    }

    public void goToLearnMenu(View view)
    {
        if(tests.size() == 0)
        {
            dialogMessage.showHelpDialog(this,"No test found",
                    "In order to access this menu there has to be at least one test in the memory. " +
                    "Please create a test by clicking the create a test button");
        }

        else
        {
            Intent intent = new Intent(MainActivity.this,LearnMenu.class);
            startActivity(intent);
        }

    }
}
