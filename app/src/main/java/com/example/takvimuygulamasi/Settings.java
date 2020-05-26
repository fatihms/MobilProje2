package com.example.takvimuygulamasi;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Settings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedP settings;
    private Spinner ringToneSpinner;
    private Switch darkAndLightSwitch;
    private LinearLayout settingsLayout;
    private Button btnSaveSettings;
    private ImageView timeSetting, ivBackMain;
    private Calendar timeCalendar;
    private TextView tvTimeSetting;
    private RadioGroup rg;
    private RadioButton rb, rb1, rb2, rb3, rb4;

    String ringSound, freqType, freq = "", ringType="";
    long repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkAndLightSwitch = findViewById(R.id.sw_darkAndLight);
        settingsLayout = findViewById(R.id.settings_layout);
        btnSaveSettings = findViewById(R.id.btn_settingsSave);
        timeSetting = findViewById(R.id.iv_timeSetting);
        tvTimeSetting = findViewById(R.id.tv_timeSetting);
        ringToneSpinner = findViewById(R.id.sp_ringTone);
        ivBackMain = findViewById(R.id.backMainS);
        rg = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb1.setChecked(false);
        rb2.setChecked(false);
        rb3.setChecked(false);
        rb4.setChecked(false);

        settings = new SharedP();

        timeCalendar = Calendar.getInstance();

        ivBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMain = new Intent(Settings.this, MainActivity.class);
                startActivity(backMain);
            }
        });


        // ---------------Hatırlatma Sıklığı

        String fr = settings.getStringValue(getApplicationContext(), "freqKey", "");

        if (fr.equals("Gün")) {
            rb1.setChecked(true);
        } else if (fr.equals("Hafta")) {
            rb2.setChecked(true);
        } else if (fr.equals("Ay")) {
            rb3.setChecked(true);
        } else if (fr.equals("Yıl")) {
            rb4.setChecked(true);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = findViewById(checkedId);
                if (rb != null) {
                    freqType = rb.getText().toString();
                }
                if (freqType.equals("Gün")) {
                    repeat = 1000 * 60 * 60 * 24;
                    freq = "Gün";
                } else if (freqType.equals("Hafta")) {
                    repeat = 7 * 1000 * 60 * 60 * 24;
                    freq = "Hafta";
                } else if (freqType.equals("Ay")) {
                    repeat = 30 * 1000 * 60 * 60 * 24;
                    freq = "Ay";
                } else if (freqType.equals("Yıl")) {
                    repeat = 360 * 1000 * 60 * 60 * 24;
                    freq = "Yıl";
                }
            }
        });


        //----------------dark and light

        if ((settings.getStringValue(getApplicationContext(), "darkAndLightKEY", "")).equalsIgnoreCase("DARK")) {
            settingsLayout.setBackgroundColor(Color.rgb(68, 220, 50));
            darkAndLightSwitch.setChecked(false);
        } else {
            settingsLayout.setBackgroundColor(Color.rgb(180, 70, 70));
            darkAndLightSwitch.setChecked(true);
        }

        darkAndLightSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkAndLightSwitch.isChecked()) {
                    settingsLayout.setBackgroundColor(Color.rgb(180, 70, 70));
                } else {
                    settingsLayout.setBackgroundColor(Color.rgb(68, 220, 50));
                }
            }
        });


        // --------------date

        String tim = settings.getStringValue(getApplicationContext(), "timeKey", "16:00");
        tvTimeSetting.setText(tim);

        timeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        timeCalendar.set(Calendar.MINUTE, minute);
                        timeCalendar.set(Calendar.SECOND, 0);
                        timeCalendar.set(Calendar.MILLISECOND, 0);
                        tvTimeSetting.setText(hourOfDay + ":" + minute);
                    }
                }, timeCalendar.get(Calendar.HOUR_OF_DAY), timeCalendar.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        // ring

        List<String> ringTone = new ArrayList<>();
        ringTone.add("ALARM");
        ringTone.add("NOTIFICATION");
        ringTone.add("RINGTONE");

        ringToneSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterRing = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ringTone);
        adapterRing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringToneSpinner.setAdapter(adapterRing);

        String ring = settings.getStringValue(getApplicationContext(), "ringType", "ALARM");

        if (ring.equals("ALARM")) {
            ringToneSpinner.setSelection(0);
        } else if (ring.equals("NOTIFICATION")) {
            ringToneSpinner.setSelection(1);
        } else if (ring.equals("RINGTONE")) {
            ringToneSpinner.setSelection(2);
        }

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedP set = new SharedP();
                String switchValue;

                if (darkAndLightSwitch.isChecked()) {
                    switchValue = "LIGHT";
                } else {
                    switchValue = "DARK";
                }

                String timeCal = tvTimeSetting.getText().toString();

                set.putStringValue(getApplicationContext(), "darkAndLightKEY", switchValue);
                set.putStringValue(getApplicationContext(), "ringToneKey", ringSound);
                set.putStringValue(getApplicationContext(), "ringType", ringType);
                set.putStringValue(getApplicationContext(), "timeKey", timeCal);
                set.putStringValue(getApplicationContext(), "freqKey", freq);

                Toast.makeText(getApplicationContext(), "Ayarlandı", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ringSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        if (position == 0) {
            ringType = "ALARM";
            ringSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
        } else if (position == 1) {
            ringType = "NOTIFICATION";
            ringSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
        } else if (position == 2) {
            ringType = "RINGTONE";
            ringSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
