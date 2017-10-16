package org.pltw.examples.poptartinventory;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrisk on 1/31/2017.
 */
public class DeleteAdapter extends ArrayAdapter<PopTart> {
    private ArrayList<PopTart> popTartList = new ArrayList<>();
    private Button buttonOne;
    private Button buttonOne2;

    public DeleteAdapter(Context context, int textViewResourceId, ArrayList<PopTart> tarts){
        super(context, textViewResourceId, tarts);
        popTartList = tarts;
        }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_delete, null);

        TextView nameView = (TextView) v.findViewById(R.id.name_delete);
        buttonOne = (Button) v.findViewById(R.id.delete_check);

        nameView.setText(popTartList.get(position).getName());
        buttonOne.setText("Delete");

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popTartList.get(position).getDelete()) {
                    popTartList.get(position).setDelete(false);
                    buttonOne2 = (Button) v.findViewById(R.id.delete_check);
                    buttonOne2.setBackgroundResource(android.R.drawable.btn_default);
                } else {
                    popTartList.get(position).setDelete(true);
                    buttonOne2 = (Button) v.findViewById(R.id.delete_check);
                    buttonOne2.setBackgroundColor(Color.RED);
                }
               }
          });
    return v;
    }
}
