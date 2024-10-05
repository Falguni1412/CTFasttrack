package com.example.ctfasttrack;

public class POJOViewAllBooking {

    String id,bus_id,bus_number,bus_from,bus_to,date,time,user_name,user_mobile_no,user_address;

    public POJOViewAllBooking(String id, String bus_id, String bus_number, String bus_from, String bus_to, String date, String time, String user_name, String user_mobile_no, String user_address) {
        this.id = id;
        this.bus_id= bus_id;
        this.bus_number = bus_number;
        this.bus_from = bus_from;
        this.bus_to = bus_to;
        this.date = date;
        this.time = time;
        this.user_name = user_name;
        this.user_mobile_no = user_mobile_no;
        this.user_address = user_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getBus_number() {
        return bus_number;
    }

    public void setBus_number(String bus_number) {
        this.bus_number = bus_number;
    }

    public String getBus_from() {
        return bus_from;
    }

    public void setBus_from(String bus_from) {
        this.bus_from = bus_from;
    }

    public String getBus_to() {
        return bus_to;
    }

    public void setBus_to(String bus_to) {
        this.bus_to = bus_to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }
}
