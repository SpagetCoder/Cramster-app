package zientek.lukasz.learnwords.dialogs;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;
import zientek.lukasz.learnwords.R;

public class DialogMessage
{
    public void showHelpDialog(Context context, String title, String message)
    {
        SweetAlertDialog dialog = new SweetAlertDialog(context);
        dialog.setTitleText(title);
        dialog.setContentText(message);
        dialog.show();
        Button button = dialog.findViewById(R.id.confirm_button);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_green));
    }
}
