package com.example.ryuon.popup.Module_Object;

public class Lamp extends module{
    // 앞으로 구현할 부분
    String sleep_Time;
    String sleep_Hour;
    String sleep_Min;

    private String weather = "0";
    private String color = "0";
    private String power = "1"; // 전원
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
