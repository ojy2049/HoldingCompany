package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    String u_name="", r_name="", phone="", address="";
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;

    private EditText edit_user_name, edit_r_name, edit_phone, edit_address, edit_address_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        edit_user_name = (EditText) findViewById(R.id.edit_u_name);
        edit_r_name = (EditText) findViewById(R.id.edit_r_name);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_address_detail = (EditText) findViewById(R.id.edit_address_detail);
        Button btn_search = (Button) findViewById(R.id.search_button);
        Button btn_register = findViewById(R.id.register_button);

        Intent getintent = getIntent();
        String UserName = getintent.getStringExtra("UserName");
        String UserEamil = getintent.getStringExtra("Email");
        edit_user_name.setText(UserName);

        edit_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        if (btn_search != null) {
            btn_search.setOnClickListener(v -> {
                Intent i = new Intent(Register.this, Search_address.class);
                startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
            });
        }
        if (btn_register !=null){
            btn_register.setOnClickListener(v -> {
                u_name = edit_user_name.getText().toString();
                r_name = edit_r_name.getText().toString();
                phone = edit_phone.getText().toString();
                address = edit_address.getText().toString() + edit_address_detail.getText().toString();
                ;
                if (u_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 전부 입력하세여", Toast.LENGTH_LONG).show();
                } else if (u_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 전부 입력하세여", Toast.LENGTH_LONG).show();
                } else if (phone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 전부 입력하세여", Toast.LENGTH_LONG).show();
                } else if (address.length() == 0) {
                    Toast.makeText(getApplicationContext(), "정보를 전부 입력하세여", Toast.LENGTH_LONG).show();
                }
                else{
                    retrofitservice retrofit = new retrofitservice();
                    Call<Result> call = retrofit.api.getRegister(u_name, r_name, phone, address, UserEamil);
                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                                System.out.println("오류1");
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            System.out.println("오류2");
                        }
                    });
                }


            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case SEARCH_ADDRESS_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    String data = intent.getExtras().getString("data");
                    System.out.println(data);
                    if (data != null) {
                        edit_address.setText(data);
                    }
                }
                break;
        }
    }
}