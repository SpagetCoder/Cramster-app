package zientek.lukasz.learnwords;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TestEditor extends AppCompatActivity
{
    private LinearLayout parentLinearLayout;
    private String mFileName;
    ImageView mPreviewIv;
    EditText inputDialog;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int STORAGE_REQUEST_CODE = 2;
    private static final int IMAGE_GALLERY_REQUEST_CODE = 3;
    private static final int IMAGE_CAMERA_REQUEST_CODE = 4;

    String[] cameraPermission;
    String[] storagePermission;
    Uri image_uri;

    private ShowDialogListener showDialogListener;

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

        showDialogListener = new ShowDialogListener() {
            @Override
            public void showDialog(String fileName) {
	            createOverrideDialog(fileName);
            }
	
	        @Override
	        public void saveFile(String enteredFileName) {
		        saveTestFile(enteredFileName);
	        }
        };

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
	
	private void saveTest() {
		displayAskFileNameDialog();
	}
	
	private void saveTestFile(String fileName) {
		String fileContents = collectInput();
		FileOutputStream outputStream;
		
		try {
			outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(fileContents.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
		Toast.makeText(this, "Test saved", Toast.LENGTH_SHORT).show();
	}
	
	public void displayAskFileNameDialog()
    {

        inputDialog = new EditText(this);

        if(mFileName != null)
            inputDialog.setText(mFileName);

        final SweetAlertDialog dialog = new SweetAlertDialog(this);
        dialog.setTitle("Name your test");
        dialog.setCustomView(inputDialog);
        dialog.setConfirmButton("Save", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
	            String enteredFileName = inputDialog.getText().toString().replaceAll("\\s+$", "");
	
	            if ( enteredFileName.trim().isEmpty() )
                {
                    inputDialog.setHintTextColor(Color.RED);
                    inputDialog.setHint("Please provide a name");
                } else if ( checkIfFileExists(enteredFileName) )
                {
	                showDialogListener.showDialog(enteredFileName);
	                sweetAlertDialog.dismissWithAnimation();
                } else {
		            showDialogListener.saveFile(enteredFileName);
		            sweetAlertDialog.dismissWithAnimation();
	            }
            }
        });

        dialog.show();
        Button save = dialog.findViewById(R.id.confirm_button);
        save.setBackground(ContextCompat.getDrawable(TestEditor.this, R.drawable.button_green));
    }
	
	private void createOverrideDialog(final String fileName) {
        SweetAlertDialog dialog2 = new SweetAlertDialog(TestEditor.this, SweetAlertDialog.WARNING_TYPE);
        dialog2.setTitle("Overwrite?");
        dialog2.setContentText("Test with that name already exists. Do you want to overwrite it?");
        dialog2.setCancelable(false);
        dialog2.setConfirmButton("Yes", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
	            showDialogListener.saveFile(fileName);
                sweetAlertDialog.dismissWithAnimation();
            }
        });

        dialog2.setCancelButton("No",new SweetAlertDialog.OnSweetClickListener()
        {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                sweetAlertDialog.dismissWithAnimation();
            }
        });

        dialog2.show();

        Button confirm = dialog2.findViewById(R.id.confirm_button);
        Button cancel = dialog2.findViewById(R.id.cancel_button);
        confirm.setBackground(ContextCompat.getDrawable(TestEditor.this, R.drawable.button_green));
        cancel.setBackground(ContextCompat.getDrawable(TestEditor.this, R.drawable.button_red));
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
            EditText eText = view.findViewById(R.id.edit_text);
            EditText eText2 = view.findViewById(R.id.edit_text2);

            String text = eText.getText().toString().trim();
            String text2 = eText2.getText().toString().trim();

            stringBuilder.append(text).append(" - ").append(text2).append("\n");
        }

        return stringBuilder.toString();
    }

    private boolean checkIfComplete()
    {
        boolean completeInput = false;
        int missingElements = 0;
        int size = parentLinearLayout.getChildCount();

        for (int i = 0 ; i < size-1 ; i++)
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

        else if(missingElements == 0)
            Toast.makeText(this,"Incomplete data: No words were added", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(this,"Incomplete data: " + missingElements + " elements missing", Toast.LENGTH_SHORT).show();

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
            if(checkIfComplete())
            {
	            saveTest();
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
        SweetAlertDialog dialog = new SweetAlertDialog(TestEditor.this);
        dialog.setTitle("Import image from");
        dialog.setConfirmButton("Camera", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                if (!checkCameraPermission())
                    requestCameraPermission();

                else
                    pickCamera();

                sweetAlertDialog.dismiss();
            }
        });
        dialog.setCancelButton("Gallery", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog)
            {
                if (!checkStoragePermission())
                    requestStoragePermission();

                else
                    pickGallery();

                sweetAlertDialog.dismiss();
            }
        });
        dialog.show();

        Button button = dialog.findViewById(R.id.confirm_button);
        Button button2 = dialog.findViewById(R.id.cancel_button);
        button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_green));
        button2.setBackground(ContextCompat.getDrawable(this, R.drawable.button_green));
    }

    private void showHelpDialog()
    {
        SweetAlertDialog dialog = new SweetAlertDialog(TestEditor.this);
        dialog.setTitle("How to create a test");
        dialog.setContentText("To manually add field click on the add field button. "
                + "To add words from photo click on the photo icon. "
                + "To save your test click on the disc icon.");
        dialog.show();
        Button button = dialog.findViewById(R.id.confirm_button);
        button.setBackground(ContextCompat.getDrawable(TestEditor.this, R.drawable.button_green));
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
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
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

    public void ProcessTheData(Bitmap bitmap, TextRecognizer recognizer)
    {
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
                for (final Text element : line.getComponents())
                {
                    int leftOfRight = element.getBoundingBox().left;

                    if(firstTime)
                    {
                        firstTime = false;
                        stringBuilder.append(element.getValue());
                    }

                    else if(element.getValue().trim().equals("-"))
                    {
                        continue;
                    }

                    else if(rightOfLeft-leftOfRight >= -50 && (bottom - element.getBoundingBox().bottom <= 40
                            && bottom - element.getBoundingBox().bottom >= -40))
                    {
                        stringBuilder.append(" ").append(element.getValue());
                    }

                    else if (bottom-element.getBoundingBox().bottom <= 50 && bottom - element.getBoundingBox().bottom >= -50)
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

        else
        {
            String finalResult = stringBuilder.toString();
            String firstValidation = finalResult.replaceAll(", -",", ").replaceAll(",-", ", ");
            String finalResultAfterValidation = firstValidation.replaceAll("(-)+","-");
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
                {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                else
                {
                    ProcessTheData(bitmap,recognizer);
                }
            }

            deleteCache(this);
        }
    }

    interface ShowDialogListener{
	    void showDialog(String fileName);
	
	    void saveFile(String enteredFileName);
    }
}
