package com.example.takvimuygulamasi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.takvimuygulamasi.Database.DBHelper;
import com.example.takvimuygulamasi.Model.EventModel;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    Context context;
    List<EventModel> eventList;
    DBHelper dbHelper;
    LayoutInflater inflater;
    EventModel event;

    public EventListAdapter(Context context, List<EventModel> eList) {
        inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
        eventList = eList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        event = eventList.get(position);
        final EventModel ev = event;
        final MyViewHolder hol = holder;
        holder.tvListEventName.setText(event.getName());
        holder.tvListEventDescription.setText(event.getDescription());
        holder.tvListStartDate.setText(event.getStartDate());
        holder.tvListEndDate.setText(event.getEndDate());
        holder.tvListLocation.setText(event.getLocation());
        holder.btnListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteEvent(ev.getId());
                eventList.remove(hol.getAdapterPosition());
                notifyDataSetChanged();

                Toast.makeText(context, "Silindi", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnListEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventEditActivity.class);
                intent.putExtra("id", ev.getId());
                context.startActivity(intent);

                Toast.makeText(context, "Düzenle", Toast.LENGTH_SHORT).show();

            }
        });
        holder.btnListShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareBody = "Etkinliğin adı:" + event.getName() + " Etkinliğin açıklaması: "+ event.getDescription();
                String shareSub = "f";

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

                context.startActivity(Intent.createChooser(shareIntent, "Paylaş"));

                Toast.makeText(context, "Paylaş", Toast.LENGTH_SHORT).show();

            }
        });
        holder.btnListSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO_EMAILS = {"example@gmail.com", "a@a.com"};
                String[] CC = {"example@gmail.com", "a@a.com"};

                Intent sendMailIntent = new Intent(Intent.ACTION_SENDTO);
                sendMailIntent.setData(Uri.parse("mailto:"));
                sendMailIntent.putExtra(Intent.EXTRA_EMAIL, TO_EMAILS);
                sendMailIntent.putExtra(Intent.EXTRA_CC, CC);

                sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "Etkinliğin adı:" + event.getName());
                sendMailIntent.putExtra(Intent.EXTRA_TEXT, " Etkinliğin açıklaması: "+ event.getDescription());

                context.startActivity(Intent.createChooser(sendMailIntent, "Gönderim tütürü seçiniz"));

            }
        });

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvListEventName, tvListEventDescription, tvListStartDate, tvListEndDate, tvListLocation;
        ImageView btnListEdit, btnListDelete, btnListShare, btnListSendMail;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvListEventName = itemView.findViewById(R.id.tv_listEventName);
            tvListEventDescription = itemView.findViewById(R.id.tv_listEventDescription);
            tvListStartDate = itemView.findViewById(R.id.tv_listStartDate);
            tvListEndDate = itemView.findViewById(R.id.tv_listEndDate);
            tvListLocation = itemView.findViewById(R.id.tv_listLocation);

            btnListDelete = itemView.findViewById(R.id.btn_listDelete);
            btnListEdit = itemView.findViewById(R.id.btn_listEdit);
            btnListShare = itemView.findViewById(R.id.btn_listShare);
            btnListSendMail = itemView.findViewById(R.id.btn_listSendMail);
        }
    }
}
