package com.webduino.fragment.cardinfo.optioncardvalue;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.webduino.MainActivity;
import com.webduino.R;

/**
 * Created by giaco on 15/02/2018.
 */

public class DecimalOptionCardValue extends OptionCardValue {

    public DecimalOptionCardValue(String name, Double value) {
        super(name,value);
        valueDescription = "" + value;
    }

    @Override
    public double getDoubleValue() {

        if (getValue() == null) return 0;
        return (double) getValue();
    }

    @Override
    public String getStringValue() {
        if (getValue() == null) return "";
        return "" + getValue();
    }


    public Object showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.activity);

        LayoutInflater inflater = MainActivity.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_decimaloptionvalue, null);
        final TextView tv = view.findViewById(R.id.decimalTextView);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CharSequence str = tv.getText();
                        setValue(Double.valueOf(str.toString()));
                        valueDescription = "" + getValue();
                        if (listeners != null)
                            for (OptionCardListener listener:listeners)
                                listener.onSetValue(getValue());
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return null;
    }
}
