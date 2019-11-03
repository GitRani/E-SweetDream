package com.example.ryuon.popup.Module_Object;

public class Blind extends module {
    int lux;

    String sleep_Time = "";
    String wake_Time = "";

    public int getlux() {
        return lux;
    }

    public void setLux(int lux) {
        this.lux = lux;
    }

    public void set_sleep_time(String sleep_time){
        this.sleep_Time = sleep_time;
    }

    public String get_sleep_time(){
        return sleep_Time;
    }

    public void set_wake_time(String wake_time){
        this.wake_Time = wake_time;
    }

    public String get_wake_time(){
        return wake_Time;
    }
}