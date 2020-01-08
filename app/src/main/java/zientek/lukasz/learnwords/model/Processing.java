package zientek.lukasz.learnwords.model;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Processing
{
    public String processTheData(Bitmap bitmap, TextRecognizer recognizer)
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

                    if (firstTime)
                    {
                        firstTime = false;
                        stringBuilder.append(element.getValue());
                    }

                    else if (element.getValue().trim().equals("-"))
                    {
                        continue;
                    }

                    else if (rightOfLeft - leftOfRight >= -50 && (bottom - element.getBoundingBox().bottom <= 40
                            && bottom - element.getBoundingBox().bottom >= -40))
                    {
                        stringBuilder.append(" ").append(element.getValue());
                    }

                    else if (bottom - element.getBoundingBox().bottom <= 50 && bottom - element.getBoundingBox().bottom >= -50)
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

        return stringBuilder.toString();
    }


}
