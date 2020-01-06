package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
{
    ArrayList<String> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tests = new ArrayList<>();
        final File fileDir = this.getFilesDir();

        for(String file: fileDir.list())
            tests.add(0,file);
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
            SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this);
            dialog.setTitle("No test found");
            dialog.setContentText("In order to access this menu there has to be at least one test in the memory. " +
                    "Please create a test by clicking the create a test button");
            dialog.show();
            Button confirm = dialog.findViewById(R.id.confirm_button);
            confirm.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.button_green));
        }

        else
        {
            Intent intent = new Intent(MainActivity.this,LearnMenu.class);
            startActivity(intent);
        }

    }
}
