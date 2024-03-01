package com.example.mobiletechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.textViewOutput);
        EditText et = findViewById(R.id.editTextInput);

        setTitle("Mobile Tech App");

        Button btn = findViewById(R.id.buttonOK);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View msg) {
                tv.setText(et.getText());
            }
        });

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = (EditText) view;
                edit.setText("");
            }
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    tv.setText(et.getText());
                }
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String msg = extras.getString("message");
            if (msg != null) {
                tv.setText(msg);
            }
            String img = extras.getString("image");
            if (img != null) {
                setContentView(R.layout.activity_image);
                ImageView iv = findViewById(R.id.imageView);
                if (img.equals("dogs")) {
                    iv.setImageResource(R.drawable.dogs);
                } else if (img.equals("ducks")) {
                    iv.setImageResource(R.drawable.ducks);
                }
            }
        }
    }

    public void displayMessage(View view) {
        //TextView tv = findViewById(R.id.textViewOutput);
        //EditText et = findViewById(R.id.editTextInput);
        //jsckjasbtv.setText(et.getText());

        Toast.makeText(this,"OK button clicked.", Toast.LENGTH_LONG).show();

    }

}