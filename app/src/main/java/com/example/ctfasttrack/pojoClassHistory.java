package com.example.ctfasttrack;

public class pojoClassHistory {

    public String busno;
    public String from;
    public String to;
    public String date;

    public pojoClassHistory(String busno, String from, String to, String date) {
        this.busno = busno;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
