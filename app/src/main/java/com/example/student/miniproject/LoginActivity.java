package com.example.student.miniproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    Button loginbutton, signbutton, bt_init, button;
    EditText idInput, passwordInput;
    ArrayList<Member> list = new ArrayList<Member>();
    MyAdapter myAdapter;
    SharedPreferences manager;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAdapter = new MyAdapter(this);
        makeUi();

        manager = PreferenceManager.getDefaultSharedPreferences(this);
        editor = manager.edit();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MODE_PRIVATE);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = idInput.getText().toString();
                String pwd = passwordInput.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(LoginService.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LoginService loginService = retrofit.create(LoginService.class);
                loginService.getData2(id, pwd).enqueue(new Callback<Result>() {


                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                        Log.d("TAG", response.code() + "");

                        if (response.isSuccessful()) {
                            Result result = response.body();
                            if (result.getMessage().equals("idfail")) {
                                System.out.println("idfail");
                            } else if (result.getMessage().equals("pwfail")) {
                                System.out.println("pwfail");
                            } else if (result.getMessage().equals("success")) {
                                System.out.println("success");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("id",id);
                                startActivity(intent);
                            } else if (result.getMessage().equals("exception")) {
                                System.out.println("exception");
                            }
                        } else {
                            System.out.println("response fail");
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

        signbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    public void makeUi() {
        idInput = findViewById(R.id.idInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginbutton = findViewById(R.id.loginButton);
        signbutton = findViewById(R.id.signButton);

    }

    class MyAdapter extends BaseAdapter { //Adapter 사용자 정의
        Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size(); //갯수에 따라서 getView가 그만큼 실행됨
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) { //5개면 position이 5개로 5번 호출됨
            View view = null;
            //  view = View.inflate(MainActivity.this,R.layout.personview,null);


            TextView textView = view.findViewById(R.id.textView);
            TextView textView2 = view.findViewById(R.id.textView2);

            textView.setText(list.get(position).getName());
            //    textView2.setText(list.get(position).getNum()+" ");


            return view;
        }
    }
}
