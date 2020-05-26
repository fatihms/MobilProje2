package com.example.takvimuygulamasi;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.takvimuygulamasi.Database.DBHelper;
import com.example.takvimuygulamasi.Model.EventModel;
import com.example.takvimuygulamasi.Model.ReminderModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EventEditActivity extends AppCompatActivity {

    private EditText etEventName, etEventDescription;
    private TextView tvStartEventDate, tvEndEventDate, tvLocation, tvReminderTitle,
            tvReminderDate, tvReminderTime, tvFreqText;
    private ImageView ivStartEventDate, ivEndEventDate, ivLocation, ivReminderDate, ivReminderTime, ivBackMain;
    private Button btnSaveEvent, btnShareEvent, btnReminderAdd;
    private CheckBox cbReminder;
    private LinearLayout reminderLayout;

    EventModel eventModel;

    private Spinner spFreqReminder;
    private Context context;

    private Calendar startDate, endDate, reminderCalendar;
    private DBHelper dbHelper;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private String s_Latitude, s_Longitude, freq = "", ringTone;
    long repeat;
    long idTemp;
    int tk = 0;
    SharedP settings;

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "kk:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm:ss";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        etEventName = findViewById(R.id.et_eventNameE);
        etEventDescription = findViewById(R.id.et_eventDescriptionE);
        tvStartEventDate = findViewById(R.id.tv_startEventDateE);
        tvEndEventDate = findViewById(R.id.tv_endEventDateE);
        tvLocation = findViewById(R.id.tv_locationE);
        ivStartEventDate = findViewById(R.id.iv_startEventDateE);
        ivEndEventDate = findViewById(R.id.iv_endEventDateE);
        ivLocation = findViewById(R.id.iv_locationE);
        btnSaveEvent = findViewById(R.id.btn_saveEventE);
        ivBackMain = findViewById(R.id.backMainE);

        cbReminder = findViewById(R.id.cb_reminderE);
        tvReminderTitle = findViewById(R.id.tv_reminderTitleE);
        tvReminderDate = findViewById(R.id.tv_reminderDateE);
        tvReminderTime = findViewById(R.id.tv_reminderTimeE);
        ivReminderDate = findViewById(R.id.iv_reminderDateE);
        ivReminderTime = findViewById(R.id.iv_reminderTimeE);
        btnReminderAdd = findViewById(R.id.btn_reminderAddE);
        spFreqReminder = findViewById(R.id.sp_freqReminderE);
        reminderLayout = findViewById(R.id.reminderLayoutE);
        tvFreqText = findViewById(R.id.tv_freqTextE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        tvReminderTitle.setVisibility(View.INVISIBLE);
        tvReminderDate.setVisibility(View.INVISIBLE);
        tvReminderTime.setVisibility(View.INVISIBLE);
        ivReminderDate.setVisibility(View.INVISIBLE);
        ivReminderTime.setVisibility(View.INVISIBLE);
        btnReminderAdd.setVisibility(View.INVISIBLE);
        spFreqReminder.setVisibility(View.INVISIBLE);
        tvFreqText.setVisibility(View.INVISIBLE);
        reminderLayout.setVisibility(View.INVISIBLE);

        dbHelper = new DBHelper(EventEditActivity.this);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        reminderCalendar = Calendar.getInstance();

        context = getApplicationContext();

        settings = new SharedP();

        Bundle bund = getIntent().getExtras();
        int id = bund.getInt("id");
        idTemp = id;
        eventModel = new EventModel();
        eventModel = dbHelper.getEvent(id);


        etEventName.setText(eventModel.getName());
        etEventDescription.setText(eventModel.getDescription());
        tvStartEventDate.setText(eventModel.getStartDate().toString());
        tvEndEventDate.setText(eventModel.getEndDate().toString());
        tvLocation.setText(eventModel.getLocation().toString());


        ivBackMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMain = new Intent(EventEditActivity.this, MainActivity.class);
                startActivity(backMain);
            }
        });


        cbReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    tvReminderTitle.setVisibility(View.VISIBLE);
                    tvReminderDate.setVisibility(View.VISIBLE);
                    tvReminderTime.setVisibility(View.VISIBLE);
                    ivReminderDate.setVisibility(View.VISIBLE);
                    ivReminderTime.setVisibility(View.VISIBLE);
                    btnReminderAdd.setVisibility(View.VISIBLE);
                    spFreqReminder.setVisibility(View.VISIBLE);
                    tvFreqText.setVisibility(View.VISIBLE);
                    reminderLayout.setVisibility(View.VISIBLE);

                } else {
                    tvReminderTitle.setVisibility(View.INVISIBLE);
                    tvReminderDate.setVisibility(View.INVISIBLE);
                    tvReminderTime.setVisibility(View.INVISIBLE);
                    ivReminderDate.setVisibility(View.INVISIBLE);
                    ivReminderTime.setVisibility(View.INVISIBLE);
                    btnReminderAdd.setVisibility(View.INVISIBLE);
                    spFreqReminder.setVisibility(View.INVISIBLE);
                    tvFreqText.setVisibility(View.INVISIBLE);
                    reminderLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

        String time = settings.getStringValue(getApplicationContext(), "timeKey", "");
        tvReminderTime.setText(time);

        List<String> frequency = new ArrayList<>();
        frequency.add("Hatırlat Sıklıkları");
        frequency.add("Gün");
        frequency.add("Hafta");
        frequency.add("Ay");
        frequency.add("Yıl");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, frequency);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFreqReminder.setAdapter(adapter);

        spFreqReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    freq = "";
                } else if (position == 1) {
                    freq = "Gün";
                    repeat = 1000 * 60 * 60 * 24;
                    Toast.makeText(getApplicationContext(), "Günlük Seçildi", Toast.LENGTH_SHORT).show();
                } else if (position == 2) {
                    freq = "Hafta";
                    repeat = 7 * 1000 * 60 * 60 * 24;
                    Toast.makeText(getApplicationContext(), "Haftalık Seçildi", Toast.LENGTH_SHORT).show();
                } else if (position == 3) {
                    freq = "Ay";
                    repeat = 30 * 1000 * 60 * 60 * 24;
                    Toast.makeText(getApplicationContext(), "Aylık Seçildi", Toast.LENGTH_SHORT).show();
                } else if (position == 4) {
                    freq = "Yıl";
                    repeat = 360 * 1000 * 60 * 60 * 24;
                    Toast.makeText(getApplicationContext(), "Yıllık Seçildi", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        reminderCalendar.set(Calendar.MINUTE, minute);
                        reminderCalendar.set(Calendar.SECOND, 0);
                        reminderCalendar.set(Calendar.MILLISECOND, 0);
                        tk = 1;
                        tvReminderTime.setText(hourOfDay + ":" + minute);
                    }
                }, reminderCalendar.get(Calendar.HOUR_OF_DAY), reminderCalendar.get(Calendar.MINUTE), true);
                timePicker.show();
            }
        });

        ivReminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        reminderCalendar.set(Calendar.YEAR, year);
                        reminderCalendar.set(Calendar.MONTH, monthOfYear);
                        reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        int tempMon = monthOfYear + 1;
                        tvReminderDate.setText(dayOfMonth + "/" + tempMon + "/" + year);
                    }
                }, reminderCalendar.get(Calendar.YEAR), reminderCalendar.get(Calendar.MONTH), reminderCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        btnReminderAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reqCode = Integer.parseInt(reminderCalendar.getTimeInMillis() / (1000 * 1000 * 1000) + new Random().nextInt(100) + "");

                String ringType = settings.getStringValue(getApplicationContext(), "ringType", "ALARM");
                String freqType = settings.getStringValue(getApplicationContext(), "freqKey", "Gün");

                if (freq.equals("")) {
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

                SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
                Date date = null;
                try {
                    date = sdf.parse(tvReminderTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String tm = (String) DateFormat.format("kk", date);
                String td = (String) DateFormat.format("mm", date);

                if (tk == 0) {
                    reminderCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tm));
                    reminderCalendar.set(Calendar.MINUTE, Integer.parseInt(td));
                }

                if (ringType.equals("ALARM")) {
                    ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
                } else if (ringType.equals("NOTIFICATION")) {
                    ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString();
                } else if (ringType.equals("RINGTONE")) {
                    ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE).toString();
                }

                long rc = reminderCalendar.getTimeInMillis();

                Intent myIntent = new Intent(EventEditActivity.this, BroadcastReceiver.class);
                myIntent.putExtra("name", etEventName.getText().toString());
                myIntent.putExtra("description", etEventDescription.getText().toString());
                myIntent.putExtra("ringtone", ringTone);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(EventEditActivity.this, reqCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (repeat != 0)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rc, repeat, pendingIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, rc, pendingIntent);

                ReminderModel reminderModel = new ReminderModel();
                reminderModel.setTime(tvReminderTime.getText().toString());
                reminderModel.setDate(tvReminderDate.getText().toString());
                reminderModel.setRepeat(freq);
                reminderModel.setRequest(reqCode);


                try {
                    dbHelper.createReminder(idTemp, reminderModel);
                } catch (Exception e) {
                    Toast.makeText(EventEditActivity.this, "Etkinlik düzenlemedi", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "Hatırlatıcı Oluşturuldu", Toast.LENGTH_SHORT).show();
            }
        });


        ivStartEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePicker = new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.set(Calendar.YEAR, year);
                        startDate.set(Calendar.MONTH, monthOfYear);
                        startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        int tempMon = monthOfYear + 1;
                        tvStartEventDate.setText(dayOfMonth + "/" + tempMon + "/" + year);
                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        ivEndEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePicker = new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.set(Calendar.YEAR, year);
                        endDate.set(Calendar.MONTH, monthOfYear);
                        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        int tempMon = monthOfYear + 1;
                        tvEndEventDate.setText(dayOfMonth + "/" + tempMon + "/" + year);
                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EventEditActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(EventEditActivity.this
                            , new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, 44);
                }
            }
        });

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
                String startDateTime = dateTimeFormat.format(startDate.getTime());
                String endDateTime = dateTimeFormat.format(endDate.getTime());
                tvEndEventDate.getText().toString();
                eventModel.setName(etEventName.getText().toString());
                eventModel.setDescription(etEventDescription.getText().toString());
                eventModel.setStartDate(startDateTime);
                eventModel.setEndDate(endDateTime);
                eventModel.setLocation("http://www.google.com/maps/place/" +
                        s_Latitude + "," + s_Longitude);

                try {
                    dbHelper.updateEvent(eventModel);
                } catch (Exception e) {
                    Toast.makeText(EventEditActivity.this, "Etkinlik düzenlenemedi", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Etkinlik Düzenlendi", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(EventEditActivity.this,
                                Locale.getDefault());
                        List<Address> address = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        s_Latitude = " " + address.get(0).getLatitude();
                        s_Longitude = " " + address.get(0).getLongitude();
                        tvLocation.setText("" + address.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final LinearLayout newReminderLayout = findViewById(R.id.editLayout);
        String darkAndLight = settings.getStringValue(getApplicationContext(), "darkAndLightKEY", "LIGHT");
        if (darkAndLight.equalsIgnoreCase("LIGHT")) {
            newReminderLayout.setBackgroundColor(Color.rgb(180, 70, 70));
        } else {
            newReminderLayout.setBackgroundColor(Color.rgb(68, 220, 50));
        }
    }

    public void onFinishEditDialog(String inputText) {

    }

}
