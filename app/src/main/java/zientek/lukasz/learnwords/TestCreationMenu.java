package zientek.lukasz.learnwords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestCreationMenu extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creation_menu);
    }

    public void goToTestEditor(View view)
    {
        Intent intent = new Intent(TestCreationMenu.this, TestEditor.class);
        startActivity(intent);
    }


}
