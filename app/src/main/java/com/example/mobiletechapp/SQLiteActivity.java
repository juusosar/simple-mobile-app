package com.example.mobiletechapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SQLiteActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    String selectedColour;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                selectedColour = selectedRadioButton.getText().toString();
            }
        });

    }
    public void pressSave(View view) {
        MyDbHelper db = new MyDbHelper(this, "MobileTech", null, 1);
            if (db != null) {
                db.deleteAllColours();
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                    if (rb.getText().toString().equals(selectedColour))
                        db.insertColour(rb.getText().toString(), "true");
                    else
                        db.insertColour(rb.getText().toString(), "false");
                }

                ArrayList<String> colours = db.getAllColours();
                TextView textView = findViewById(R.id.textViewColours);
                textView.setText("");
                for (String colour : colours)
                    textView.append("(" + colour + ") ");
            }
        }
    }

