package com.example.takvimuygulamasi.Model;

public class ReminderModel {

    int id, request, eventID;
    String time, date, repeat;
    String created_at;

    public ReminderModel() {

    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public ReminderModel(String time, String date, String repeat, int request, int eventID) {
        this.time = time;
        this.date = date;
        this.repeat = repeat;
        this.request = request;
        this.eventID = eventID;
    }

    public ReminderModel(int id, String time, String date, String repeat, int request, int eventID) {
        this.id = id;
        this.time = time;
        this.date = date;
        this.repeat = repeat;
        this.request = request;
        this.eventID = eventID;
    }

    public int getRequest() {
        return request;
    }

    public void setRequest(int request) {
        this.request = request;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }
}
