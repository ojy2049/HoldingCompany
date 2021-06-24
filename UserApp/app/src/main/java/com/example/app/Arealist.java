package com.example.app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Arealist extends AppCompatActivity {
    private RadioButton r_btn1, r_btn2, r_btn3, r_btn4, r_btn5, r_btn6, r_btn7, r_btn8;
    private Button btn;
    private String Region = "";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        r_btn1 = findViewById(R.id.r_btn1);
        r_btn2 = findViewById(R.id.r_btn2);
        r_btn3 = findViewById(R.id.r_btn3);
        r_btn4 = findViewById(R.id.r_btn4);
        r_btn5 = findViewById(R.id.r_btn5);
        r_btn6 = findViewById(R.id.r_btn6);
        r_btn7 = findViewById(R.id.r_btn7);
        r_btn8 = findViewById(R.id.r_btn8);

        textView=(TextView)findViewById(R.id.text3);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Arealist.this, Searchview.class);
                intent.putExtra("Region", Region);
                startActivity(intent);
            }
        });

    }
        public void RadioButtonClicked(View view){
            boolean check = ((RadioButton) view).isChecked();

            switch (view.getId()) {
                case R.id.r_btn1:
                    if (check)
                        Region = "종로구";
                    break;
                case R.id.r_btn2:
                    if (check)
                        Region = "중구";
                    break;

            }

        }
    }

