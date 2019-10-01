//package com.example.ryuon.popup.Weather;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.example.ryuon.popup.Activity.GroupControlActivity;
//import com.example.ryuon.popup.Module_Object.Lamp;
//import com.example.ryuon.popup.Module_Object.module;
//import com.google.gson.JsonObject;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.GET;
//import retrofit2.http.Header;
//import retrofit2.http.Query;
//
//public class weather implements LocationListener {
//
//    LocationManager locationManager;
//    double latitude;
//    double longitude;
//    //날씨 결과
//    String result;
//
//    Lamp lamp;
//
//    public weather(module module){
//        // 새로추가
//            requestLocation();
//            lamp = (Lamp)module;
//    }
//
//
//    //날씨
//    private void requestLocation() {
//        //사용자로 부터 위치정보 권한체크
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1,  this);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, this);
//
//        }
//    }
//
//    //날씨
//    private interface ApiService {
//        //베이스 Url
//        String BASEURL = "https://api2.sktelecom.com/";
//        String APPKEY = "0da20112-f3e2-4352-a3b1-00c0782c5187";
//
//        //get 메소드를 통한 http rest api 통신
//        @GET("weather/current/hourly")
//        Call<JsonObject> getHourly(@Header("appkey") String appKey, @Query("version") int version,
//                                   @Query("lat") double lat, @Query("lon") double lon);
//
//    }
//
//    //날씨
//    @Override
//    public void onLocationChanged(Location location) {
//        /*현재 위치에서 위도경도 값을 받아온뒤 우리는 지속해서 위도 경도를 읽어올것이 아니니
//        날씨 api에 위도경도 값을 넘겨주고 위치 정보 모니터링을 제거한다.*/
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        //날씨 가져오기 통신
//        getWeather(latitude, longitude);
//        //위치정보 모니터링 제거
//        locationManager.removeUpdates((LocationListener) weather.this);
//    }
//
//    //날씨
//    private void getWeather(double latitude, double longitude) {
//        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(weather.ApiService.BASEURL)
//                .build();
//        weather.ApiService apiService = retrofit.create(weather.ApiService.class);
//        Call<JsonObject> call = apiService.getHourly(weather.ApiService.APPKEY, 1, latitude, longitude);
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    //날씨데이터를 받아옴
//                    JsonObject object = response.body();
//                    if (object != null) {
//                        result=object.toString();
//
//                        if(result.contains("맑음")){
//                            lamp.setWeather("1");
//                        }
//                        else if(result.contains("구름") || result.contains("흐림")){
//                            lamp.setWeather("2");
//                        }
//                        else if(result.contains("비")){
//                            lamp.setWeather("3");
//                        }
//                        else{
//                            lamp.setWeather("4");
//                        }
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
//            }
//        });
//    }
//
//    //날씨. 오버라이드 필수
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//    //날씨. 오버라이드 필수
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//    //날씨. 오버라이드 필수
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//}
