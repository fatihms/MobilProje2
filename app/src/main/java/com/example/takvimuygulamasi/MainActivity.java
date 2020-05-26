package com.example.takvimuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takvimuygulamasi.Database.DBHelper;
import com.example.takvimuygulamasi.Model.EventModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    Context context;
    List<EventModel> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        dbHelper = new DBHelper(MainActivity.this);

        recyclerView = findViewById(R.id.rv_eventList);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        events = new ArrayList<>();

        try {
            events = dbHelper.getAllEvents();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Anımsatıcı listesine ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (events.size() == 0) {
            Toast.makeText(MainActivity.this, "Gösterilecek not bulunmuyor.", Toast.LENGTH_SHORT).show();
        }

        floatingActionButton = findViewById(R.id.fab);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createReminderActivity = new Intent(getApplicationContext(), EventCreateActivity.class);
                startActivity(createReminderActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void listEvent(int type) {

        List<EventModel> eventModelsNew = new ArrayList<>();
        Calendar today = Calendar.getInstance();

        switch (type) {
            case 1:
                for (EventModel reminderNote : events) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(reminderNote.getStartDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String monthNumber = (String) DateFormat.format("MM", date);
                    String day = (String) DateFormat.format("dd", date);
                    String year = (String) DateFormat.format("yyyy", date);

                    String tm = (String) DateFormat.format("MM", today);
                    String td = (String) DateFormat.format("dd", today);


                    if (Integer.parseInt(monthNumber) == Integer.parseInt(tm) &&
                            Integer.parseInt(day) == Integer.parseInt(td)) {
                        eventModelsNew.add(reminderNote);
                    }

                }
                Toast.makeText(MainActivity.this, "Günlük olarak listelendi", Toast.LENGTH_SHORT).show();

                break;
            case 2:
                for (EventModel reminderNote : events) {


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(reminderNote.getStartDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String monthNumber = (String) DateFormat.format("MM", date);
                    String day = (String) DateFormat.format("dd", date);
                    String year = (String) DateFormat.format("yyyy", date);

                    String tm = (String) DateFormat.format("MM", today);
                    String td = (String) DateFormat.format("dd", today);


                    if (Integer.parseInt(monthNumber) > Integer.parseInt(tm) &&
                            Integer.parseInt(day) <= (Integer.parseInt(td) + 7) % 30) {
                        eventModelsNew.add(reminderNote);

                    }
                    if (Integer.parseInt(monthNumber) == Integer.parseInt(tm) &&
                            Integer.parseInt(day) >= Integer.parseInt(td)) {
                        eventModelsNew.add(reminderNote);
                    }
                }
                Toast.makeText(MainActivity.this, "Haftalık olarak listelendi", Toast.LENGTH_SHORT).show();

                break;

            case 3:
                for (EventModel reminderNote : events) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = sdf.parse(reminderNote.getStartDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String monthNumber = (String) DateFormat.format("MM", date);
                    String day = (String) DateFormat.format("dd", date);
                    String year = (String) DateFormat.format("yyyy", date);

                    String tm = (String) DateFormat.format("MM", today);
                    String td = (String) DateFormat.format("dd", today);

                    if (Integer.parseInt(monthNumber) > Integer.parseInt(tm) &&
                            Integer.parseInt(day) <= (Integer.parseInt(td)) % 30) {
                        eventModelsNew.add(reminderNote);

                    }
                    if (Integer.parseInt(monthNumber) == Integer.parseInt(tm) &&
                            Integer.parseInt(day) >= Integer.parseInt(td)) {
                        eventModelsNew.add(reminderNote);
                    }
                }
                Toast.makeText(MainActivity.this, "Aylık olarak listelendi", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                eventModelsNew = events;
                Toast.makeText(MainActivity.this, "Tüm liste listelendi", Toast.LENGTH_SHORT).show();
                break;
        }

        EventListAdapter eventListViewAdapter = new EventListAdapter(this, eventModelsNew);
        recyclerView.setAdapter(eventListViewAdapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int type;

        switch (item.getItemId()) {
            case R.id.menuAbout:
                Intent openCreateReminderActivity = new Intent(getApplicationContext(), ReminderList.class);
                startActivity(openCreateReminderActivity);
                return true;
            case R.id.menuSettings:
                Intent openAboutActivity = new Intent(getApplicationContext(), Settings.class);
                startActivity(openAboutActivity);
                return true;
            case R.id.list1:
                type = 1;
                listEvent(1);
                return true;
            case R.id.list2:
                type = 2;
                listEvent(2);
                return true;
            case R.id.list3:
                type = 3;
                listEvent(3);
                return true;
            case R.id.list4:
                type = 4;
                listEvent(4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
