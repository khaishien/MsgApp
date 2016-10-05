package com.example.shen_mini_itx.msgapp.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.R;

/**
 * Created by shen-mini-itx on 28-Aug-16.
 */
public class DialogTemp {

    private void newdialog(Context context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_box_alert);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogLeftButton = (Button) dialog.findViewById(R.id.button_left_dialog);
        dialogLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogRightButton = (Button) dialog.findViewById(R.id.button_right_dialog);
        dialogRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private static void showUserProfileDialog(final UserModel item, Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_user_profile);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("");

        Button dialogLeftButton = (Button) dialog.findViewById(R.id.button_left_dialog);
        dialogLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogRightButton = (Button) dialog.findViewById(R.id.button_right_dialog);
        dialogRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
