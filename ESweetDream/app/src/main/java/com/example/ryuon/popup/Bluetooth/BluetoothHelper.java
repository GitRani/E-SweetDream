package com.example.ryuon.popup.Bluetooth;

import com.example.ryuon.popup.Activity.GroupEditingActivity_new;
import com.example.ryuon.popup.Module_Object.Group;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothHelper implements Serializable, Runnable {
    private static Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터

    public ArrayList<BluetoothSocket> bluetoothSockets;
    static ArrayList<OutputStream> outputStreams;
    ArrayList<InputStream> inputStreams;
    ArrayList<String> module_name; // GroupControlActivity에서 넘겨준 module 이름

    boolean running = true;

    public BluetoothHelper() {
        // 블루투스 활성화하기
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            // 여기에 처리 할 코드를 작성하세요.
        } else { // 디바이스가 블루투스를 지원 할 때
            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                GroupEditingActivity_new.setModuleList(selectBluetoothDevice()); // 블루투스 디바이스 선택 함수 호출
            }
        }
    }

    @Override
    public void run() {
        connectDevice(module_name);

        try {
          while (!Thread.currentThread().isInterrupted()) {
              receiveData(findingIndex("센서모듈"));
              Thread.sleep(3000);
              System.out.println("Thread is alive ..");
          }
        } catch (InterruptedException e) {

        } finally {
            System.out.println("Thread is dead !!");
            disconnectDevice();
        }


    }

    public void setModuleName(ArrayList<String> moduleName) {
        this.module_name = moduleName;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public ArrayList<String> selectBluetoothDevice() {
        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> moduleList = new ArrayList<>();
        // 페어링 된 디바이스의 크기를 저장
        int pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if (pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
        } else {
            // 페어링된 디바이스중에서 E편한꿀잠에 관련된 모듈의 이름을 리스트에 추가
            for (BluetoothDevice bluetoothDevice : devices) {
                if (bluetoothDevice.getName().contains("ESD0-")) {
                    moduleList.add(bluetoothDevice.getName());
                }
            }
        }
        return moduleList;
    }

    public void connectDevice(ArrayList deviceNames) {
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
        bluetoothSockets = new ArrayList<>();
        outputStreams = new ArrayList<>();
        inputStreams = new ArrayList<>();

        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if(deviceNames.contains(tempDevice.getName())) {
                bluetoothDevices.add(tempDevice);
            }
        }
        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            for (int i = 0; i < bluetoothDevices.size(); i++) {
                bluetoothSockets.add(bluetoothDevices.get(i).createRfcommSocketToServiceRecord(uuid));
                bluetoothSockets.get(i).connect();
                // 데이터 송,수신 스트림을 얻어옵니다.
                outputStreams.add(bluetoothSockets.get(i).getOutputStream());
                inputStreams.add(bluetoothSockets.get(i).getInputStream());
            }
//            // 데이터 수신 함수 호출 - 센서모듈만
//            receiveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectDevice() {
//        System.out.println("☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★done☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★");

        try {
            for (int i = 0; i < bluetoothSockets.size(); i++) {
                inputStreams.get(i).close();
                outputStreams.get(i).close();
                bluetoothSockets.get(i).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static OutputStream outputStream = null;
    // 모듈에 맞는 outputStream을 get하여 아두이노로 송신한다.
    public static void send_Data(int deviceIndex, String send_Info) {
        try{
            // 데이터 송신
            outputStream = outputStreams.get(deviceIndex);
            outputStream.write(send_Info.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int findingIndex(String moduleName) {
        int index = 0;
        int deviceIndex = 0;

        for (BluetoothDevice bluetoothDevice : devices) {
            if (bluetoothDevice.getName().contains(moduleName)) {
                deviceIndex = index;
            }
            index++;
        }
        return deviceIndex;
    }

    Byte humidity = 0;
    Byte temperature = 0;
    Byte lux = 0;
    public void receiveData(int deviceIndex) {
        final InputStream inputStream = inputStreams.get(deviceIndex);
        // 데이터를 수신하기 위한 버퍼를 생성
        int readBufferPosition = 0;
        byte[] readBuffer = new byte[1024];

        try {
            int byteAvailable = inputStream.available(); // 데이터를 수신했는지 확인합니다.
            if (byteAvailable > 0) {                     // 데이터가 수신 된 경우
                byte[] bytes = new byte[byteAvailable];  // 입력 스트림에서 바이트 단위로 읽어 옵니다.
                inputStream.read(bytes);                 // 읽은 데이터를 bytes에 저장하고 읽은 바이트 수를 반환하는 read() 메소드.

                int counter = 0;
                for (int i = 0; i < byteAvailable; i++) {
                    byte tempByte = bytes[i];

                    switch (counter) {
                        case 0:
                            humidity = tempByte;
                            counter++;
                            break;
                        case 1:
                            temperature = tempByte;
                            counter++;
                            break;
                        case 2:
                            lux = tempByte;
                            counter++;
                            break;
                        case 3:
                            counter = 0;
                            break;
                    }
                    
                    System.out.println("☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★☆★ : " + humidity + " " + temperature + " "  + lux);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


//        // 데이터를 수신하기 위한 쓰레드 생성
//        Thread workerThread = new Thread(new Runnable() {
//            int readBufferPosition = 0;
//            byte[] readBuffer = new byte[1024];
//
//
//
//            @Override
//            public void run() {
//                while (Thread.currentThread().isInterrupted()) {
//                    try {
//                        // 데이터를 수신했는지 확인합니다.
//                        int byteAvailable = inputStream.available();
//                        // 데이터가 수신 된 경우
//                        if (byteAvailable > 0) {
//                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
//                            byte[] bytes = new byte[byteAvailable];
//                            inputStream.read(bytes); // 읽은 데이터를 bytes에 저장하고 읽은 바이트 수를 반환하는 read() 메소드.
//                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
//                            for (int i = 0; i < byteAvailable; i++) {
//                                byte tempByte = bytes[i];
//                                // 개행문자를 기준으로 받음(한줄)
//                                if (tempByte == '\n') {
//                                    // readBuffer 배열을 encodedBytes로 복사
//                                    byte[] encodedBytes = new byte[readBufferPosition];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    // 인코딩 된 바이트 배열을 문자열로 변환
//                                    final String text = new String(encodedBytes, "US-ASCII");
//                                    readBufferPosition = 0;
////                                    handler.post(new Runnable() {
////                                        @Override
////                                        public void run() {
////                                            // 텍스트 뷰에 출력
////                                            textViewReceive.append(text + "\n");
////                                        }
////                                    });
//                                } // 개행 문자가 아닐 경우
//                                else {
//                                    readBuffer[readBufferPosition++] = tempByte;
//                                    System.out.println(tempByte);
//                                }
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        // 1초마다 받아옴
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        workerThread.start();
    }
}
