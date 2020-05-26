package com.example.takvimuygulamasi;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.takvimuygulamasi.Database.DBHelper;
import com.example.takvimuygulamasi.Model.EventModel;
import com.example.takvimuygulamasi.Model.ReminderModel;

import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.MyViewHolder> {

    Context context;
    List<ReminderModel> reminderList;
    DBHelper dbHelper;
    LayoutInflater inflater;
    ReminderModel reminder;

    public ReminderListAdapter(Context context, List<ReminderModel> rList) {
        inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
        reminderList = rList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row2, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        reminder = reminderList.get(position);
        final ReminderModel re = reminder;
        final MyViewHolder hol = holder;

        EventModel ev = dbHelper.getEvent(reminder.getEventID());

        holder.tvListReminderTime.setText(reminder.getTime());
        holder.tvListReminderDate.setText(reminder.getDate());
        holder.tvListReminderRepeat.setText(reminder.getRepeat());
        holder.tvListReminderName.setText(ev.getName());
        holder.btnListReminderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(context, BroadcastReceiver.class);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, re.getRequest(), myIntent, 0);
                alarmManager.cancel(pendingIntent);

                dbHelper.deleteReminder(re.getId());
                reminderList.remove(hol.getAdapterPosition());
                notifyDataSetChanged();

                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnListReminderEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra("id", re.getEventID());
                context.startActivity(intent);

                Toast.makeText(context, "Ekle", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvListReminderTime, tvListReminderDate, tvListReminderRepeat, tvListReminderName;
        ImageView btnListReminderEdit, btnListReminderDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvListReminderTime = itemView.findViewById(R.id.tv_listReminderTime);
            tvListReminderDate = itemView.findViewById(R.id.tv_listReminderDate);
            tvListReminderRepeat = itemView.findViewById(R.id.tv_listReminderRepeat);
            tvListReminderName = itemView.findViewById(R.id.tv_listReminderName);

            btnListReminderEdit = itemView.findViewById(R.id.iv_reminderListEdit);
            btnListReminderDelete = itemView.findViewById(R.id.iv_reminderListDelete);

        }
    }
}
