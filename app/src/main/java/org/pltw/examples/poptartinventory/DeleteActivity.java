package org.pltw.examples.poptartinventory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrisk on 1/31/2017.
 */
public class DeleteActivity extends AppCompatActivity {
    ArrayList<PopTart> inventory = new ArrayList<>();
    ArrayList<PopTart> inventory2 = new ArrayList<>();

    String saveStuff;

    ListView simplelist;
    Button returnButton;
    Button deleteButton;
    Button deleteAll;

    File target = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    File logFile = new File(target.getAbsolutePath() + "/", "PopTart.txt");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load();

        setContentView(R.layout.activity_delete);

        System.out.println(logFile);



        //Log.d("MyTag", inventory2.get(0).getName());


        returnButton = (Button) findViewById(R.id.return_to_main);
        deleteButton = (Button) findViewById(R.id.delete);
        simplelist = (ListView) findViewById(R.id.simple_listview_delete);
        deleteAll = (Button) findViewById(R.id.delete_all);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStuff = "";
                writeFile(saveStuff);
                load();

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
                saveStuff = "";
                //int temp = inventory2.size();
                for(int i = 0; i<inventory2.size(); i++){
                    if(inventory2.get(i).getDelete()){
                        inventory2.get(i).setName("null");

                    }
                }
                for (int i = 0; i < inventory2.size(); i++) {

                    if (inventory2.get(i).getName().equals("null")) {
                    } else {
                        saveStuff += inventory2.get(i).getName() + "/" + String.valueOf(inventory2.get(i).getCount()) + "/" + String.valueOf(inventory2.get(i).getMinimum()) + "/" ;
                        Log.d("MyTag", saveStuff);
                    }
                }
                writeFile(saveStuff);
                load();

                refresh();

            }
        });

        DeleteAdapter deleteAdapter = new DeleteAdapter(this, R.layout.list_view_delete, inventory2);
        simplelist.setAdapter(deleteAdapter);



    }

    public void refresh(){
        DeleteAdapter deleteAdapter = new DeleteAdapter(this, R.layout.list_view_delete, inventory2);
        simplelist.setAdapter(deleteAdapter);
    }
    public void load() {

        int i = 0;
        inventory2.clear();

        try {
            FileReader fr = new FileReader(logFile);
            BufferedReader in = new BufferedReader(fr);
            String str;
            if ((str = in.readLine()) != null) {
                System.out.println(str);

                while (str.indexOf("/") >= 0) {
                    String name = str.substring(0, str.indexOf("/"));
                    System.out.println(name);
                    str = str.substring(str.indexOf("/") + 1);
                    int count = Integer.valueOf(str.substring(0, str.indexOf("/")));
                    System.out.println(String.valueOf(count));
                    str = str.substring(str.indexOf("/") + 1);
                    int min = Integer.valueOf(str.substring(0, str.indexOf("/")));
                    System.out.println(String.valueOf(min));
                    str = str.substring(str.indexOf("/") + 1);

                    if (name.equals("null")) {
                    } else {
                        inventory2.add(new PopTart(name, count, min));

                    }
                }
            }

        } catch (Exception e) {

        }

    }
    public void writeFile(String text) {


        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Catch A - writing");
            }
        }
        try {

            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false));
            buf.write(text);
            buf.close();
            System.out.println(text);
            System.out.println("File written");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(text);
            System.out.println("Failed to write");
        }

    }

}
