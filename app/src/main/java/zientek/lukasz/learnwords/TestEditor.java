package zientek.lukasz.learnwords;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class TestEditor extends AppCompatActivity
{
    private LinearLayout parentLinearLayout;
    private String mFileName;
    ImageView mPreviewIv;

    EditText inputDialog;
    private String dialogInput;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 2;
    private static final int IMAGE_GALLERY_REQUEST_CODE = 3;
    private static final int IMAGE_CAMERA_REQUEST_CODE = 4;

    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_editor);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        mPreviewIv = findViewById(R.id.imageIv);
        mFileName = getIntent().getStringExtra("FILE_NAME");

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(mFileName != null)
            readTest(mFileName);

    }

    public static void deleteCache(Context context)
    {
        try
        {
            File dir = context.getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e) {}
    }

    public static boolean deleteDir(File dir)
    {
        if (dir != null && dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
            return dir.delete();
        }

        else if(dir!= null && dir.isFile())
        {
            return dir.delete();
        }

        else
            return false;

    }

    public void onAddField(View v)
    {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v)
    {
        parentLinearLayout.removeView((View) v.getParent());
    }

    private void saveTest()
    {
        String filename = askTestName();
        String fileContents = collectInput();
        FileOutputStream outputStream;

        try
        {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String askTestName()
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        inputDialog = new EditText(this);

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Name your test")
                .setView(inputDialog)
                .setPositiveButton("Save", null)
                .show();

        Button positiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogInput = inputDialog.getText().toString();

                if (dialogInput.trim().isEmpty())
                {
                    inputDialog.setText("");
                    inputDialog.setHintTextColor(Color.RED);
                    inputDialog.setHint(" Please provide a name");
                }

//                else if (checkIfFileExists(dialogInput))
//                {
//                    Toast.makeText(TestEditor.this, "fdsfsfsdf", Toast.LENGTH_SHORT).show();
//                }

                else
                    handler.sendMessage(handler.obtainMessage());

            }
        });

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return dialogInput;
    }

    public boolean checkIfFileExists(String fileName)
    {
        final File fileDir = this.getFilesDir();

        for(String file: fileDir.list())
        {
            if(file.equals(fileName))
                return true;
        }

        return false;
    }

    private void readTest(String fileName)
    {
        try
        {
            FileInputStream fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            String lines;
            while((lines = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(lines).append("\n");
            }

            String[] linesOfWords = stringBuilder.toString().split("\n");

            for(int i = 0; i < linesOfWords.length; i++)
            {
                String lineWords = linesOfWords[i];
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field, null);
                EditText out1 = rowView.findViewById(R.id.edit_text);
                EditText out2 = rowView.findViewById(R.id.edit_text2);

                String[] singleWords = lineWords.split(" - ");
                out1.setText(singleWords[0]);
                out2.setText(singleWords[1]);
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            }

        }

        catch(FileNotFoundException x)
        {

        }
        catch(IOException x )
        {

        }
    }

    private String collectInput()
    {
        int size = parentLinearLayout.getChildCount();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i=0 ; i < size-1 ; i++)
        {
            View view = parentLinearLayout.getChildAt(i);
            EditText text = view.findViewById(R.id.edit_text);
            EditText text2 = view.findViewById(R.id.edit_text2);
            stringBuilder.append(text.getText().toString()).append(" - ").append(text2.getText().toString()).append("\n");
        }

        return stringBuilder.toString();
    }

    private boolean checkIfComplete()
    {
        boolean completeInput = false;
        int missingElements = 0;
        int size = parentLinearLayout.getChildCount();

        for (int i=0 ; i < size-1 ; i++)
        {
            View view = parentLinearLayout.getChildAt(i);
            EditText text = view.findViewById(R.id.edit_text);
            EditText text2 = view.findViewById(R.id.edit_text2);

            if(text.getText().toString().trim().isEmpty())
            {
                text.setText("");
                text.setHintTextColor(Color.RED);
                text.setHint("No word given");
                missingElements++;
            }

            if(text2.getText().toString().trim().isEmpty())
            {
                text2.setText("");
                text2.setHintTextColor(Color.RED);
                text2.setHint("No translation");
                missingElements++;
            }
        }

        if(missingElements == 0 && size != 1)
            completeInput = true;

        return completeInput;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_test_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.saveTest)
        {
            if(!checkIfComplete())
                Toast.makeText(this, "Incomplete data", Toast.LENGTH_SHORT).show();
            else
            {
                saveTest();
                super.finish();
                Toast.makeText(this, "Test saved", Toast.LENGTH_SHORT).show();
            }

        }

        if(id == R.id.addImage)
            showImageImportDialog();

        if (id == R.id.help)
            showHelpDialog();

        return super.onOptionsItemSelected(item);
    }

    private void showImageImportDialog()
    {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select image");
        dialog.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 0)
                {
                    // camera
                    if (!checkCameraPermission())
                        requestCameraPermission();

                    else
                        pickCamera();
                }

                if (which == 1)
                {
                    // gallery
                    if (!checkStoragePermission())
                        requestStoragePermission();

                    else
                        pickGallery();
                }

            }
        });
        dialog.create().show();
    }

    private void showHelpDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("How to create a test")
                .setMessage("To manually add field simply click on add field button. \n" +
                        "To add words from photo click on photo icon. \n \n" +
                        "When you are done with adding new words click on disc icon to save the test.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.create().show();
    }

    private void pickGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_GALLERY_REQUEST_CODE);
    }

    private void pickCamera()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image for conversion");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image for conversion");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAMERA_REQUEST_CODE);
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkCameraPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0)
                {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted)
                        pickCamera();

                    else
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0)
                {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted)
                        pickGallery();

                    else
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_GALLERY_REQUEST_CODE)
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);

            if(requestCode == IMAGE_CAMERA_REQUEST_CODE)
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);
                BitmapDrawable bitmapDrawable = (BitmapDrawable)mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational())
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

                else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = recognizer.detect(frame);
                    StringBuilder stringBuilder = new StringBuilder();

                    int rightOfLeft = 0;
                    int bottom = 0;
                    boolean firstTime = true;

                    for (int index = 0; index < textBlocks.size(); index++)
                    {
                        TextBlock tBlock = textBlocks.valueAt(index);
                        for (Text line : tBlock.getComponents())
                        {
                            for (Text element : line.getComponents())
                            {
                                int leftOfRight = element.getBoundingBox().left;

                                if(firstTime)
                                {
                                    firstTime = false;
                                    stringBuilder.append(element.getValue());
                                }

                                else if(rightOfLeft-leftOfRight >= -60 && (bottom-element.getBoundingBox().bottom <= 70
                                        && bottom - element.getBoundingBox().bottom >= -70)
                                        && !element.getValue().endsWith("-"))
                                {
                                    stringBuilder.append(" ").append(element.getValue());
                                }

                                else if (bottom-element.getBoundingBox().bottom <= 70 && bottom - element.getBoundingBox().bottom >= -70)
                                {
                                    stringBuilder.append("-").append(element.getValue());
                                }

                                else
                                {
                                    stringBuilder.append("\n").append(element.getValue());
                                }

                                bottom = element.getBoundingBox().bottom;
                                rightOfLeft = element.getBoundingBox().right;
                            }
                        }
                    }

                    if (stringBuilder.length() == 0)
                        Toast.makeText(this, "Couldn't convert given photo. Try again", Toast.LENGTH_LONG).show();

                    String finalResult = stringBuilder.toString();
                    String finalResultAfterValidation = finalResult.replaceAll("(-)+","-");
                    String[] linesOfWords = finalResultAfterValidation.split("\n");

                    for(int i = 0; i < linesOfWords.length; i++)
                    {
                        String lineWords = linesOfWords[i];
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.field, null);
                        EditText out1 = rowView.findViewById(R.id.edit_text);
                        EditText out2 = rowView.findViewById(R.id.edit_text2);

                        try
                        {
                            String[] singleWords = lineWords.split("-");
                            if(singleWords[0].equals(""))
                                out1.setHint("No word given");
                            else
                                out1.setText(singleWords[0]);

                            out2.setText(singleWords[1]);
                            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                        }

                        catch (Exception x)
                        {
                            out1.setText(linesOfWords[i]);
                            out2.setHint("No translation");
                            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                        }
                    }
                }
            }

            deleteCache(this);

        }
    }

}
