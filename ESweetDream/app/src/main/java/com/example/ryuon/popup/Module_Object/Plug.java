package com.example.ryuon.popup.Module_Object;

public class Plug extends module {
    int temperature;
    int humidity;
    int temperature_ud; // 플러그1 전원
    int humidity_ud; // 플러그2 전원

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature_ud() {
        return temperature_ud;
    }

    public void setTemperature_ud(int temperature_ud) {
        this.temperature_ud = temperature_ud;
    }

    public int getHumidity_ud() {
        return humidity_ud;
    }

    public void setHumidity_ud(int humidity_ud) {
        this.humidity_ud = humidity_ud;
    }

}
