package com.tempus.proyectos.tempusx;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ecernar on 10/11/2016.
 */

public class ViewDialog {

    public Button dialogButtonAceptar;
    public Button dialogButtonCancelar;
    public Dialog viewDialog;

    public ViewDialog(Activity activity) {
        viewDialog = new Dialog(activity);
        viewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewDialog.setCancelable(false);
        viewDialog.setContentView(R.layout.alert);
    }

    public void showDialog(String titulo, String msg){

        TextView text = (TextView) viewDialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        TextView text1 = (TextView) viewDialog.findViewById(R.id.text_title);
        text1.setText(titulo);

        dialogButtonAceptar = (Button) viewDialog.findViewById(R.id.btn_dialog_ok);
        dialogButtonCancelar = (Button) viewDialog.findViewById(R.id.btn_dialog_ko);

        //dialogButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        dialog.dismiss();
        //    }
        //});

        viewDialog.show();

    }
}