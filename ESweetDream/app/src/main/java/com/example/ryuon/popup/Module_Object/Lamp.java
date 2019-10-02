package com.example.ryuon.popup.Module_Object;

public class Lamp extends module{
    // 앞으로 구현할 부분
    String sleep_Time = "";
    String wake_Time = "";

    private String weather = "0";
    private String color = "0";
    private String power = "1"; // 전원

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




    public void setWeather(String weather){
        this.weather=weather;
    }

    public String getWeather(){
        return weather;
    }

    public void setColor(String color){
        this.color=color;
    }

    public String getColor(){
        return color;
    }

    public void setPower(String power){ this.power=power; }

    public String getPower(){ return power; }
}
