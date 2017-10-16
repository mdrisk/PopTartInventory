package org.pltw.examples.poptartinventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class PopUp2  extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        names = getIntent().getStringArrayListExtra("poptarts");

        setContentView(R.layout.activity_pop_up2);
        final EditText flavor =  (EditText) findViewById(R.id.flav);
        final EditText number = (EditText) findViewById(R.id.curstck);
        final EditText min = (EditText)  findViewById(R.id.minstck);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams wmlp = getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.LEFT;

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setAttributes(wmlp);
        getWindow().setLayout(width, height);
        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(PopUp2.this, MainActivity.class);
                  if (flavor.getText().toString().trim().length() == 0 || number.getText().toString().trim().length() == 0 || min.getText().toString().trim().length() == 0) {
                      Toast.makeText(PopUp2.this,
                      "Please enter all information", Toast.LENGTH_SHORT).show();
                  } else {
                      intent.putExtra("flavor", flavor.getText().toString().trim());
                      intent.putExtra("current", Integer.valueOf(number.getText().toString()));
                      intent.putExtra("min", Integer.valueOf(min.getText().toString()));
                      startActivity(intent);
                      }
                  }
            }
        );

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PopUp2.this,MainActivity.class ));
            }
        });

    }
}

