package com.winter.picker;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.TimePickerDialog;
import com.example.liangmutian.mypicker.TimezonePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button zone;
    Button date;
    Button time;
    private Dialog dateDialog, timeDialog, timezoneDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zone = findViewById(R.id.zone);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        zone.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone:
                openZone();
                break;
            case R.id.date:
                openDate();
                break;
            case R.id.time:
                openTime();
                break;
            default:
                break;
        }
    }

    private void openTime() {
        Calendar calendar = Calendar.getInstance();
        List<Integer> time = new ArrayList<>();
        time.add(calendar.get(Calendar.HOUR_OF_DAY));
        time.add(calendar.get(Calendar.MINUTE));
        TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this)
                .setSelectHour(time.get(0))
                .setSelectMinute(time.get(1));
        timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int[] times) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, times[0]);
                calendar.set(Calendar.MINUTE, times[1]);
                calendar.set(Calendar.SECOND, 0);
                Toast.makeText(MainActivity.this, "time = " + calendar.toString(), Toast.LENGTH_SHORT).show();

            }
        }).create();

        timeDialog.show();
    }


    private void openDate() {
        Calendar calendar = Calendar.getInstance();
        List<Integer> date = new ArrayList<>();
        date.add(calendar.get(Calendar.YEAR));
        date.add(calendar.get(Calendar.MONTH));
        date.add(calendar.get(Calendar.DAY_OF_MONTH));
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this)
                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1))
                .setSelectDay(date.get(2) - 1);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, dates[0]);
                calendar.set(Calendar.MONTH, dates[1] - 1);
                calendar.set(Calendar.DAY_OF_MONTH, dates[2]);
                Toast.makeText(MainActivity.this, "date = " + calendar.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }
        });

        dateDialog = builder.create();
        dateDialog.show();
    }

    private void openZone() {
        String zone = TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
        if (zone.contains("格林尼治标准时间")) {
            zone = zone.replace("格林尼治标准时间", "GMT");
            char[] strs = zone.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < strs.length; i++) {
                sb.append(strs[i]);
                if (i == 5) {
                    sb.append(":");
                }
            }
            zone = sb.toString();
        }
        TimezonePickerDialog.Builder builder = new TimezonePickerDialog.Builder(this)
                .setsSlectValue(zone);
        timezoneDialog = builder.setOnTimeSelectedListener(new TimezonePickerDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(String value) {
                String zone_value = value.substring(value.indexOf("(") + 1).replace(")", "");
                Toast.makeText(MainActivity.this, "value = " + zone_value, Toast.LENGTH_SHORT).show();
            }
        }).create();
        timezoneDialog.show();
    }


}
