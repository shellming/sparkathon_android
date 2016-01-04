package com.shellming.sparkathon.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shellming.sparkathon.R;
import com.shellming.sparkathon.model.TwitterModel;
import com.shellming.sparkathon.util.ToastUtil;
import com.shellming.sparkathon.util.TwitterUtil;

import java.util.Calendar;

/**
 * Created by ruluo1992 on 1/4/2016.
 */
public class SendTwitterActivity extends AppCompatActivity {
    private EditText timeEdit;
    private EditText dateEdit;
    private EditText locationEdit;
    private Button sendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendtwitter);

        timeEdit = (EditText) findViewById(R.id.time);
        dateEdit = (EditText) findViewById(R.id.date);
        locationEdit = (EditText) findViewById(R.id.location);
        sendButton = (Button) findViewById(R.id.send_btn);

        setupToolbar();
        setupTimepicker();
        setupSendBtn();

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        dateEdit.setText(date);

        timeEdit.requestFocus();
    }

    private void setupSendBtn(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = timeEdit.getText().toString();
                String date = dateEdit.getText().toString();
                String location = locationEdit.getText().toString();
                TwitterModel model = new TwitterModel(date, time, location);
                TwitterUtil.getInstance().postTweet(model.toString());
                ToastUtil.showToast(getApplicationContext(), "Send Success", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setupTimepicker(){
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                // 创建一个TimePickerDialog实例，并把它显示出来
                new TimePickerDialog(SendTwitterActivity.this,
                        // 绑定监听器
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                timeEdit.setText(hourOfDay + ":" + minute);
                            }
                        }
                        // 设置初始时间
                        , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        // true表示采用24小时制
                        true).show();
            }
        });
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
