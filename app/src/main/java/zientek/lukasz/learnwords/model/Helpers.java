package zientek.lukasz.learnwords.model;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;

public class Helpers
{
    private Context context;

    public Helpers(Context context)
    {
        this.context = context;
    }

    public ArrayList<String> testList()
    {
        ArrayList<String> tests = new ArrayList<>();
        final File fileDir = context.getFilesDir();

        for(String file: fileDir.list())
            tests.add(0,file);

        return tests;
    }

    public boolean checkIfFileExists(String fileName)
    {
        final File fileDir = context.getFilesDir();

        for(String file: fileDir.list())
        {
            if(file.equals(fileName))
                return true;
        }

        return false;
    }

    public void deleteCache()
    {
        try
        {
            File dir = context.getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e) {}
    }

    private boolean deleteDir(File dir)
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

}
