package org.pltw.examples.poptartinventory;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
    Beta version 1.01
    Updates:
        Count text will now become red if count is below minimum.
        Email will now generate the full list, as opposed to only the poptarts that are below the minimum, per customer

     */

public class MainActivity extends AppCompatActivity {


    private ArrayList<PopTart> inventory = new ArrayList<>();
    private ListView simplelist;
    private Button addNewButton;
    private Button emailButton;
    private Button deleteButton;
    private String saveData;


    //Beta 0.98 - Current Persistence model as txt doc.
    private String filename = "popTart.txt";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        simplelist = (ListView) findViewById(R.id.simpleListView);

        //Creates the file if it doesn't exist
        File file = getBaseContext().getFileStreamPath(filename);

        if(file.exists())
        {
            Log.i("Success", "File found");

        } else {
            Log.i("Error", "File not found");

        }


        loadFromFile(filename, getApplicationContext());

        //Handling Intent return from Addition Activity
        Intent in = this.getIntent();
        String newFlavor = in.getStringExtra("flavor");
        int current = in.getIntExtra("current", 0);
        int min = in.getIntExtra("min", 10);


        saveData = "";

        //If newFlavor != null, we have returned from Addition Activity
        if (newFlavor != null) {
            //Here we are checking for multiple entries of the same name
            for (int i = 0; i < inventory.size(); i++) {
                if (newFlavor.equalsIgnoreCase(inventory.get(i).getName())) {
                    newFlavor = "match";
                    Toast.makeText(MainActivity.this,
                            "Duplication Error: Flavor already exists", Toast.LENGTH_SHORT).show();
                } else {

                }
                //Here we are adding the poptart to the ArrayList if it didn't match an exisiting poptart

            }
            if(!newFlavor.equalsIgnoreCase("match")) {
                inventory.add(new PopTart(newFlavor, current, min));
            }

            //This rewrites our string saveData to include this new flavor and the rewrites the txt file, updating the data
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.size() > 0) {
                    if (!inventory.get(i).getName().equals(null)) {
                        //Delimiting character being used is "/"
                        saveData += inventory.get(i).getName() + "/" + String.valueOf(inventory.get(i).getCount()) + "/" + String.valueOf(inventory.get(i).getMinimum()) + "/";
                    }
                }

            }

            //This writes the txt file via the writeToFile()
            writeToFile(saveData, getApplicationContext());

            //We wrote the file and now reload it to verify persistence


            loadFromFile(filename, getApplicationContext());
        }

        //Setting up the adapter for the listview
        MyAdapter myAdapter = new MyAdapter(this, R.layout.list_view_inventory, inventory);
        simplelist.setAdapter(myAdapter);


        // addNew Button Wire-up XML
        addNewButton = (Button) findViewById(R.id.addNew);
        addNewButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //starts the PopUp2 activity to add new poptarts to the listing
                Intent intent = new Intent(MainActivity.this, PopUp2.class);

                startActivity(intent);
            }
        });
        // emailButton Wireup XML
        emailButton = (Button) findViewById(R.id.email);
        emailButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //format the info
                String emailInfo = generateList(inventory);
                //send the info to the email method
                sendEmail(emailInfo);


            }
        });

        // deleteButton Wire-up XML
        deleteButton = (Button) findViewById(R.id.delete_main_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //starts the delete activity to delete poptarts from the listing
                Intent intent = new Intent(MainActivity.this, DeleteActivity.class);
                startActivity(intent);
            }
        });

    }

    //Generate Email Body
    public String generateList(ArrayList<PopTart> tarts) {
        String emailString = "Pop-Tarts needed: \n";
        for (PopTart tart : tarts) {
            emailString += tart.getName() + "  has " + tart.getCount() + " out of " + tart.getMinimum() + " \n__________________________________________________\n";
        }
        return emailString;

    }

    //Email intent
    protected void sendEmail(String emailInfo) {

        //Hardwired for now, to be variable in 1.1
        String[] TO = {"mrisk@wclark.k12.in.us"};
        String[] CC = {""};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pop-Tart Inventory");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailInfo);

        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Send mail..."), 0);


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
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



