package org.pltw.examples.poptartinventory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrisk on 11/9/2016.
 */
public class MyAdapter extends ArrayAdapter<PopTart> {

    ArrayList<PopTart> popTartList = new ArrayList<>();
    Button updateButton;
    String format;

    public MyAdapter(Context context, int textViewResourceId, ArrayList<PopTart> tarts) {
        super(context, textViewResourceId, tarts);
        popTartList = tarts;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_inventory, null);

        final TextView countView;
        final EditText countUpdate;
        final TextView nameView;

        //XML wire-up
        nameView = (TextView) v.findViewById(R.id.name);
        countView = (TextView) v.findViewById(R.id.countnumber);
        countUpdate = (EditText) v.findViewById(R.id.edit_text);
        updateButton = (Button) v.findViewById(R.id.increment);

        nameView.setText(popTartList.get(position).getName());

        // Formatting the "5/10" view
        format = String.valueOf(popTartList.get(position).getCount()) + " / " + String.valueOf(popTartList.get(position).getMinimum());
        countView.setText(format);

        //Handles updating the text to Red if current is below minimum count
        if (popTartList.get(position).getCount() < popTartList.get(position).getMinimum()) {
            countView.setTextColor(Color.RED);
        } else {
            countView.setTextColor(Color.DKGRAY);
        }


        updateButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (countUpdate.getText().toString().trim().equalsIgnoreCase("")) {
                    countUpdate.clearFocus();
                    hideKeyboardFrom(getContext(), v);
                } else {

                    String countUpdateString = countUpdate.getText().toString();
                    popTartList.get(position).setCount(Integer.valueOf(countUpdateString));
                    format = String.valueOf(popTartList.get(position).getCount()) + " / " + String.valueOf(popTartList.get(position).getMinimum());
                    countView.setText(format);
                    countUpdate.getText().clear();
                    countUpdate.clearFocus();
                    hideKeyboardFrom(getContext(), v);

                    //Creating the saveData after updating the count
                    String saveData = "";
                    for (int i = 0; i < popTartList.size(); i++) {

                        if (!popTartList.get(i).getName().equals("null")) {
                            saveData += popTartList.get(i).getName() + "/" + String.valueOf(popTartList.get(i).getCount()) + "/" + String.valueOf(popTartList.get(i).getMinimum()) + "/";
                        }
                    }

                    //Handles updating the text to Red if current is below minimum count
                    if (popTartList.get(position).getCount() < popTartList.get(position).getMinimum()) {
                        countView.setTextColor(Color.RED);
                    } else {
                        countView.setTextColor(Color.DKGRAY);
                    }
                    //Save and reload
                    new MainActivity().writeToFile(saveData, getContext());
                    new MainActivity().loadFromFile("popTart.txt", getContext());
                }
            }
        });
        return v;
    }
    //Used to force hide the keyboard after typing and hitting confirmation button
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
