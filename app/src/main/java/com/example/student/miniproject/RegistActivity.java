package com.example.student.miniproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    Button yesButton, noButton,checkButton;
    EditText etId,etPassword,etPasswordConfirm,etName,etBirthdate,etEmail,etPhone,etAddress;
    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2;
    Spinner spinner,spinner2,spinner3,spinner4;
    String fav1String,fav2String,fav3String,fav4String,sexString;

    public String convertChar(String sss){
        byte[] byname = sss.getBytes();
        Charset euckrCharset = Charset.forName("utf-8");
        CharBuffer charBuffer = euckrCharset.decode(ByteBuffer.wrap(byname));
        String name = charBuffer.toString();
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        yesButton =findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        checkButton = findViewById(R.id.checkButton);
        etId = findViewById(R.id.etId);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etBirthdate = findViewById(R.id.etBirthdate);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        radioButton1 = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioGroup = findViewById(R.id.radiogroup);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString();
                String pw = "0";
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(LoginService.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LoginService loginService = retrofit.create(LoginService.class);
                loginService.idcheck(id,pw).enqueue(new Callback<Result>() {

                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                        Log.d("TAG",response.code()+"");

                        if(response.isSuccessful()){
                            Result result =response.body();
                            if(result.getMessage().equals("already")){
                                System.out.println("already");
                                Toast.makeText(RegistActivity.this, "ID가 이미 존재합니다!", Toast.LENGTH_LONG).show();
                            }
                            else if(result.getMessage().equals("able")){
                                System.out.println("able");
                                Toast.makeText(RegistActivity.this, "ID 사용이 가능합니다!", Toast.LENGTH_SHORT).show();
                            }
                            else if(result.getMessage().equals("exception")){
                                System.out.println("exception");
                                Toast.makeText(RegistActivity.this, "서버 오류입니다 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            System.out.println("response fail");
                            Toast.makeText(RegistActivity.this, "서버 오류입니다 다시 시도해주세요!!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                        System.out.println("onfailure");
                    }
                });
            }
        });



        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = etId.getText().toString();
                String pw = etPassword.getText().toString();
                String name = convertChar(etName.getText().toString());
                String birthdate = etBirthdate.getText().toString();
                String email = etEmail.getText().toString();
                int phone = Integer.parseInt(etPhone.getText().toString());
                String address = convertChar(etAddress.getText().toString());
                String sex = convertChar(sexString);
                String fav1 = convertChar(fav1String);
                String fav2 = convertChar(fav2String);
                String fav3 = convertChar(fav3String);
                String favcar = convertChar(fav4String);


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(LoginService.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LoginService loginService = retrofit.create(LoginService.class);
                loginService.register(id,pw,name,birthdate,email,phone,address,sex,fav1,fav2,fav3,favcar).enqueue(new Callback<Result>() {

                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                        Log.d("TAG",response.code()+"");

                        if(response.isSuccessful()){
                            Result result =response.body();
                            if(result.getMessage().equals("complete")){
                                System.out.println("complete");
                                Toast.makeText(RegistActivity.this, "회원가입 완료", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else if(result.getMessage().equals("error")){
                                System.out.println("error");
                                Toast.makeText(RegistActivity.this, "서버 에러!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            System.out.println("response fail");
                            Toast.makeText(RegistActivity.this, "서버 오류입니다 다시 시도해주세요!!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        t.printStackTrace();
                        System.out.println("onfailure");
                    }
                });
            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton){
                    sexString = radioButton1.getText().toString();
                    Toast.makeText(RegistActivity.this, sexString, Toast.LENGTH_SHORT).show();
                }
                else{
                    sexString = radioButton2.getText().toString();
                    Toast.makeText(RegistActivity.this, sexString, Toast.LENGTH_SHORT).show();
                }
            }
        });


        final String[] data = getResources().getStringArray(R.array.hobby);
        final String[] data2 = getResources().getStringArray(R.array.cars);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data2);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);
        spinner3.setAdapter(adapter);
        spinner4.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fav1String =  data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner2.setSelection(1);
                fav2String =  data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner3.setSelection(2);
                fav3String =  data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner4.setSelection(0);
                fav4String =  data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString();
                String confirm = etPasswordConfirm.getText().toString();

                if( password.equals(confirm) ) {
                    etPassword.setTextColor(Color.GREEN);
                    etPasswordConfirm.setTextColor(Color.GREEN);
                } else {
                    etPassword.setTextColor(Color.RED);
                    etPasswordConfirm.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}