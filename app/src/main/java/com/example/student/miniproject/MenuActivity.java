package com.example.student.miniproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skt.Tmap.TMapData;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    Button button, customokButton, changeButton, reservationChange, reservationCancel;
    ImageButton homeImgButton, myImgButton, customImgButton, reservationImgButton, csImgButton;
    TextView lightTextview, tempTextview, seatTextview, carTextview, nameTextview, emailTextview, phoneTextview, addressTextview, interestTextview1,
            interestTextview2, interestTextview3, preferTextview, resCarTextview, resTimeTextview, resStartTextview, resEndTextview,
            resReceiveTextview, resReturnTextview, resFeeTextview, resDrivingfeeTextview, resOption1Textview,
            resOption2Textview,resOption3Textview,resOption4Textview;
    SeekBar lightSeekbar, tempSeekbar, seatSeekbar, carSeekbar;
    LinearLayout layout, layout2, layout3, layout4;
    Intent intent;
    String id;
    SharedPreferences manager;
    SharedPreferences.Editor editor;
    Handler handler = null;
    final TMapData tMapData = new TMapData();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MenuService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 이거 있어야 리버스지오코딩 작동함
        StrictMode.enableDefaults();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        intent = getIntent();
        id = intent.getStringExtra("id");

        button = findViewById(R.id.button);
        customokButton = findViewById(R.id.customokButton);
        changeButton = findViewById(R.id.changeButton);
//        reservationChange = findViewById(R.id.reservationChange);
        reservationCancel = findViewById(R.id.reservationCancel);

        homeImgButton = findViewById(R.id.homeImgButton);
        myImgButton = findViewById(R.id.myImgButton);
        customImgButton = findViewById(R.id.customImgButton);
        reservationImgButton = findViewById(R.id.reservationImgButton);
        csImgButton = findViewById(R.id.csImgButton);

        homeImgButton.setOnClickListener(this);
        myImgButton.setOnClickListener(this);
        customImgButton.setOnClickListener(this);
        reservationImgButton.setOnClickListener(this);
        csImgButton.setOnClickListener(this);

        layout = findViewById(R.id.layout);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);

        lightTextview = findViewById(R.id.lightTextview);
        tempTextview = findViewById(R.id.tempTextview);
        seatTextview = findViewById(R.id.seatTextview);
        carTextview = findViewById(R.id.carTextview);

        nameTextview = findViewById(R.id.nameTextview);
        emailTextview = findViewById(R.id.emailTextview);
        phoneTextview = findViewById(R.id.phoneTextview);
        addressTextview = findViewById(R.id.addressTextview);
        interestTextview1 = findViewById(R.id.interestTextview1);
        interestTextview2 = findViewById(R.id.interestTextview2);
        interestTextview3 = findViewById(R.id.interestTextview3);
        preferTextview = findViewById(R.id.preferTextview);

        resCarTextview = findViewById(R.id.resCarTextview);
        resTimeTextview = findViewById(R.id.resTimeTextview);
        resStartTextview = findViewById(R.id.resStartTextview);
        resEndTextview = findViewById(R.id.resEndTextview);
        resReceiveTextview = findViewById(R.id.resReceiveTextview);
        resReturnTextview = findViewById(R.id.resReturnTextview);
        resFeeTextview = findViewById(R.id.resFeeTextview);
        resDrivingfeeTextview  = findViewById(R.id.resDrivingfeeTextview);
        resOption1Textview = findViewById(R.id.resOption1Textview);
        resOption2Textview = findViewById(R.id.resOption2Textview);
        resOption3Textview = findViewById(R.id.resOption3Textview);
        resOption4Textview = findViewById(R.id.resOption4Textview);

        manager = PreferenceManager.getDefaultSharedPreferences(this);
        editor = manager.edit();
        editor.putString("idlist", id);
        editor.commit();
        id = manager.getString("idlist", "");
        lightSeekbar = findViewById(R.id.lightSeekbar);
        tempSeekbar = findViewById(R.id.tempSeekbar);
        seatSeekbar = findViewById(R.id.seatSeekbar);
        carSeekbar = findViewById(R.id.carSeekbar);

        myImgButton.setImageResource(R.drawable.myinfo_click);

        // 내정보에 고객정보 셋팅
                MenuService menuService = retrofit.create(MenuService.class);
                menuService.myinfo(id).enqueue(new Callback<Cust>() {
                @Override
                public void onResponse(Call<Cust> call, Response<Cust> response) {
                Cust cust = response.body();
                nameTextview.setText(cust.getCust_name());
                emailTextview.setText(cust.getCust_address());
                phoneTextview.setText("0" + cust.getCust_phone());
                addressTextview.setText(cust.getCust_address());
                interestTextview1.setText(cust.getCust_fav1());
                interestTextview2.setText(cust.getCust_fav2());
                interestTextview3.setText(cust.getCust_fav3());
                preferTextview.setText(cust.getCust_favcar());
            }

            @Override
            public void onFailure(Call<Cust> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "My Information Response Fail", Toast.LENGTH_SHORT).show();
            }
        });


        // 마이예약조회에 예약정보 셋팅
        menuService.reservationCheck(id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                Order order = response.body();
                resCarTextview.setText(order.getMtype());
                resDrivingfeeTextview.setText(order.getMtype());
                resStartTextview.setText(order.getStime());
                resEndTextview.setText(order.getEtime());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date startTime = dateFormat.parse(order.getStime());
                    Date endTime = dateFormat.parse(order.getEtime());
                    long calDate = endTime.getTime() - startTime.getTime();
                    long minutes = calDate / 60000 ;
                    resTimeTextview.setText(minutes+" 분");

                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(MenuActivity.this, "Usage Time Reception Fail",Toast.LENGTH_SHORT).show();
                }


                try {
                    final String saddress = tMapData.convertGpsToAddress(order.getSlng(),order.getSlag());
                    final String eaddress = tMapData.convertGpsToAddress(order.getElng(),order.getElag());
                    handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resReceiveTextview.setText(saddress);
                            resReturnTextview.setText(eaddress);
                        }
                    });
                }catch(Exception e) {
                   Toast.makeText(MenuActivity.this, "Reservation Check Exception",Toast.LENGTH_SHORT).show();
                }

                DecimalFormat priceFormat = new DecimalFormat("###,###");
                String formattedPrice = priceFormat.format(order.getTotalprice());
                resFeeTextview.setText(formattedPrice +" 원");
                resDrivingfeeTextview .setText("200 원/km");
                resOption1Textview.setText(order.getMoption1());
                resOption2Textview.setText(order.getMoption2());
                resOption3Textview.setText(order.getMoption3());
                resOption4Textview.setText(order.getMoption4());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "예약 내역이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });


        lightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lightTextview.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tempSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempTextview.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seatSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seatTextview.setText("" + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        carSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    carTextview.setText("단거리 여가형");
                } else if (progress == 1) {
                    carTextview.setText("장거리 여행형");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.homeImgButton) {
            finish();
        }else if (v.getId() == R.id.myImgButton) {
            myImgButton.setImageResource(R.drawable.myinfo_click);
            customImgButton.setImageResource(R.drawable.mycustom);
            reservationImgButton.setImageResource(R.drawable.myreser);
            csImgButton.setImageResource(R.drawable.mycall);
            layout.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.INVISIBLE);
            layout4.setVisibility(View.INVISIBLE);
        }else if (v.getId() == R.id.customImgButton) {
            myImgButton.setImageResource(R.drawable.myinfo);
            customImgButton.setImageResource(R.drawable.mycustom_click);
            reservationImgButton.setImageResource(R.drawable.myreser);
            csImgButton.setImageResource(R.drawable.mycall);
            layout.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.INVISIBLE);
            layout4.setVisibility(View.INVISIBLE);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MenuService.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MenuService menuService = retrofit.create(MenuService.class);
            menuService.getcustom(id).enqueue(new Callback<Cust>() {
                @Override
                public void onResponse(Call<Cust> call, Response<Cust> response) {
                    Cust cust = response.body();
                    lightSeekbar.setProgress(Integer.parseInt(cust.getCtmlgt()));
                    seatSeekbar.setProgress(Integer.parseInt(cust.getCtmseat()));
                    tempSeekbar.setProgress(Integer.parseInt(cust.getCtmtep()));
                    int car = 0;
                    if(cust.getCtmcar().equals("장거리 여행형")){
                        car = 1;
                    }
                    carSeekbar.setProgress(car);
                }

                @Override
                public void onFailure(Call<Cust> call, Throwable t) {
                    Toast.makeText(MenuActivity.this, "getcustom Fail", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if (v.getId() == R.id.reservationImgButton) {
            myImgButton.setImageResource(R.drawable.myinfo);
            customImgButton.setImageResource(R.drawable.mycustom);
            reservationImgButton.setImageResource(R.drawable.myreser_click);
            csImgButton.setImageResource(R.drawable.mycall);
            layout.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.INVISIBLE);
        } else if (v.getId() == R.id.csImgButton) {
            myImgButton.setImageResource(R.drawable.myinfo);
            customImgButton.setImageResource(R.drawable.mycustom);
            reservationImgButton.setImageResource(R.drawable.myreser);
            csImgButton.setImageResource(R.drawable.mycall_click);
            layout.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            layout3.setVisibility(View.INVISIBLE);
            layout4.setVisibility(View.VISIBLE);

        }else if (v.getId() == R.id.customokButton) {
            String light = (String) lightTextview.getText();
            String temp = (String) tempTextview.getText();
            String seat = (String) seatTextview.getText();
            String car = (String) carTextview.getText();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MenuService.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MenuService menuService = retrofit.create(MenuService.class);
            menuService.customize(id, light, temp, seat, car).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.d("TAG", response.code() + "");

                    if (response.isSuccessful()) {
                        Result result = response.body();
                        if (result.getMessage().equals("complete")) {
                            System.out.println("complete");
                            Toast.makeText(MenuActivity.this, "커스터마이제이션 완료", Toast.LENGTH_LONG).show();
                        } else if (result.getMessage().equals("error")) {
                            System.out.println("error");
                            Toast.makeText(MenuActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("response fail");
                        Toast.makeText(MenuActivity.this, "서버 오류입니다 다시 시도해주세요", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                    System.out.println("onfailure");
                }
            });

        } else if (v.getId() == R.id.changeButton) {
            //비밀번호 변경
            PasswordchangeDialog passwordchangeDialog = new PasswordchangeDialog(MenuActivity.this);
            passwordchangeDialog.callFunction();

        } else if (v.getId() == R.id.logoutButton) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("로그 아웃");
            dlg.setMessage("로그 아웃 하시겠습니까?");
            dlg.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            dlg.setPositiveButton("아니오", null);
            dlg.show();

        } else if (v.getId() == R.id.deleteButton) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("회원탈퇴");
            dlg.setMessage("정말로 탈퇴하시겠습니까?");
            dlg.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.mydelete(id).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d("TAG", response.code() + "");

                            if (response.isSuccessful()) {
                                Result result = response.body();
                                if (result.getMessage().equals("complete")) {
                                    Toast.makeText(MenuActivity.this, "회원 탈퇴 완료", Toast.LENGTH_LONG).show();
                                }
                                else if (result.getMessage().equals("error")) {
                                    Toast.makeText(MenuActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MenuActivity.this, "서버 오류입니다 다시 시도해주세요", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            t.printStackTrace();
                            System.out.println("onfailure");
                        }
                    });

                    Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            dlg.setPositiveButton("아니오", null);
            dlg.show();

        }
//        else if (v.getId() == R.id.reservationChange){
//            //예약 변경
//            ReservationchangeDialog reservationchangeDialog = new ReservationchangeDialog(MenuActivity.this);
//            reservationchangeDialog.callFunction();
//
//        }
        else if (v.getId() == R.id.reservationCancel){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("예약 취소");
            dlg.setMessage("예약 취소 하시겠습니까?");
            dlg.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.reservationcancel(id).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d("TAG", response.code() + "");

                            if (response.isSuccessful()) {
                                Result result = response.body();
                                if (result.getMessage().equals("complete")) {
                                    Toast.makeText(MenuActivity.this, "예약 취소 완료", Toast.LENGTH_LONG).show();
                                }
                                else if (result.getMessage().equals("error")) {
                                    Toast.makeText(MenuActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MenuActivity.this, "서버 오류입니다 다시 시도해주세요", Toast.LENGTH_LONG).show();
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
            dlg.setPositiveButton("아니오", null);
            dlg.show();
        }
    }

    class PasswordchangeDialog extends AppCompatActivity {

        private Context context;
        EditText etPassword,etPasswordConfirm;
        Button passwordchangeYes, passwordchangeNo;
        ImageButton passwordchangeCancel;

        public PasswordchangeDialog(Context context) {
            this.context = context;
        }


        public void callFunction()  {

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog(context);

            // 액티비티의 타이틀바를 숨긴다.
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dlg.setContentView(R.layout.passwordchange);

            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            passwordchangeYes = dlg.findViewById(R.id.passwordchangeYes);
            passwordchangeNo = dlg.findViewById(R.id.passwordchangeNo);
            passwordchangeCancel = dlg.findViewById(R.id.changeCancel);
            etPassword = dlg.findViewById(R.id.etPassword);
            etPasswordConfirm = dlg.findViewById(R.id.etPasswordConfirm);

            passwordchangeCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });

            passwordchangeYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String password = etPasswordConfirm.getText().toString();

                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.passwordchange(id, password).enqueue(new Callback<Result>() {
                        @Override
                         public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d("TAG", response.code() + "");

                            if (response.isSuccessful()) {
                                Result result = response.body();
                                if (result.getMessage().equals("complete")) {
                                    Toast.makeText(MenuActivity.this, "비밀번호 변경 완료", Toast.LENGTH_LONG).show();
                                } else if (result.getMessage().equals("error")) {
                                    Toast.makeText(MenuActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                System.out.println("response fail");
                                Toast.makeText(MenuActivity.this, "서버 오류입니다 다시 시도해주세요", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });

            passwordchangeNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
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
        }

    }
//
//    class ReservationchangeDialog extends AppCompatActivity {
//
//        private Context context;
//        Button reservationchangeYes,reservationchangeNo;
//        ImageButton reservationchangeCancel;
//
//        public ReservationchangeDialog(Context context) {
//            this.context = context;
//        }
//
//        public void callFunction()  {
//
//            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
//            final Dialog dlg = new Dialog(context);
//
//            // 액티비티의 타이틀바를 숨긴다.
//            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//            // 커스텀 다이얼로그의 레이아웃을 설정한다.
//            dlg.setContentView(R.layout.reservationchange);
//
//            // 커스텀 다이얼로그를 노출한다.
//            dlg.show();
//
//            reservationchangeYes = dlg.findViewById(R.id.reservationchangeYes);
//            reservationchangeNo = dlg.findViewById(R.id.reservationchangeNo);
//            reservationchangeCancel = dlg.findViewById(R.id.reservationchangeCancel);
//
//            reservationchangeCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // 커스텀 다이얼로그를 종료한다.
//                    dlg.dismiss();
//                }
//            });
//
//            reservationchangeYes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // 커스텀 다이얼로그를 종료한다.
//                    dlg.dismiss();
//                }
//            });
//
//            reservationchangeNo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    // 커스텀 다이얼로그를 종료한다.
//                    dlg.dismiss();
//                }
//            });
//
//        }
//
//    }
//
}