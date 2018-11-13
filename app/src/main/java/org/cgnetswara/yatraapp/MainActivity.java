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
    List<Trainee> traineeRealList;
    SharedPreferences sp;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String REQUESTTAG = "requesttag";
    RequestQueue requestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senderNumber=findViewById(R.id.editText);
        sp = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sp.contains(getString(R.string.sender_number))){
            String temp=sp.getString("sender_number","DNE");
            senderNumber.setText(temp);
            senderNumber.setEnabled(false);
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
        else{
            senderNumber.setEnabled(true);

        }
    }

    private void checkSyncStatus(List<Trainee> traineeRealList) {
        Trainee trainee;
        try {
            for (int i = 0; i < traineeRealList.size(); i++) {
                trainee = traineeRealList.get(i);
                if(trainee.getIsSynced()==0){
                    sendVolleyRequestToServer(trainee.getTraineeNumber(),trainee.getDateTime());
                }
            }
        } catch (NullPointerException e)
        {e.printStackTrace();}
    }

    public void sendVolleyRequestToServer(final String receiverNumber, final String dateTime){
        requestQueue = Volley.newRequestQueue(this);

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
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender_number", senderNumber.getText().toString());
                params.put("receiver_number", receiverNumber);
                params.put("datetime",dateTime);
                return params;
            }
        };
        stringRequest.setTag(REQUESTTAG);
        requestQueue.add(stringRequest);
    }

    public void addTrainee(View view) {
        EditText traineeNumber=findViewById(R.id.editText2);
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
            Date date = Calendar.getInstance().getTime();
            trainee.setDateTime(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date));
            trainee.setIsSynced(0);
            mTraineeViewModel.insert(trainee);
        }
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQUESTTAG);
        }
    }

}
