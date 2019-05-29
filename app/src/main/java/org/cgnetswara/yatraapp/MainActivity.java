package org.cgnetswara.yatraapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TraineeViewModel mTraineeViewModel;
    private TraineeListAdapter traineeListAdapter;
    private RecyclerView recyclerView;
    private EditText senderNumber;
    private EditText senderName;
    List<Trainee> traineeRealList;
    SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String REQUESTTAG = "requesttag";
    public static final String REQUESTTAG2 = "requesttag2";
    RequestQueue requestQueue;
    StringRequest stringRequest;
    StringRequest stringRequest2;
    TextView answeredCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        answeredCount = findViewById(R.id.textView6);
        answeredCount.setText("Loading Count...");

        senderNumber=findViewById(R.id.editText);
        senderName=findViewById(R.id.editText4);
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sp.contains(getString(R.string.sender_number)) && sp.contains(getString(R.string.sender_name))){
            String temp=sp.getString("sender_number","DNE");
            senderNumber.setText(temp);
            senderNumber.setEnabled(false);
            String temp2=sp.getString("sender_name","DNE");
            senderName.setText(temp2);
            senderName.setEnabled(false);
        }
        else{
            onChoose();
        }

        recyclerView = findViewById(R.id.recyclerview);
        traineeListAdapter = new TraineeListAdapter(this);
        recyclerView.setAdapter(traineeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTraineeViewModel = ViewModelProviders.of(this).get(TraineeViewModel.class);
        mTraineeViewModel.getAllTrainees().observe(MainActivity.this, new Observer<List<Trainee>>() {
            @Override
            public void onChanged(@Nullable List<Trainee> traineeList) {
                traineeListAdapter.setTrainees(traineeList);
                //Checking count here
                checkAnsweredStatus(traineeList);
            }
        });
        final Handler mHandler=new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                traineeRealList=mTraineeViewModel.getAllTraineesAsList();
                checkSyncStatus(traineeRealList);
                mHandler.postDelayed(this, 2000);
            }
        };
        mHandler.post(runnable);
    }


    public void onChoose(View view) {
        onChoose();
    }

    private void onChoose() {
        SharedPreferences.Editor editor = sp.edit();
        if(senderNumber.isEnabled()){
            if(senderNumber.length()!=10) {
                senderNumber.setError("कृपया 10 अंकों का फोन नंबर दर्ज करें !");
            }
            else{
                senderNumber.setEnabled(false);
                editor.putString(getString(R.string.sender_number),senderNumber.getText().toString());
                editor.apply();
            }
        }
        if(senderName.isEnabled()){
            if(senderName.length()== 0) {
                senderName.setError("कृपया उचित नाम भरें !");
            }
            else{
                senderName.setEnabled(false);
                editor.putString(getString(R.string.sender_name),senderName.getText().toString());
                editor.apply();
            }
        }
        else{
            senderNumber.setEnabled(true);
            senderName.setEnabled(true);
        }
    }

    private void checkSyncStatus(List<Trainee> traineeRealList) {
        Trainee trainee;
        try {
            for (int i = 0; i < traineeRealList.size(); i++) {
                trainee = traineeRealList.get(i);
                if(trainee.getIsSynced()==0){
                    sendVolleyRequestToServer(trainee.getTraineeNumber(),trainee.getTraineeName(),trainee.getDateTime());
                }
            }
        } catch (NullPointerException e)
        {e.printStackTrace();}
    }

    private void checkAnsweredStatus(List<Trainee> traineeRealList) {
        Trainee trainee;
        int count;
        try {
            count = traineeRealList.size();
        }
        catch(NullPointerException e){
            count=0;
            e.printStackTrace();
        }
        try {
            for (int i = 0; i < traineeRealList.size(); i++) {
                trainee = traineeRealList.get(i);
                Log.d("count", ""+count);
                if(trainee.getIsAnswered()==0){
                    count=count-1;
                    sendVolleyRequestToServer_AnsweredStatus(trainee.getTraineeNumber());
                }
            }
        } catch (NullPointerException e)
        {e.printStackTrace();}
        String displayCount="People Called: "+count;
        answeredCount.setText(displayCount);
    }

    private void sendVolleyRequestToServer_AnsweredStatus(final String receiverNumber) {
        String url = getString(R.string.base_url) + "yatraAnsweredData";

        stringRequest2 = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Done!")){
                            mTraineeViewModel.updateAnsweredStatus(receiverNumber);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("receiver_number", "0"+receiverNumber);
                return params;
            }
        };
        stringRequest2.setTag(REQUESTTAG2);
        requestQueue.add(stringRequest2);
    }

    public void sendVolleyRequestToServer(final String receiverNumber,final String receiverName, final String dateTime){
        String url = getString(R.string.base_url) + "yatradata";

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Done!")){
                            mTraineeViewModel.updateSyncStatus(receiverNumber);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender_number", senderNumber.getText().toString());
                params.put("sender_name", senderName.getText().toString());
                params.put("receiver_number", receiverNumber);
                params.put("receiver_name", receiverName);
                params.put("datetime",dateTime);
                return params;
            }
        };
        stringRequest.setTag(REQUESTTAG);
        requestQueue.add(stringRequest);
    }

    public void addTrainee(View view) {
        EditText traineeNumber=findViewById(R.id.editText2);
        EditText traineeName=findViewById(R.id.editText3);
        if(traineeNumber.length()!=10){
            traineeNumber.setError("कृपया 10 अंकों का फोन नंबर दर्ज करें !");
        }
        else if(senderNumber.length()!=10){
            senderNumber.setError("कृपया 10 अंकों का फोन नंबर दर्ज करें !");
        }
        else {
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            Trainee trainee = new Trainee();
            trainee.setTraineeNumber(traineeNumber.getText().toString());
            trainee.setTraineeName(traineeName.getText().toString());
            trainee.setTrainerNumber(senderNumber.getText().toString());
            trainee.setTrainerName(senderName.getText().toString());
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String date = simpleDateFormat.format(new Date());
            trainee.setDateTime(date);
            trainee.setIsSynced(0);
            mTraineeViewModel.insert(trainee);
            //Clearing the textboxes post receiving input
            traineeNumber.getText().clear();
            traineeName.getText().clear();
        }
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUESTTAG);
            requestQueue.cancelAll(REQUESTTAG2);
        }
    }

    public void deleteTrainee(View view) {

    }
}
