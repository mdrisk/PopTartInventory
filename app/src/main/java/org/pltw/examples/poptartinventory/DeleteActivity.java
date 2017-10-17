package org.pltw.examples.poptartinventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by mrisk on 1/31/2017.
 */
public class DeleteActivity extends AppCompatActivity {

    private ArrayList<PopTart> inventory = new ArrayList<>();
    private String saveData;
    private ListView simplelist;
    private Button returnButton;
    private Button deleteButton;
    private Button deleteAll;

    private String filename = "popTart.txt";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFromFile(filename, getApplicationContext());
        setContentView(R.layout.activity_delete);

        returnButton = (Button) findViewById(R.id.return_to_main);
        deleteButton = (Button) findViewById(R.id.delete);
        simplelist = (ListView) findViewById(R.id.simple_listview_delete);
        deleteAll = (Button) findViewById(R.id.delete_all);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = "";
                writeToFile(saveData, getApplicationContext());
                loadFromFile(filename, getApplicationContext());
                refresh();
            }
        });


        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeleteActivity.this,MainActivity.class ));
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = "";

                for(int i = 0; i< inventory.size(); i++){
                    if(inventory.get(i).getDelete()){
                        inventory.get(i).setName("null");

                    }
                }
                for (int i = 0; i < inventory.size(); i++) {

                    if (inventory.get(i).getName().equals("null")) {
                    } else {
                        saveData += inventory.get(i).getName() + "/" + String.valueOf(inventory.get(i).getCount()) + "/" + String.valueOf(inventory.get(i).getMinimum()) + "/" ;

                    }
                }
                writeToFile(saveData,getApplicationContext() );
                loadFromFile(filename, getApplicationContext());

                refresh();

            }
        });

        DeleteAdapter deleteAdapter = new DeleteAdapter(this, R.layout.list_view_delete, inventory);
        simplelist.setAdapter(deleteAdapter);
    }

    // Force a refresh
    public void refresh(){
        DeleteAdapter deleteAdapter = new DeleteAdapter(this, R.layout.list_view_delete, inventory);
        simplelist.setAdapter(deleteAdapter);
    }


    // Beta 1.01 Current persistence
    public void writeToFile(String text, Context ctx) {
        FileOutputStream outputStream;


        try {
            outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
            Log.i("save", "Saved Data");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("save", "Didn't save!");
        }
    }

    // Beta 1.01 Current persistence
    public void loadFromFile(String filename, Context ctx) {

        //Clear the ArrayList
        inventory.clear();
        int i = 0;
        try {
            FileInputStream fis = ctx.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");

            //StringBuilder sb = new StringBuilder();
            //String line;


            BufferedReader in = new BufferedReader(isr);
            String str;
            if ((str = in.readLine()) != null) {
                //System.out.println(str);

                //Reading and assigning data based on delimiting character "/"
                while (str.indexOf("/") >= 0) {
                    String name = str.substring(0, str.indexOf("/"));
                    //System.out.println(name);
                    str = str.substring(str.indexOf("/") + 1);
                    int count = Integer.valueOf(str.substring(0, str.indexOf("/")));
                    //System.out.println(String.valueOf(count));
                    str = str.substring(str.indexOf("/") + 1);
                    int min = Integer.valueOf(str.substring(0, str.indexOf("/")));
                    //System.out.println(String.valueOf(min));
                    str = str.substring(str.indexOf("/") + 1);

                    if (name.equals("null")) {
                    } else {
                        //Adding poptarts back into ArrayList
                        inventory.add(new PopTart(name, count, min));
                        //System.out.println(inventory.size());
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
