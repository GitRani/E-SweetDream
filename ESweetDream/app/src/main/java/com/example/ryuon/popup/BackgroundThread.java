package com.example.ryuon.popup;

public class BackgroundThread implements Runnable {
    public BackgroundThread() {
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("★☆★☆★☆★☆★☆★☆running★☆★☆★☆★☆★☆★☆★☆★☆");
        }

    }
}
