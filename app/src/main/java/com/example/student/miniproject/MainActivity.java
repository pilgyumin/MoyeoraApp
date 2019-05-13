package com.example.student.miniproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 현재 위치로 지도 보여주는 어플
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    LinearLayout searchLayout,checkLayout,inuseLayout,startlayout, destlayout;
    TextView textView2,textView3,textView4,textView5,textView6,textView7,textView8,choosedate;
    EditText startEditText,endEditText;
    Button checkButton,completereserv,searchButton,resvstart,resvend,resvstart1,resvend1,reset,fianlres, carreceive;
    ImageButton imageButton,imageButton2,unlockButton,lockButton,extensionButton,returnButton;

    TMapView tMapView;
    TMapData tmapdata;
    WebView webView, webView2;

    double startlat,startlng,endlat,endlng;//POI 방식스타트
    double current,current1;//터치방식스타트

    boolean alreadyreceive = false;
    boolean alreadyreservation = false;
    int cid = 0;

    Search search;
    SharedPreferences manager;
    SharedPreferences.Editor editor;

    String startreservationDate,endreservationDate;

    // Retrofit
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MainService.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey( "36c25d23-ea56-4f80-bed5-6e447452647f" );
        linearLayoutTmap.addView( tMapView );
        webView = findViewById(R.id.webView);
        webView2 = findViewById(R.id.webView2);

        reset = findViewById(R.id.reset);
        fianlres = findViewById(R.id.fianlres);

        startlayout = findViewById(R.id.startlayout);
        destlayout = findViewById(R.id.destlayout);

        tmapdata = new TMapData();
        resvstart =findViewById(R.id.resvstart);
        resvstart1 =findViewById(R.id.resvstart1);
        resvend =findViewById(R.id.resvend);
        resvend1 =findViewById(R.id.resvend1);
        carreceive = findViewById(R.id.carreceive);
        completereserv = findViewById(R.id.completereserv);

        checkLayout = findViewById(R.id.checkLayout);
        searchButton = findViewById(R.id.searchButton);
        checkButton = findViewById(R.id.checkButton);
        searchLayout = findViewById(R.id.searchLayout);
        inuseLayout = findViewById(R.id.inuseLayout);
        unlockButton = findViewById(R.id.unlockButton);
        lockButton = findViewById(R.id.lockButton);
        extensionButton = findViewById(R.id.extensionButton);
        returnButton = findViewById(R.id.returnButton);
        imageButton = findViewById(R.id.imageButton);
        imageButton2 = findViewById(R.id.imageButton2);
        startEditText = findViewById(R.id.startEdittext);
        endEditText = findViewById(R.id.endEdittext);

        resvstart.setVisibility(View.INVISIBLE);
        resvend.setVisibility(View.INVISIBLE);

        resvend1.setVisibility(View.INVISIBLE);
        fianlres.setVisibility(View.INVISIBLE);

        resvstart1.setVisibility(View.VISIBLE);
        reset.setVisibility(View.INVISIBLE);
        carreceive.setVisibility(View.INVISIBLE);

        checkLayout.setVisibility(View.INVISIBLE);
        inuseLayout.setVisibility(View.INVISIBLE);

        manager = PreferenceManager.getDefaultSharedPreferences(this);
        editor = manager.edit();
        Intent intent = getIntent();
        String loginid = intent.getExtras().getString("id");
        editor.putString("idlist",loginid);
        editor.commit();

        MainService mainService = retrofit.create(MainService.class);
        mainService.checkreceive(loginid).enqueue(new Callback<Result>() {

            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    if (result.getMessage().equals("reservation")) {
                        Toast.makeText(MainActivity.this, "reservation", Toast.LENGTH_LONG).show();
                        alreadyreceive = false;
                    }
                    else if (result.getMessage().equals("driving")) {
                        Toast.makeText(MainActivity.this, "driving", Toast.LENGTH_LONG).show();
                        alreadyreceive = true;
                    }
                    else if (result.getMessage().equals("null")) {
                        Toast.makeText(MainActivity.this, "예약 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (result.getMessage().equals("error")) {
                        Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "서버 오류입니다 oncreate()", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this, "fail oncreate()", Toast.LENGTH_LONG).show();
            }

        });

        mainService.reservationconfirm(loginid).enqueue(new Callback<Result>() {

            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    if (result.getMessage().equals("complete")) {
                        alreadyreservation = true;
                    }
                    else if (result.getMessage().equals("null")) {
                        alreadyreservation = false;
                    }
                    else if (result.getMessage().equals("error")) {
                        alreadyreservation = true;
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "서버 오류입니다 searchbutton", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(MainActivity.this, "fail oncreate()", Toast.LENGTH_LONG).show();
            }

        });


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    class Search implements Runnable{
        TMapPoint tMapPointStart;
        TMapPoint tMapPointEnd;


        public Search(TMapPoint tMapPointStart, TMapPoint tMapPointEnd) {
            this.tMapPointStart = tMapPointStart;
            this.tMapPointEnd = tMapPointEnd;
        }

        TMapPolyLine tMapPolyLine;

        @Override
        public void run() {
            try {
                tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                tMapPolyLine.setLineColor(Color.BLUE);
                tMapPolyLine.setLineWidth(2);
                tMapView.addTMapPolyLine("Line2", tMapPolyLine);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        String loginid = manager.getString("idlist", "");
        MainService mainService = retrofit.create(MainService.class);
        mainService.mapoff(loginid, 1).enqueue(new Callback<Result>() {

            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }

        });
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fianlres){
            tMapView.removeAllTMapPolyLine();
            tMapView.removeMarkerItem("markerItem2");
            tMapView.removeMarkerItem("markerItem2");
            tMapView.removeAllTMapPOIItem();
            searchLayout.setVisibility(View.INVISIBLE);
            inuseLayout.setVisibility(View.INVISIBLE);

            reset.setVisibility(View.INVISIBLE);
            fianlres.setVisibility(View.INVISIBLE);

            imageButton.setVisibility(View.INVISIBLE);
            imageButton2.setVisibility(View.INVISIBLE);

            startEditText.setVisibility(View.INVISIBLE);
            endEditText.setVisibility(View.INVISIBLE);

            unlockButton.setVisibility(View.INVISIBLE);
            unlockButton.setEnabled(false);
            imageButton.setEnabled(false);
            imageButton2.setEnabled(false);
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }
            });
            TMapMarkerItem markerItem2 = new TMapMarkerItem();
            TMapPoint tMapPoint2 = new TMapPoint(37.498299, 127.029180);
            MainService mainService = retrofit.create(MainService.class);
            String loginid = manager.getString("idlist", "");
            mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                }

            });

            Reservation reservation = new Reservation(MainActivity.this,"예약시작");
            reservation.callFunction();


        }else if(v.getId()==R.id.button){
            String loginid = manager.getString("idlist", "");
            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
            intent.putExtra("id",loginid);
            startActivity(intent);

            MainService mainService = retrofit.create(MainService.class);
            mainService.mapoff(loginid, 1).enqueue(new Callback<Result>() {

                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                }

            });
        }
        else if(v.getId()==R.id.searchButton){
            String loginid = manager.getString("idlist", "");
            MainService mainService = retrofit.create(MainService.class);

            mainService.reservationconfirm(loginid).enqueue(new Callback<Result>() {

                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                    if (response.isSuccessful()) {
                        Result result = response.body();
                        if (result.getMessage().equals("complete")) {
                            alreadyreservation = true;
                            Toast.makeText(MainActivity.this, "이미 예약하였습니다.", Toast.LENGTH_LONG).show();
                        }
                        else if (result.getMessage().equals("null")) {
                            alreadyreservation = false;
                            Toast.makeText(MainActivity.this, "예약 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if (result.getMessage().equals("error")) {
                            alreadyreservation = true;
                            Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "서버 오류입니다 searchbutton", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {

                }

            });
            if(alreadyreservation){
                completereserv.setEnabled(false);
                completereserv.setVisibility(View.VISIBLE);
                resvstart.setVisibility(View.INVISIBLE);

            }
            else{
                resvstart1.setEnabled(true);
            }

            startlayout.setVisibility(View.VISIBLE);
            destlayout.setVisibility(View.VISIBLE);
            resvstart1.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.VISIBLE);
            checkButton.setVisibility(View.VISIBLE);
            checkButton.setEnabled(true);
            checkLayout.setVisibility(View.INVISIBLE);
            inuseLayout.setVisibility(View.INVISIBLE);
            unlockButton.setVisibility(View.INVISIBLE);
            carreceive.setVisibility(View.INVISIBLE);
            carreceive.setEnabled(false);
            unlockButton.setEnabled(false);
            imageButton.setEnabled(true);
            imageButton2.setEnabled(true);


            mainService.mapoff(loginid, 1).enqueue(new Callback<Result>() {

                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                }

            });
        }
        else if(v.getId()==R.id.checkButton){
            searchLayout.setVisibility(View.INVISIBLE);
            String loginid = manager.getString("idlist", "");
            MainService mainService = retrofit.create(MainService.class);
            if(!alreadyreceive){

                unlockButton.setEnabled(false);
                checkButton.setEnabled(true);

                checkButton.setText("예약 차량 위치 조회");
                checkButton.setVisibility(View.VISIBLE);
                checkLayout.setVisibility(View.VISIBLE);
                inuseLayout.setVisibility(View.INVISIBLE);
                completereserv.setEnabled(false);
                completereserv.setVisibility(View.INVISIBLE);
                resvend1.setVisibility(View.INVISIBLE);
                resvend.setVisibility(View.INVISIBLE);
                fianlres.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.INVISIBLE);
                resvstart1.setVisibility(View.INVISIBLE);
                unlockButton.setVisibility(View.INVISIBLE);

                if(alreadyreservation){
                    carreceive.setEnabled(true);
                    carreceive.setVisibility(View.VISIBLE);
                }
                else{
                    carreceive.setEnabled(false);
                    carreceive.setVisibility(View.INVISIBLE);
                }

                unlockButton.setEnabled(false);
                imageButton.setEnabled(false);
                imageButton2.setEnabled(false);

                mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                    }

                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("http://70.12.245.130/Moyeora/carlocation.myr?id="+loginid);
                webView.setWebViewClient(new WebViewClientClass());
            }
            else{

                checkButton.setText("사용 차량 조회");
                unlockButton.setEnabled(true);
                searchLayout.setVisibility(View.INVISIBLE);
                checkLayout.setVisibility(View.INVISIBLE);
                inuseLayout.setVisibility(View.VISIBLE);
                unlockButton.setVisibility(View.VISIBLE);
                unlockButton.setEnabled(true);
                imageButton.setEnabled(false);
                resvstart1.setVisibility(View.INVISIBLE);
                carreceive.setVisibility(View.INVISIBLE);

                imageButton2.setEnabled(false);
                mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                    }

                });

                webView2.getSettings().setJavaScriptEnabled(true);
                webView2.loadUrl("http://70.12.245.130/Moyeora/currentlocation.myr?id="+loginid);
                webView2.setWebViewClient(new WebViewClientClass());

            }


        }

        else if(v.getId() == R.id.unlockButton){
            String loginid = manager.getString("idlist", "");
            MainService mainService = retrofit.create(MainService.class);
            mainService.dooropen(loginid).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            final Handler mHandler;
            mHandler = new Handler();
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려 주세요.", true);
                    mHandler.postDelayed( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {

                                if (mProgressDialog != null && mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                    String loginid = manager.getString("idlist", "");
                                    MainService mainService = retrofit.create(MainService.class);
                                    mainService.opencheck(loginid).enqueue(new Callback<Result>() {

                                        @Override
                                        public void onResponse(Call<Result> call, Response<Result> response) {
                                            Log.d("TAG", response.code() + "");

                                            if (response.isSuccessful()) {
                                                Result result = response.body();
                                                if (result.getMessage().equals("open")) {
                                                    Toast.makeText(MainActivity.this, "차문이 열렸습니다", Toast.LENGTH_LONG).show();
                                                }
                                                else if (result.getMessage().equals("error")) {
                                                    Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(MainActivity.this, "서버 오류입니다 unlock", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Result> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });

                                }
                            }
                            catch ( Exception e )
                            {
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
                }
            } );

        }
        else if(v.getId() == R.id.lockButton){
            String loginid = manager.getString("idlist", "");
            MainService mainService = retrofit.create(MainService.class);
            mainService.doorlock(loginid).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            final Handler mHandler;
            mHandler = new Handler();
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    final ProgressDialog mProgressDialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려 주세요.", true);
                    mHandler.postDelayed( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {

                                if (mProgressDialog != null && mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                    String loginid = manager.getString("idlist", "");
                                    MainService mainService = retrofit.create(MainService.class);
                                    mainService.opencheck(loginid).enqueue(new Callback<Result>() {

                                        @Override
                                        public void onResponse(Call<Result> call, Response<Result> response) {
                                            Log.d("TAG", response.code() + "");

                                            if (response.isSuccessful()) {
                                                Result result = response.body();
                                                if (result.getMessage().equals("close")) {
                                                    Toast.makeText(MainActivity.this, "차문이 잠겼습니다", Toast.LENGTH_LONG).show();
                                                }
                                                else if (result.getMessage().equals("error")) {
                                                    Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(MainActivity.this, "서버 오류입니다 lock", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Result> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            }
                            catch ( Exception e )
                            {
                                e.printStackTrace();
                            }
                        }
                    }, 3000);
                }
            } );

        }
        else if(v.getId()==R.id.imageButton){

            String strData = startEditText.getText().toString();

            tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(ArrayList poiItem) {
                    TMapMarkerItem markerItem1 = new TMapMarkerItem();
                    TMapPOIItem  item = (TMapPOIItem) poiItem.get(0);
                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                            "Point: " + item.getPOIPoint().toString());
                    TMapPoint tMapPoint1 = new TMapPoint(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude());

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.suryeong);

                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                    markerItem1.setName(item.getPOIName()); // 마커의 타이틀 지정
                    tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                    startlat = item.getPOIPoint().getLatitude();
                    startlng = item.getPOIPoint().getLongitude();
                    tMapView.setCenterPoint(  item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude() );
                }
            });


        }else if(v.getId()==R.id.imageButton2){
            String strData = endEditText.getText().toString();
            resvend1.setVisibility(View.INVISIBLE);
            resvend.setVisibility(View.INVISIBLE);
            fianlres.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            resvstart1.setVisibility(View.INVISIBLE);

            tmapdata.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
                @Override
                public void onFindAllPOI(ArrayList poiItem) {
                    TMapMarkerItem markerItem2 = new TMapMarkerItem();
                    TMapPOIItem  item = (TMapPOIItem) poiItem.get(0);
                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "")  + ", " +
                            "Point: " + item.getPOIPoint().toString());
                    TMapPoint tMapPoint2 = new TMapPoint(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude());

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bannap);

                    markerItem2.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem2.setTMapPoint( tMapPoint2 ); // 마커의 좌표 지정
                    markerItem2.setName(item.getPOIName()); // 마커의 타이틀 지정
                    tMapView.addMarkerItem("markerItem2", markerItem2); // 지도에 마커 추가


                    TMapPoint tMapPointEnd = new TMapPoint(item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude());
                    endlat = item.getPOIPoint().getLatitude();
                    endlng = item.getPOIPoint().getLongitude();
                    TMapPoint tMapPointStart = new TMapPoint(startlat, startlng); // SKT타워(출발지)

                    tMapView.setCenterPoint(  item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude() );

                    search = new Search(tMapPointStart,tMapPointEnd);
                    new Thread(search).start();
                }
            });

        }else if(v.getId()==R.id.resvstart1) {
            resvstart1.setVisibility(View.INVISIBLE);
            if(alreadyreservation){
                resvstart.setEnabled(false);
            }
            else{
                resvstart.setEnabled(true);
            }
            resvend.setVisibility(View.INVISIBLE);
            resvstart.setVisibility(View.VISIBLE);

            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    TMapMarkerItem markerItem1 = new TMapMarkerItem();
                    TMapPoint tMapPoint1 = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude());

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.suryeong);

                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                    // 마커의 타이틀 지정
                    tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

                    startlat = tMapPoint.getLatitude();
                    startlng = tMapPoint.getLongitude();

                    tMapView.setCenterPoint( tMapPoint.getLongitude(), tMapPoint.getLatitude() );

                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                    return false;
                }
            });

        }else if(v.getId()==R.id.resvstart){
            resvend1.setVisibility(View.VISIBLE);
            resvstart.setVisibility(View.INVISIBLE);
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }
            });
        }else if(v.getId()==R.id.resvend1){

            resvend1.setVisibility(View.INVISIBLE);
            resvend.setVisibility(View.INVISIBLE);
            fianlres.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            if(alreadyreservation){
                reset.setEnabled(false);
                fianlres.setEnabled(false);
            }
            else{
                reset.setEnabled(true);
                fianlres.setEnabled(true);
            }
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    TMapMarkerItem markerItem2 = new TMapMarkerItem();
                    TMapPoint tMapPoint2 = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude());

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bannap);

                    markerItem2.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem2.setTMapPoint( tMapPoint2 ); // 마커의 좌표 지정


                    tMapView.addMarkerItem("markerItem2", markerItem2); // 지도에 마커 추가
                    TMapPoint tMapPointEnd = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude());
                    endlat = tMapPoint.getLatitude();
                    endlng = tMapPoint.getLongitude();

                    TMapPoint tMapPointStart = new TMapPoint(startlng, startlat); // SKT타워(출발지)

                    tMapView.setCenterPoint(  tMapPoint.getLongitude(),tMapPoint.getLatitude() );

                    search = new Search(tMapPointStart,tMapPointEnd);
                    new Thread(search).start();

                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                    return false;
                }
            });

        }else if(v.getId()==R.id.resvend){

            resvend1.setVisibility(View.INVISIBLE);
            if(alreadyreservation){
                reset.setEnabled(false);
                fianlres.setEnabled(false);
            }
            else{
                reset.setEnabled(true);
                fianlres.setEnabled(true);
            }
            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    return false;
                }
            });


        }else if(v.getId()==R.id.reset){
            tMapView.removeAllMarkerItem();
            tMapView.removeAllTMapPolyLine();

            reset.setVisibility(View.INVISIBLE);
            fianlres.setVisibility(View.INVISIBLE);

            resvend.setVisibility(View.INVISIBLE);
            resvstart1.setVisibility(View.VISIBLE);

            tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
                @Override
                public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
                    TMapMarkerItem markerItem1 = new TMapMarkerItem();
                    TMapPoint tMapPoint1 = new TMapPoint(tMapPoint.getLatitude(), tMapPoint.getLongitude());

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.suryeong);

                    markerItem1.setIcon(bitmap); // 마커 아이콘 지정
                    markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
                    markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
                    // 마커의 타이틀 지정
                    tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

                    current = tMapPoint.getLatitude();
                    current1 = tMapPoint.getLongitude();

                    tMapView.setCenterPoint( tMapPoint.getLongitude(), tMapPoint.getLatitude() );

                    return false;
                }

                @Override
                public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

                    return false;
                }
            });

        }

        else if (v.getId() == R.id.carreceive) {
            alreadyreceive = true;
            String loginid = manager.getString("idlist", "");
            MainService mainService = retrofit.create(MainService.class);
            searchLayout.setVisibility(View.INVISIBLE);
            checkLayout.setVisibility(View.INVISIBLE);
            completereserv.setVisibility(View.INVISIBLE);
            inuseLayout.setVisibility(View.VISIBLE);
            unlockButton.setVisibility(View.VISIBLE);
            carreceive.setVisibility(View.INVISIBLE);
            checkButton.setText("사용 차량 조회");
            completereserv.setVisibility(View.INVISIBLE);

            unlockButton.setEnabled(true);
            imageButton.setEnabled(false);
            imageButton2.setEnabled(false);

            mainService.getcid(loginid).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.d("TAG", response.code() + "");

                    if (response.isSuccessful()) {
                        Result result = response.body();
                        cid = Integer.parseInt(result.getMessage());
                    }
                    else {
                        Toast.makeText(MainActivity.this, "서버 오류입니다 carreceive2", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            mainService.carreceive(loginid).enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.d("TAG", response.code() + "");

                    if (response.isSuccessful()) {
                        Result result = response.body();
                        if (result.getMessage().equals("complete")) {
                            Toast.makeText(MainActivity.this, "차량 수령 완료", Toast.LENGTH_LONG).show();
                        }
                        else if (result.getMessage().equals("null")) {
                            Toast.makeText(MainActivity.this, "예약 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if (result.getMessage().equals("error")) {
                            Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, "서버 오류입니다 carreceive2", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                @Override
                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                }

            });

            webView2.getSettings().setJavaScriptEnabled(true);
            webView2.loadUrl("http://70.12.245.130/Moyeora/currentlocation.myr?id="+loginid);
            webView2.setWebViewClient(new WebViewClientClass());
        }
        else if (v.getId() == R.id.returnButton) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("차량 반납");
            dlg.setMessage("반납하시겠습니까?");
            dlg.setNegativeButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String id = manager.getString("idlist", "");
                    alreadyreceive = false;
                    alreadyreservation = false;
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            checkButton.setText("MY 차량 조회");
                            startlayout.setVisibility(View.VISIBLE);
                            destlayout.setVisibility(View.VISIBLE);

                        }
                    } );
                    MainService mainService = retrofit.create(MainService.class);
                    mainService.mapoff2(cid, 1).enqueue(new Callback<Result>() {

                        @Override
                        public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                            if(response.isSuccessful()){
                                System.out.println("asd");
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                        }

                    });
                    mainService.carreturn(id).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d("TAG", response.code() + "");

                            if (response.isSuccessful()) {
                                Result result = response.body();
                                if (result.getMessage().equals("complete")) {
                                    Toast.makeText(MainActivity.this, "차량 반납이 완료되었습니다. 이용해 주셔서 감사합니다!", Toast.LENGTH_SHORT).show();
                                }
                                else if (result.getMessage().equals("error")) {
                                    Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                }
                                else if(result.getMessage().equals("null")){
                                    System.out.println("null");
                                    Toast.makeText(MainActivity.this, "반납할 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "서버 오류입니다 carreturn", Toast.LENGTH_LONG).show();
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });

                }
            });
            dlg.setPositiveButton("아니오", null);
            dlg.show();
        } else if (v.getId() == R.id.extensionButton) {
            //사용 연장
            UsageextentionDialog usageextentionDialog = new UsageextentionDialog(MainActivity.this);
            usageextentionDialog.callFunction();
        }

    }

    class Reservation extends AppCompatActivity implements TimePicker.OnTimeChangedListener{

        private Context context;
        Button btn_resv_date;
        DatePicker dp;
        TimePicker tp;
        String gubun;
        int hourofday,minute;

        public Reservation(Context context,String gubun) {
            this.context = context;
            this.gubun = gubun;

        }


        public void callFunction() {

            fianlres.setVisibility(View.VISIBLE);
            fianlres.setEnabled(true);
            reset.setEnabled(true);
            reset.setVisibility(View.VISIBLE);

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Calendar c = Calendar.getInstance();



            int year = c.get(c.YEAR);
            int month = c.get(c.MONTH); //1월은 '0'부터 시작
            int dayOfMonth = c.get(c.DAY_OF_MONTH);

            final Dialog dlg = new Dialog(MainActivity.this);

            dlg.setContentView(R.layout.reservation_date);

            btn_resv_date = dlg.findViewById(R.id.btn_resv_date);
            dp = (DatePicker) dlg.findViewById(R.id.dp);
            tp = (TimePicker) dlg.findViewById(R.id.tp);
            choosedate = dlg.findViewById(R.id.choosedate);

            dlg.show();
            tp.setOnTimeChangedListener(this);

            dlg.findViewById(R.id.btn_resv_date).setOnClickListener(
                    new Button.OnClickListener(){
                        public void onClick(View v){
                            choosedate.setText("수령일시 선택");
                            startreservationDate = String.valueOf(dp.getYear())+"-" + String.valueOf(dp.getMonth() + 1) +"-"+ String.valueOf(dp.getDayOfMonth())+" "+hourofday+":"+minute+":00";

                        }

                    });

            dlg.findViewById(R.id.btn_get_date).setOnClickListener(
                    new Button.OnClickListener(){
                        public void onClick(View v){
                            choosedate.setText("반납일시 선택");
                            endreservationDate = String.valueOf(dp.getYear())+"-" + String.valueOf(dp.getMonth() + 1) +"-"+ String.valueOf(dp.getDayOfMonth())+" "+String.valueOf(hourofday)+":"+String.valueOf(minute)+":00";

                        }

                    });
            dlg.findViewById(R.id.btn_end_date).setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    String loginid = manager.getString("idlist", "");
                    MainService mainService = retrofit.create(MainService.class);
                    carreceive.setEnabled(true);
                    fianlres.setVisibility(View.INVISIBLE);
                    fianlres.setEnabled(false);
                    reset.setEnabled(false);
                    reset.setVisibility(View.INVISIBLE);
                    mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                        @Override
                        public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                        }

                    });
                    checkLayout.setVisibility(View.VISIBLE);
                    searchLayout.setVisibility(View.INVISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl("http://70.12.245.130/Moyeora/carlocation.myr?id="+loginid);
                    webView.setWebViewClient(new WebViewClientClass());
                    dlg.dismiss();
                    CheckActivity checkActivity = new CheckActivity(MainActivity.this);
                    checkActivity.callFunction();
                }
            });
        }

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            this.hourofday = hourOfDay;
            this.minute = minute;
        }
    }


    class CheckActivity extends AppCompatActivity {

        private Context context;
        String carString,option1String,option2String,option3String,option4String;
        Spinner spinner5,spinner6,spinner7,spinner8,spinner9;
        TextView feeTextview,drivingfeeTextview;
        ImageButton  cancelButton;
        Button reservationButton, carreceive;

        public CheckActivity(Context context) {
            this.context = context;
        }


        public void callFunction() {

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog(context);

            dlg.setContentView(R.layout.check);

            spinner5 = dlg.findViewById(R.id.spinner5);
            spinner6 = dlg.findViewById(R.id.spinner6);
            spinner7 = dlg.findViewById(R.id.spinner7);
            spinner8 = dlg.findViewById(R.id.spinner8);
            spinner9 = dlg.findViewById(R.id.spinner9);

            feeTextview = dlg.findViewById(R.id.feeTextview);
            drivingfeeTextview = dlg.findViewById(R.id.drivingfeeTextview);

            cancelButton = dlg.findViewById(R.id.cancelButton);
            reservationButton = dlg.findViewById(R.id.reservationButton);
            carreceive = MainActivity.this.findViewById(R.id.carreceive);

            final String[] data = MainActivity.this.getResources().getStringArray(R.array.cars);
            final String[] data2 = MainActivity.this.getResources().getStringArray(R.array.options);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,data);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,data2);

            spinner5.setAdapter(adapter);
            spinner6.setAdapter(adapter2);
            spinner7.setAdapter(adapter2);
            spinner8.setAdapter(adapter2);
            spinner9.setAdapter(adapter2);

            spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    carString =  data[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    option1String =  data2[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    option2String =  data2[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    option3String =  data2[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    option4String =  data2[position];
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dlg.show();
            dlg.findViewById(R.id.reservationButton).setOnClickListener(
                    new Button.OnClickListener(){
                        public void onClick(View v){
                            carreceive.setVisibility(View.VISIBLE);
                            final String loginid = manager.getString("idlist", "");


                            Retrofit retrofit2 = new Retrofit.Builder()
                                    .baseUrl(LoginService.URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            LoginService loginService = retrofit2.create(LoginService.class);
                            loginService.reregister(loginid,carString,option1String,option2String,option3String,option4String,100000,String.valueOf(startlng),String.valueOf(startlat),String.valueOf(endlng),String.valueOf(endlat),startreservationDate,endreservationDate,"예약완료").enqueue(new Callback<Result>() {

                                @Override
                                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                                    Log.d("TAG",response.code()+"");

                                    if(response.isSuccessful()){
                                        Result result =response.body();
                                        if(result.getMessage().equals("complete")){
                                            System.out.println("complete");
                                            checkLayout.setVisibility(View.VISIBLE);
                                            MainService mainService = retrofit.create(MainService.class);
                                            mainService.mapoff(loginid, 0).enqueue(new Callback<Result>() {

                                                @Override
                                                public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {

                                                }

                                                @Override
                                                public void onFailure(Call<Result> call, Throwable t) {
                                                }

                                            });
                                            alreadyreservation = true;
                                            webView.getSettings().setJavaScriptEnabled(true);
                                            webView.loadUrl("http://70.12.245.130/Moyeora/carlocation.myr?id="+loginid);
                                            webView.setWebViewClient(new WebViewClientClass());
                                            Toast.makeText(MainActivity.this, "예약 완료", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                        else if(result.getMessage().equals("error")){
                                            System.out.println("error");
                                            reset.setVisibility(View.INVISIBLE);
                                            fianlres.setVisibility(View.INVISIBLE);

                                            resvend.setVisibility(View.INVISIBLE);
                                            resvstart1.setVisibility(View.VISIBLE);
                                            Toast.makeText(MainActivity.this, "서버 에러!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else{
                                        System.out.println("response fail");
                                        Toast.makeText(MainActivity.this, "서버 오류입니다 reservation", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    t.printStackTrace();
                                    System.out.println("onfailure");
                                }
                            });

                            dlg.dismiss();
                        }

                    });


        }

    }


    class UsageextentionDialog extends AppCompatActivity {

        private Context context;
        TextView presentReturn, changeReturn;
        Button usageextensionYes, usageextensionNo, extend1, extend2, extend3;
        ImageButton usageextensionCancel;

        public UsageextentionDialog(Context context) {
            this.context = context;
        }


        public void callFunction()  {

            // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
            final Dialog dlg = new Dialog(context);

            // 액티비티의 타이틀바를 숨긴다.
            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // 커스텀 다이얼로그의 레이아웃을 설정한다.
            dlg.setContentView(R.layout.usageextension);

            // 커스텀 다이얼로그를 노출한다.
            dlg.show();

            usageextensionYes = dlg.findViewById(R.id.usageextensionYes);
            usageextensionNo = dlg.findViewById(R.id.usageextensionNo);
            usageextensionCancel = dlg.findViewById(R.id.usageextensionCancel);
            extend1 = dlg.findViewById(R.id.extend1);
            extend2 = dlg.findViewById(R.id.extend2);
            extend3 = dlg.findViewById(R.id.extend3);
            presentReturn = dlg.findViewById(R.id.presentReturn);
            changeReturn = dlg.findViewById(R.id.changeReturn);

            final String loginid = manager.getString("idlist", "");
            final String time = changeReturn.getText().toString();

            // 기존 반납일
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MenuService.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final MenuService menuService = retrofit.create(MenuService.class);
            menuService.reservationCheck(loginid).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    Order order = response.body();
                    presentReturn.setText(order.getEtime());
//                    changeReturn.setText("");

                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Reservation Check Response Fail", Toast.LENGTH_SHORT).show();
                }
            });

            usageextensionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });

            usageextensionYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainService mainService = retrofit.create(MainService.class);
                    mainService.extend(loginid, time).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                            Log.d("TAG",response.code()+"");

                            if(response.isSuccessful()){
                                Result result =response.body();
                                if(result.getMessage().equals("complete")){
                                    System.out.println("complete");
                                    Toast.makeText(MainActivity.this, "연장 완료", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else if(result.getMessage().equals("error")){
                                    System.out.println("error");
                                    Toast.makeText(MainActivity.this, "서버 에러", Toast.LENGTH_SHORT).show();
                                }
                                else if(result.getMessage().equals("null")){
                                    System.out.println("error");
                                    Toast.makeText(MainActivity.this, "연장할 차량이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                System.out.println("response fail");
                                Toast.makeText(MainActivity.this, "서버 오류입니다  usageextensionYes", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            t.printStackTrace();
                            System.out.println("onfailure");
                        }
                    });


                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });

            usageextensionNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
            });

            // 1시간 연장
            extend1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginid = manager.getString("idlist", "");
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MenuService.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.reservationCheck(loginid).enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order order = response.body();
                            presentReturn.setText(order.getEtime());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date endTime = dateFormat.parse(order.getEtime());
                                long calTime = endTime.getTime();

                                // 1시간 더하기
                                long extendedTime = calTime + 1*60*60*1000;
                                // date로 다시 바꿔주기
                                Date dated = new Date ( extendedTime );
                                // format 맞춰주기
                                String formatted = dateFormat.format(dated);
                                changeReturn.setText(""+formatted);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Reservation Check Response Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // 2시간 연장
            extend2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginid = manager.getString("idlist", "");
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MenuService.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.reservationCheck(loginid).enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order order = response.body();
                            presentReturn.setText(order.getEtime());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date endTime = dateFormat.parse(order.getEtime());
                                long calTime = endTime.getTime();

                                // 2시간 더하기
                                long extendedTime = calTime + 2*60*60*1000;
                                // date로 다시 바꿔주기
                                Date dated = new Date ( extendedTime );
                                // format 맞춰주기
                                String formatted = dateFormat.format(dated);
                                changeReturn.setText(""+formatted);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Reservation Check Response Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // 3시간 연장
            extend3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginid = manager.getString("idlist", "");
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MenuService.URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MenuService menuService = retrofit.create(MenuService.class);
                    menuService.reservationCheck(loginid).enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order order = response.body();
                            presentReturn.setText(order.getEtime());

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date endTime = dateFormat.parse(order.getEtime());
                                long calTime = endTime.getTime();

                                // 1시간 더하기
                                long extendedTime = calTime + 3*60*60*1000;
                                // date로 다시 바꿔주기
                                Date dated = new Date ( extendedTime );
                                // format 맞춰주기
                                String formatted = dateFormat.format(dated);
                                changeReturn.setText(""+formatted);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Reservation Check Response Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }

    }
}