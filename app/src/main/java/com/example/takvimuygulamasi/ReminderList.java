package com.example.takvimuygulamasi;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takvimuygulamasi.Database.DBHelper;
import com.example.takvimuygulamasi.Model.ReminderModel;

import java.util.ArrayList;
import java.util.List;

public class ReminderList extends AppCompatActivity {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    ImageView ivBackListMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        ivBackListMain = findViewById(R.id.iv_backListMain);

        dbHelper = new DBHelper(ReminderList.this);
        recyclerView = findViewById(R.id.rv_reminderList);

        LinearLayoutManager llm = new LinearLayoutManager(ReminderList.this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        List<ReminderModel> reminders = new ArrayList<>();

        ivBackListMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMainIntent = new Intent(ReminderList.this, MainActivity.class);
                startActivity(backMainIntent);
            }
        });

        try {
            reminders = dbHelper.getAllReminders();
        } catch (Exception e) {
            Toast.makeText(ReminderList.this, "Listeye ulaşılamadı.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (reminders.size() == 0) {
            Toast.makeText(ReminderList.this, "Liste boş.", Toast.LENGTH_SHORT).show();
        }

        ReminderListAdapter reminderListViewAdapter = new ReminderListAdapter(this, reminders);
        recyclerView.setAdapter(reminderListViewAdapter);
    }

}
